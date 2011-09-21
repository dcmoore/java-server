package com.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpRequestHandlerTest {
    class TestServer implements Runnable {
        public void run() {
            RequestHandler testHandler = new HttpRequestHandler();
            testHandler.setNumRequests(1);
            Server.startServer(8802, testHandler);
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
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(outContent.toString(), "Starting the server on port: 8802\nWaiting for a request...\nClosed the server on port: 8802\nThread 0 is starting...\nThread 0 is done.\n");
    }
}
