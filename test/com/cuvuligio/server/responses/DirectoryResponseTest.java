package com.cuvuligio.server.responses;


import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DirectoryResponseTest {
    class TestDirectoryResponse extends DirectoryResponse {
        public File getFile(String relativePath) {
            return new File(System.getProperty("user.dir") + "/test/test_data" + relativePath);
        }
    }

    @Test
    public void directoryResponseTest() {
        Map<String, String> testRequest = new HashMap<String, String>();
        testRequest.put("Method", "GET");
        testRequest.put("Request-URI", "/");
        testRequest.put("HTTP-Version", "HTTP/1.1");

        assertEquals("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n" +
                    "<html><body><a href=\"http://null:null//.DS_Store\">.DS_Store</a><br />\r\n" +
                    "<a href=\"http://null:null//directory\">directory</a><br />\r\n" +
                    "<a href=\"http://null:null//dog.jpg\">dog.jpg</a><br />\r\n" +
                    "<a href=\"http://null:null//srp.pdf\">srp.pdf</a><br />\r\n" +
                    "<a href=\"http://null:null//test_file.html\">test_file.html</a><br />\r\n" +
                    "</body></html>",
                new String(new TestDirectoryResponse().get(testRequest)));
    }
}