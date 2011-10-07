package com.cuvuligio.server.responses;


import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FileResponseTest {
    class TestFileResponse extends FileResponse {
        public File getFile(String relativePath) {
            return new File(System.getProperty("user.dir") + "/test/test_data" + relativePath);
        }
    }

    @Test
    public void getMyFile() {
        Map<String, String> testRequest = new HashMap<String, String>();
        testRequest.put("Method", "GET");
        testRequest.put("Request-URI", "/test_file.html");
        testRequest.put("HTTP-Version", "HTTP/1.1");

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n" +
                "<html>\n" +
                "<head>\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "  <meta http-equiv=\"Content-Style-Type\" content=\"text/css\">\n" +
                "  <title></title>\n" +
                "  <meta name=\"Generator\" content=\"Cocoa HTML Writer\">\n" +
                "  <meta name=\"CocoaVersion\" content=\"1038.36\">\n" +
                "  <style type=\"text/css\">\n" +
                "    p.p1 {margin: 0.0px 0.0px 0.0px 0.0px; font: 12.0px Helvetica}\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p class=\"p1\">This is a test html file. Check out this up and coming company. They make the best ham sandwiches around. Click <a href=\"http://www.google.com\">here</a>.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n",
                new String(new TestFileResponse().get(testRequest)));
    }
}
