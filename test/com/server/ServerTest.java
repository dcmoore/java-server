package com.server;

import org.junit.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;

import static org.junit.Assert.*;

public class ServerTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setTestStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

    }

    @After
    public void deleteTestStreams() {
        System.setOut(null);
        System.setErr(null);
    }

	@Test
	public void opensASocket() {
        RequestHandler mockHandler = new MockHttpRequestHandler();
        assertTrue(Server.startServer(8801, mockHandler));
	}

    private class MockHttpRequestHandler implements RequestHandler {
        public boolean fulfillRequests(ServerSocket server) {
            return true;
        }
    }
}