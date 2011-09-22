package com.server;


import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class TaskTest {
    String testServerResponse = "";
    BufferedReader input;
    PrintWriter output;

    class TestServer implements Runnable {
        public void run() {
            try {
                ServerSocket server = new ServerSocket(8803);
                Socket connection = server.accept();

                Task testTask = new Task(connection, "TEST_TASK");
                testTask.run();

                testServerResponse = testTask.getResponse();

                server.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
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
    public void runMethodTest() {
        output.println("GET /echo HTTP/1.1");
        output.println("Host: localhost:8803\n");

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals("GET /echo HTTP/1.1\n", testServerResponse);
    }
}
