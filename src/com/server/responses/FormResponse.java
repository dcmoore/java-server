package com.server.responses;


import com.server.HttpResponseGenerator;
import java.util.Map;

public class FormResponse extends HttpResponseGenerator {
    public FormResponse(Map<String, String> r) {
        super(r);
    }

    public byte[] get() {
        return (getStatusLine(200) + "Content-Type: text/html; charset=UTF-8\r\n\r\n<html><body><form name=\"input\" action=\"/formData\" method=\"post\"><input type=\"text\" name=\"text1\" /><input type=\"text\" name=\"text2\" /><input type=\"submit\" value=\"Submit\" /></form></body></html>").getBytes();
    }
}