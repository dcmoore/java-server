package com.cuvuligio.server;


import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TaskTest {
    class TestTask extends Task {
        public ByteArrayOutputStream output = new ByteArrayOutputStream();
        private int requestNumber = 0;

        public TestTask(Socket s, Map<String, ServerResponse> r) {
            super(s, r);
        }

        public InputStream getInputStream() throws IOException {
            if(requestNumber == 0) {
                requestNumber++;
                return new ByteArrayInputStream("GET /test1 HTTP/1.1\r\nOrigin: TaskTest.java\r\n\r\n".getBytes());
            }

            return new ByteArrayInputStream("GET /test2 HTTP/1.1\r\nOrigin: TaskTest.java\r\n\r\n".getBytes());
        }

        private void resetOutputStream() {
            output = new ByteArrayOutputStream();
        }

        public OutputStream getOutputStream() throws IOException {
            return output;
        }
    }

    class TestResponse1 implements ServerResponse {
        public byte[] get(Map<String, String> request) {
            return "test1".getBytes();
        }
    }

    class TestResponse2 implements ServerResponse {
        public byte[] get(Map<String, String> request) {
            return "test2".getBytes();
        }
    }

    @Test
    public void parseAndRespondToRequests() {
        Map<String, ServerResponse> routes = new HashMap<String, ServerResponse>();
        routes.put("/test1", new TestResponse1());
        routes.put("/test2", new TestResponse2());
        TestTask task1 = new TestTask(new Socket(), routes);

        try {
            task1.run();
            assertEquals("test1", task1.getOutputStream().toString());

            task1.resetOutputStream();
            task1.run();
            assertEquals("test2", task1.getOutputStream().toString());
        } catch (Exception e) {}
    }
}
