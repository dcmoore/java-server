package com.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.net.Socket;

public class HttpRequestHandlerTest {
    class TestServer implements Runnable {
        public void run() {
            RequestHandler testHandler = new TestHttpRequestHandler();
            testHandler.setNumRequests(1);
            Server.runServer(8802, testHandler);
        }
    }

    class TestHttpRequestHandler extends HttpRequestHandler {
        public Runnable createTask(Socket s, String name) {
            return new MockTask();
        }
    }

    class MockTask implements Runnable {
        public void run() {
            System.out.println("Test Thread");
        }
    }

    BufferedReader input;
    PrintWriter output;
    PrintStream stndOut;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void initialize() {
        stndOut = System.out;
        System.setOut(new PrintStream(outContent));

        new Thread(new TestServer()).start();

        try {
            Socket s = new Socket("localhost", 8802);
            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            output = new PrintWriter(s.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    public void cleanUpStreams() {
        System.setOut(stndOut);
    }

    @Test
    public void acceptsRequests() {
        output.print("Test html request");

        //This is needed to make sure that the thread finishes before we reset System.out
        try {
            Thread.sleep(10);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(outContent.toString(),
            "Starting the server on port: 8802\n" +
            "Waiting for a request...\n" +
            "Test Thread\n" +
            "Closed the server on port: 8802\n");
    }

//    @Test
//    public void doesntCloseSocketUntilAllRequestsAreFinished() {
//        for(int i = 0; i < 60; i++) {
//            output.print("Test html request");
//
//            //This is needed to make sure that the thread finishes before we reset System.out
//            try {
//                Thread.sleep(10);
//            }
//            catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            assertEquals(outContent.toString(),
//                "Starting the server on port: 8802\n" +
//                "Waiting for a request...\n" +
//                "Test Thread\n" +
//                "Closed the server on port: 8802\n");
//        }
//    }

}