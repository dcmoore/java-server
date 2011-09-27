package com.server;

import org.junit.Test;
import java.io.*;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HttpRequestParserTest {
    String fakePostRequest = "POST /formData HTTP/1.1\n" +
        "Host: localhost:8765\n" +
        "Connection: keep-alive\n" +
        "Content-Length: 19\n" +
        "Cache-Control: max-age=0\n" +
        "Origin: http://localhost:8765\n" +
        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.186 Safari/535.1\n" +
        "Content-Type: application/x-www-form-urlencoded\n" +
        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
        "Referer: http://localhost:8765/form\n" +
        "Accept-Encoding: gzip,deflate,sdch\n" +
        "Accept-Language: en-US,en;q=0.8\n" +
        "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.3\n" +
        "\n" +
        "text1=abc&text2=123&Referer: =hacker";

    @Test
    public void getRequestTest() {
        ByteArrayInputStream input1 = new ByteArrayInputStream("Test request".getBytes());
        HttpRequestParser parser1 = new HttpRequestParser(input1);

        assertEquals("Test request", parser1.getRequest());

        ByteArrayInputStream input2 = new ByteArrayInputStream(fakePostRequest.getBytes());
        HttpRequestParser parser2 = new HttpRequestParser(input2);

        assertEquals(fakePostRequest, parser2.getRequest());
    }

    @Test
    public void parseTest() {
        ByteArrayInputStream input1 = new ByteArrayInputStream("Garbage request data".getBytes());
        HttpRequestParser parser1 = new HttpRequestParser(input1);

        assertEquals("Garbage", parser1.parse().get("Method"));

        ByteArrayInputStream input2 = new ByteArrayInputStream(fakePostRequest.getBytes());
        HttpRequestParser parser2 = new HttpRequestParser(input2);
        Map<String, String> formattedReq = parser2.parse();

        assertEquals("19", formattedReq.get("Content-Length"));
        assertEquals(null, formattedReq.get("Invalid"));
        assertEquals("http://localhost:8765/form", formattedReq.get("Referer"));
        assertEquals("123", formattedReq.get("Post-text2"));
    }
}
