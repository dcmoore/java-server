package com.server;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.ServerSocket;

public class ServerTest {
	@Test
	public void opensASocket() {
        RequestHandler mockHandler = new MockHttpRequestHandler();
        assertTrue(Server.runServer(8801, mockHandler));
	}

    private class MockHttpRequestHandler implements RequestHandler {
        public void setNumRequests(long x){}
        public long getNumRequests() {return (long)1;}

        public boolean fulfillRequests(ServerSocket server) {
            return true;
        }
    }
}