package com.server;


import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class TaskTest {
    Object testServerResponse = "";
    BufferedReader input;
    PrintWriter output;

    class TestServer implements Runnable {
        public void run() {
            try {
                ServerSocket server = new ServerSocket(8803);
                Socket connection = server.accept();

                Task testTask = new TestTask(connection, "TEST_TASK");
                testTask.run();

                testServerResponse = testTask.getResponse();

                server.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    class TestTask extends Task {
        public TestTask(Socket c, String n) {
            super(c, n);
        }

        public File getFilePath(String relativePath) {
            return new File("/Users/dcmoore/Projects/java-server/" + relativePath); //You must edit this to make a test pass on your system
        }
    }

    @Before
    public void initialize() {
        new Thread(new TestServer()).start();

        try {
            Thread.sleep(500);
            Socket s = new Socket("localhost", 8803);
            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            output = new PrintWriter(s.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void echoTest() {
        output.println("GET /echo HTTP/1.1");
        output.println("Host: localhost:8803\n");

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals("GET /echo HTTP/1.1\nHost: localhost:8803\n\n\n", testServerResponse);
    }

    @Test
    public void invalidRequestTest() {
        output.println("GET /invalid HTTP/1.1");
        output.println("Host: localhost:8803\n");

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals("HTTP/1.1 404 NF\nContent-Type: text/html; charset=UTF-8\n\n<html><body><h1>404 File not found</h1></body></html>", testServerResponse);
    }

    @Test
    public void formRequestTest() {
        output.println("GET /form HTTP/1.1");
        output.println("Host: localhost:8803\n");

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals("HTTP/1.1 200 OK\nContent-Type: text/html; charset=UTF-8\n\n<html><body><form name=\"input\" action=\"/formData\" method=\"post\"><input type=\"text\" name=\"text1\" /><input type=\"text\" name=\"text2\" /><input type=\"submit\" value=\"Submit\" /></form></body></html>", testServerResponse);
    }

    @Test
    public void formDataRequestTest() {
        output.println("POST /formData HTTP/1.1");
        output.println("Host: localhost:8803\n\n");
        output.println("text1=asdf&text2=zxcv\n");

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals("HTTP/1.1 200 OK\nContent-Type: text/html; charset=UTF-8\n\n<html><body><p>asdf</p><p>zxcv</p></body></html>", testServerResponse);
    }

    @Test
    public void timeStampTest() {
        output.println("GET /time HTTP/1.1");
        output.println("Host: localhost:8803\n");

        try {
            Thread.sleep(3200);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals("200", testServerResponse.toString().split(" ")[1]);
    }

    @Test
    public void directoryRenderTest() {
        output.println("GET /test/test_data HTTP/1.1");
        output.println("Host: localhost:8803\n");

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals("200", testServerResponse.toString().split(" ")[1]);
    }

    @Test
    public void htmlRenderTest() {
        output.println("GET /test/test_data/test.html HTTP/1.1");
        output.println("Host: localhost:8803\n");

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals("200", testServerResponse.toString().split(" ")[1]);
    }

//    @Test
//    public void imageRenderTest() {
//        output.println("GET /test/test_data/dog.jpg HTTP/1.1");
//        output.println("Host: localhost:8803\n");
//
//        try {
//            Thread.sleep(500);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        assertEquals("200", testServerResponse.toString().split(" ")[1]);
//    }

    @Test
    public void isImageTest() {
        Task testTask = new Task(new Socket(), "Test");
        assert(testTask.isImage("jpg"));
        assert(!(testTask.isImage("invalid")));
        testTask.closeConnection();
    }
}