package com.server;


import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HttpResponseGeneratorTest {
    ResponseGenerator generator1;

    @Before
    public void initializer() {
        generator1 = new HttpResponseGenerator(new HashMap<String, String>());
    }

    @Test
    public void getStatusLine() {
        assertEquals("HTTP/1.1 404 Not Found\r\n", generator1.getStatusLine(404));
        assertEquals("HTTP/1.1 200 OK\r\n", generator1.getStatusLine(200));
        assertEquals("HTTP/1.1 415 Unsupported Media Type\r\n", generator1.getStatusLine(415));
    }

    @Test
    public void echoRequestTest() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Method", "GET");
        request.put("Request-URI", "/echo");
        request.put("HTTP-Version", "HTTP/1.1");
        request.put("This_is", "also_a_test");
        request.put("This", "is_a_test");

        generator1 = new HttpResponseGenerator(request);

        assertEquals("GET /echo HTTP/1.1\r\nThis: is_a_test\r\nThis_is: also_a_test\r\n\r\n", new String(generator1.generate()));
    }

    @Test
    public void formRequestTest() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Method", "GET");
        request.put("Request-URI", "/form");
        request.put("HTTP-Version", "HTTP/1.1");
        request.put("This", "is_a_test");

        generator1 = new HttpResponseGenerator(request);

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n<html><body><form name=\"input\" action=\"/formData\" method=\"post\"><input type=\"text\" name=\"text1\" /><input type=\"text\" name=\"text2\" /><input type=\"submit\" value=\"Submit\" /></form></body></html>",
                new String(generator1.generate()));
    }

    @Test
    public void formDataRequestTest() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Method", "POST");
        request.put("Request-URI", "/formData");
        request.put("HTTP-Version", "HTTP/1.1");
        request.put("This", "is_a_test");
        request.put("Post-text1", "abc");
        request.put("Post-text2", "123");

        generator1 = new HttpResponseGenerator(request);

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n<html><body><p>abc</p><p>123</p></body></html>",
                new String(generator1.generate()));
    }

    @Test
    public void timeStampRequestTest() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Method", "GET");
        request.put("Request-URI", "/time");
        request.put("HTTP-Version", "HTTP/1.1");
        request.put("Body", "This: is_a_test");


        generator1 = new HttpResponseGenerator(request);

        assertEquals(true, (new String(generator1.generate())).contains(
                "HTTP/1.1 200 OK\r\n"));
    }

    class MockResponseGenerator extends HttpResponseGenerator {
        public MockResponseGenerator(Map<String, String> r) {
            super(r);
        }

        public File getFile(String relativePath) {
            return new File("/Users/dcmoore/Projects/java-server/test/test_data/" + relativePath);
        }
    }

    @Test
    public void fileServeRequest() {
        File file = new File("/Users/dcmoore/Projects/java-server/test/test_data/test.html");
        byte[] bytes = new byte[]{};

        try {
            FileInputStream inputStream = new FileInputStream(file);
            bytes = new byte[(int) file.length()];
            inputStream.read(bytes);
        } catch (Exception e) {}

        Map<String, String> request = new HashMap<String, String>();
        request.put("Method", "GET");
        request.put("Request-URI", "/test.html");

        generator1 = new MockResponseGenerator(request);
        String a = new String(bytes);
        String b = new String(generator1.generate());

        assertEquals(true, a.equals(b));
    }

    @Test
    public void fileNotFoundTest() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Method", "GET");
        request.put("Request-URI", "/invalid");
        request.put("HTTP-Version", "HTTP/1.1");
        request.put("Body", "This: is_a_test");

        generator1 = new HttpResponseGenerator(request);

        assertEquals(true, (new String(generator1.generate())).contains(
                "HTTP/1.1 404 Not Found\r\n"));
    }

    @Test
    public void directoryRequestTest() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Method", "GET");
        request.put("Request-URI", "/");
        request.put("HTTP-Version", "HTTP/1.1");
        request.put("Body", "This: is_a_test");

        generator1 = new MockResponseGenerator(request);

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n<html><body>" +
                    "<a href=\"http://localhost:8765" + request.get("Request-URI") + "coverage/\">coverage</a><br />\r\n" +
                    "<a href=\"http://localhost:8765" + request.get("Request-URI") + "dog.jpg\">dog.jpg</a><br />\r\n" +
                    "<a href=\"http://localhost:8765" + request.get("Request-URI") + "test.html\">test.html</a><br />\r\n" +
                    "</body></html>",
                new String(generator1.generate()));
    }
}
