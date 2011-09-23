package com.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.net.Socket;

public class HttpRequestHandlerTest {
    class TestServer implements Runnable {
        public boolean success;

        public void run() {
            RequestHandler testHandler = new TestHttpRequestHandler();
            testHandler.setNumRequests(3);
            success = Server.runServer(8802, testHandler);
        }
    }

    class TestHttpRequestHandler extends HttpRequestHandler {
        public Runnable createTask(Socket s, String name) {
            return new MockTask();
        }
    }

    class MockTask implements Runnable {
        public void run() {
            System.out.println("Test Thread Start");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Test Thread Start");
        }
    }

    BufferedReader input;
    PrintWriter output;
    TestServer serv;

    @Before
    public void initialize() {
        serv = new TestServer();
        new Thread(serv).start();

        try {
            Socket s = new Socket("localhost", 8802);
            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            output = new PrintWriter(s.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void acceptsRequests() {
        output.println("Test html request1");
        output.flush();
        output.println("Test html request2");
        output.flush();
        output.println("Test html request3");
        output.flush();

        //This is needed to make sure that the thread finishes before we reset System.out
        try {
            Thread.sleep(100);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assert(serv.success);
    }
}