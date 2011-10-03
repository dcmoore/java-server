package com.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpRequestHandlerTest {
    class MockTask implements Runnable {
        private Socket socket;

        public MockTask(Socket s) {
            socket = s;
        }

        public void run() {
            try {
                socket.close();
            }
            catch (Exception e) {}
        }
    }

    class TestRequestHandler extends HttpRequestHandler {
        public Runnable createTask(Socket s) throws IOException {
            this.setNumRequests(1);
            return new MockTask(s);
        }
    }

    class ThreadedServer implements Runnable {
        public void run() {
            try {
                ServerSocket server = new ServerSocket(8855);
                new TestRequestHandler().fulfillRequests(server);
                server.close();
            } catch (Exception e) {}
        }
    }

    PrintStream stndOut;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void initializer() {
        stndOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(stndOut);
    }

    @Test
    public void handleRequestsWithoutGettingStuck() {
        (new Thread(new ThreadedServer())).start();

        try {
            Socket test = new Socket("localhost", 8855);
            test.getOutputStream().write("Test".getBytes());
        }
        catch (Exception e) { assertTrue(false); }
    }
}