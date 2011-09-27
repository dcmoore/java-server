package com.server;


import org.junit.Test;
import static org.junit.Assert.*;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TaskTest {
    @Test
    public void wontParseAnEmptySocket() {
        Task task1 = new Task(new Socket());
        task1.run();

        assertFalse(task1.errorFree);
    }

    class TestTask extends Task {
        public TestTask(Socket s) {
            super(s);
        }

        public Map<String, String> parseRequest() throws Exception {
            return new HashMap<String, String>();
        }

        public byte[] generateResponse(Map<String, String> parsedRequest) {
            return new byte[0];
        }

        public void sendResponse(byte[] response) throws Exception {
            //Do nothing
        }
    }

    @Test
    public void doesntThrowErrors() {
        TestTask task1 = new TestTask(new Socket());
        task1.run();
        assertTrue(task1.errorFree);
    }
}