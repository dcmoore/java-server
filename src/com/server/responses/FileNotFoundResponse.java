package com.server.responses;


import com.server.HttpResponseGenerator;
import java.util.Map;


public class FileNotFoundResponse extends HttpResponseGenerator {
    public FileNotFoundResponse(Map<String, String> r) {
        super(r);
    }

    public byte[] get() {
        return (getStatusLine(404) + "Content-Type: text/html; charset=UTF-8\r\n\r\n" +
                "<html><body><h1>404 File Not Found</h1><p>Please try another file</p><body></html>").getBytes();
    }
}