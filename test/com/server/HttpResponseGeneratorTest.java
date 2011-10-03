package com.server;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HttpResponseGeneratorTest {
    class MockResponseGenerator extends HttpResponseGenerator {
        public MockResponseGenerator(Map<String, String> r) {
            super(r);
        }

        public File getFile(String relativePath) {
            return new File("/Users/dcmoore/Projects/java-server/test/test_data" + "/public" + relativePath);
        }
    }

    ResponseGenerator generator1;
    PrintStream stndOut;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void initializer() {
        generator1 = new MockResponseGenerator(new HashMap<String, String>());
    }

    @After
    public void cleanUpStreams() {
        System.setOut(stndOut);
        stndOut = System.out;
        System.setOut(new PrintStream(outContent));
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

        generator1 = new MockResponseGenerator(request);

        assertEquals("GET /echo HTTP/1.1\r\nThis: is_a_test\r\nThis_is: also_a_test\r\n\r\n", new String(generator1.generate()));
    }

    @Test
    public void formRequestTest() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Method", "GET");
        request.put("Request-URI", "/form");
        request.put("HTTP-Version", "HTTP/1.1");
        request.put("This", "is_a_test");

        generator1 = new MockResponseGenerator(request);

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

        generator1 = new MockResponseGenerator(request);

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


        generator1 = new MockResponseGenerator(request);

        assertEquals(true, (new String(generator1.generate())).contains(
                "HTTP/1.1 200 OK\r\n"));
    }

    @Test
    public void fileServeRequest() {
        File file = new File("/Users/dcmoore/Projects/java-server/test/test_data/public/test.html");
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

        generator1 = new MockResponseGenerator(request);

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
                    "<a href=\"/coverage\">coverage</a><br />\r\n" + //TODO - should be 'coverage/' instead of 'coverage'
                    "<a href=\"/dog.jpg\">dog.jpg</a><br />\r\n" +
                    "<a href=\"/test.html\">test.html</a><br />\r\n" +
                    "</body></html>",
                new String(generator1.generate()));
    }

    @Test
    public void embeddedDirectoryRequestTest() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Method", "GET");
        request.put("Request-URI", "/coverage/_files/");
        request.put("HTTP-Version", "HTTP/1.1");
        request.put("Body", "This: is_a_test");

        generator1 = new MockResponseGenerator(request);

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n<html><body>" +
                    "<a href=\"/coverage/_files/0.html\">0.html</a><br />\r\n" +
                    "<a href=\"/coverage/_files/1.html\">1.html</a><br />\r\n" +
                    "<a href=\"/coverage/_files/2.html\">2.html</a><br />\r\n" +
                    "<a href=\"/coverage/_files/3.html\">3.html</a><br />\r\n" +
                    "<a href=\"/coverage/_files/4.html\">4.html</a><br />\r\n" +
                    "</body></html>",
                new String(generator1.generate()));
    }

    @Test
    public void parseRoutes() {
        assertEquals("", "");
    }
}
