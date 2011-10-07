package com.cuvuligio.server;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

public class ServerResponseTest {
    @Test
    public void getStatusLine() {
        ServerResponse generator1 = new ServerResponse() {
            @Override
            public byte[] get(Map<String, String> request) {
                return new byte[0];
            }
        };

        assertEquals("HTTP/1.1 404 Not Found\r\n", generator1.getStatusLine(404));
        assertEquals("HTTP/1.1 200 OK\r\n", generator1.getStatusLine(200));
        assertEquals("HTTP/1.1 415 Unsupported Media Type\r\n", generator1.getStatusLine(415));
        assertEquals("Content-Type: text/html; charset=UTF-8\r\n\r\n", generator1.getHeaders());
        assertEquals("HTTP/1.1 201 Created\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n", generator1.getResponseMessage(201));
    }
}
