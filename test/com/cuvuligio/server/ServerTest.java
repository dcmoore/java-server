package com.cuvuligio.server;

import com.cuvuligio.server.responses.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class ServerTest {
    class TestServer extends Server {
        public Runnable createTask(Socket s, Map<String, ServerResponse> r) {
            return new TestTask();
        }
    }

    class TestTask implements Runnable {
        public void run() {
            System.out.println("Test Task Running");
        }
    }

    private TestServer server;

    @Before
    public void initialize() {
        server = new TestServer();
        server.setCurrentPort(8576);
    }

    @Test
    public void configurePort() {
        assertEquals(8576, server.getCurrentPort());

        server.setCurrentPort(8686);
        assertEquals(8686, server.getCurrentPort());
    }

    @Test
    public void configureRoutesTest() {
        ServerResponse response1 = new EchoResponse();
        ServerResponse response2 = new TimeResponse();

        server.addRoute("/echo", response1);
        server.addRoute("/time", response2);

        Map<String, ServerResponse> testRoutesMap = new HashMap<String, ServerResponse>();
        testRoutesMap.put("/echo", response1);
        testRoutesMap.put("/time", response2);
        assertEquals(testRoutesMap, server.getRoutes());

        server.removeRoute("/time");
        testRoutesMap.remove("/time");
        assertEquals(testRoutesMap, server.getRoutes());
    }

    @Test
    public void runServerTest() { //TODO - gets stuck when address is already in use
        assertFalse(server.isActive());
        server.start();

        try {
            while(!server.isActive()){ Thread.yield(); }
            while(!server.gracefulKill()) { Thread.yield(); }
        } catch (Exception e) {
            assertTrue(false); //Shouldn't run
        }

        assertFalse(server.isActive());
    }

    @Test
    public void acceptsConnectionsOnlyWhenServerIsRunning() {
        server.start();
        while(!server.isActive()) { Thread.yield(); }
        assertEquals(0, server.getNumRequests());

        try {
            Socket test = new Socket("localhost", 8576);
            test.getOutputStream().write("This request should go through".getBytes());
            while(server.getNumRequests() != 1) { Thread.yield(); }
            test.close();
            assertTrue(server.kill());
        } catch (Exception e) {
            assertTrue(false); //Shouldn't run
        }

        try {
            new Socket("localhost", 8576);
            assertTrue(false); //Shouldn't run
        } catch (Exception e) {
            assertTrue(true); //1st line in the try block should create an error
        }

        assertEquals(1, server.getNumRequests());
    }

    @Test
    public void acceptsSimultaneousConnections() {
        server.start();
        while(!server.isActive()) { Thread.yield(); }
        assertEquals(0, server.getNumRequests());

        try {
            Socket conn1 = new Socket("localhost", 8576);
            Socket conn2 = new Socket("localhost", 8576);

            conn1.getOutputStream().write("This request should go through".getBytes());
            conn2.getOutputStream().write("This request should also go through".getBytes());

            while(!(server.getNumRequests() == 2)) { Thread.yield(); }
            conn1.close();
            conn2.close();

            while(!server.gracefulKill()) { Thread.yield(); }
        } catch (Exception e) {
            assertTrue(false);
        }

        assertEquals(2, server.getNumRequests());
    }

    @Test
    public void acceptsManyConnections() {
        server.start();
        while(!server.isActive()) { Thread.yield(); }

        try {
            for(int i = 1; i <= 50; i++) {
                Socket conn = new Socket("localhost", 8576);
                conn.getOutputStream().write("This request should go through".getBytes());
                while(server.getNumRequests() != i) { Thread.yield(); }
                conn.close();
            }

            while(!server.gracefulKill()) { Thread.yield(); }
        } catch (Exception e) {}

        assertEquals(50, server.getNumRequests());
    }
}