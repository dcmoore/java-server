package com.cuvuligio.server.responses;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TimeResponseTest {
    @Test
    public void timeStampRequestTest() {
        Map<String, String> testRequest = new HashMap<String, String>();
        assertTrue(new String(new TimeResponse().get(testRequest)).contains("HTTP/1.1 200 OK\r\n"));
    }
}
