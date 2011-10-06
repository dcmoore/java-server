package com.cuvuligio.server;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseGeneratorTest {
    class MockResponseGenerator extends HttpResponseGenerator {
        public MockResponseGenerator(Map<String, String> requestMap, Map<String, ServerResponse> routeMap) {
            super(requestMap, routeMap);
        }

        public File getFile(String relativePath) {
            return new File(System.getProperty("user.dir") + "/test/test_data" + relativePath);
        }
    }

    MockResponseGenerator generator1;

    @Before
    public void initializer() {
        generator1 = new MockResponseGenerator(new HashMap<String, String>(), new HashMap<String, ServerResponse>());
    }

    @Test
    public void getStatusLine() {
        assertEquals("HTTP/1.1 404 Not Found\r\n", generator1.getStatusLine(404));
        assertEquals("HTTP/1.1 200 OK\r\n", generator1.getStatusLine(200));
        assertEquals("HTTP/1.1 415 Unsupported Media Type\r\n", generator1.getStatusLine(415));
    }

    @Test
    public void getsMyFile() {
        File test = generator1.getFile("/dog.jpg");
    }
}
