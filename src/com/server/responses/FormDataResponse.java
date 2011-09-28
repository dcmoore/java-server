package com.server.responses;


import com.server.HttpResponseGenerator;
import java.util.Map;

public class FormDataResponse extends HttpResponseGenerator {
    public FormDataResponse(Map<String, String> r) {
        super(r);
    }

    public byte[] post() {
        return (getStatusLine(200) +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "\r\n" +
                "<html><body><p>" + request.get("Post-text1") +
                "</p><p>" + request.get("Post-text2") +
                "</p></body></html>").getBytes();
    }
}