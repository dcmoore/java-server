package com.cuvuligio.server.responses;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class FileNotFoundResponseTest {
    @Test
    public void fileNotFoundResponseTest() {
        Map<String, String> testRequest = new HashMap<String, String>();
        testRequest.put("Method", "GET");
        testRequest.put("Request-URI", "/invalid");
        testRequest.put("HTTP-Version", "HTTP/1.1");

        assertEquals("HTTP/1.1 404 Not Found\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n<html><body><h1>File Not Found</h1></body></html>",
                new String(new FileNotFoundResponse().get(testRequest)));
    }
}
