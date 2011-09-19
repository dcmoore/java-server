package com.server;

import org.junit.*;
import static com.server.Server.startServer;
import static org.junit.Assert.*;

public class ServerTest {
	@Test
	public void test_main() {
	   assertTrue(startServer() == true);
	}
}
