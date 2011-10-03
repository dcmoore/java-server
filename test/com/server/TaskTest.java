package com.server;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TaskTest {
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