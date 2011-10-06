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

        public TestTask(Socket s, Map<String, ServerResponse> r) {
            super(s, r);
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream("GET /test HTTP/1.1\r\nOrigin: TaskTest.java\r\n\r\n".getBytes());
        }

        public OutputStream getOutputStream() throws IOException {
            output.write("test".getBytes());
            return output;
        }
    }

    class TestResponse implements ServerResponse {
        public byte[] get() {
            return "test".getBytes();
        }
    }

    @Test
    public void parseAndRespondToRequests() {
        Map<String, ServerResponse> routes = new HashMap<String, ServerResponse>();
        routes.put("/test", new TestResponse());
        TestTask task1 = new TestTask(new Socket(), routes);
        task1.run();

        try {
            assertEquals("test", task1.getOutputStream().toString());
        } catch (Exception e) {}
    }
}
