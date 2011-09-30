package com.server;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.ServerSocket;

public class ServerTest {
	private class MockHttpRequestHandler implements RequestHandler {
        private long numReqs;
        public void setNumRequests(long x){ numReqs = x; }

        public boolean fulfillRequests(ServerSocket server) {
            return true;
        }
    }

    @Test
	public void opensASocket() {
        RequestHandler mockHandler = new MockHttpRequestHandler();
        assertTrue(Server.runServer(8801, mockHandler));
	}

    private class MockHttpRequestHandler2 implements RequestHandler {
        private long numReqs;
        public void setNumRequests(long x){ numReqs = x; }


        public boolean fulfillRequests(ServerSocket server) {
            double x = 1/0;
            return true;
        }
    }

    @Test
	public void failsToOpenASocket() {
        RequestHandler mockHandler = new MockHttpRequestHandler2();
        assertFalse(Server.runServer(8801, mockHandler));
	}
}