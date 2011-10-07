package com.cuvuligio.server.responses;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class EchoResponseTest {
    @Test
    public void echoResponseTest() {
        Map<String, String> testRequest1 = new HashMap<String, String>();
        testRequest1.put("Method", "GET");
        testRequest1.put("Request-URI", "/");
        testRequest1.put("HTTP-Version", "HTTP/1.1");
        testRequest1.put("Get", "Some");

        Map<String, String> testRequest2 = new HashMap<String, String>();
        testRequest2.put("Method", "POST");
        testRequest2.put("Request-URI", "/");
        testRequest2.put("HTTP-Version", "HTTP/1.1");
        testRequest2.put("Word", "To Your Mother");

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n\r\nGET / HTTP/1.1\r\nGet: Some\r\n",
                new String(new EchoResponse().get(testRequest1)));
        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n\r\nPOST / HTTP/1.1\r\nWord: To Your Mother\r\n",
                new String(new EchoResponse().get(testRequest2)));
    }
}
