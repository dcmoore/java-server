package com.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class HttpRequestParser implements RequestParser {
    private InputStream input;
    private String[] requestHeaderFields = new String[] {
            "Accept",
            "Accept-Charset",
            "Accept-Encoding",
            "Accept-Language",
            "Authorization",
            "Cache-Control",
            "Connection",
            "Content-Type",
            "Content-Length",
            "Expect",
            "From",
            "Host",
            "If-Match",
            "If-Modified-Since",
            "If-None-Match",
            "If-Range",
            "If-Unmodified-Since",
            "Max-Forwards",
            "Origin",
            "Proxy-Authorization",
            "Range",
            "Referer",
            "TE",
            "User-Agent"
    };

    public HttpRequestParser(InputStream i) {
        input = i;
    }

    public Map<String, String> parse() throws ArrayIndexOutOfBoundsException {
        Map<String, String> formattedRequest = new HashMap<String, String>();

        String rawRequest = this.getRequest();
        String requestLine = rawRequest.split("\r\n")[0];
        String[] requestLineElements = requestLine.split(" ");

        formattedRequest.put("Method", requestLineElements[0]);
        formattedRequest.put("Request-URI", requestLineElements[1]);
        formattedRequest.put("HTTP-Version", requestLineElements[2]);

        for(String requestHeaderField : requestHeaderFields) {
            if(rawRequest.contains(requestHeaderField)) {
                formattedRequest.put(requestHeaderField, rawRequest.split("\r\n" + requestHeaderField + ": ")[1].split("\r\n")[0]);
            }
        }

        if(formattedRequest.get("Method").equals("POST")) {
            String rawPostData = rawRequest.split("\r\n\r\n")[1];
            String[] postDataArray = rawPostData.split("&");

            for(String post : postDataArray) {
                String[] postDataPair = post.split("=");
                formattedRequest.put("Post-" + postDataPair[0], postDataPair[1]);
            }
        }

        return formattedRequest;
    }

    public String getRequest() {
        String requestStringVersion = "";
        BufferedReader request = new BufferedReader
                  (new InputStreamReader(input));

        try {
            while(request.ready()) {            //TODO - request variable is never ready in Chrome on the first request after a navigate backwards
                int nextChar = request.read();
                requestStringVersion += (char)nextChar;
            }
        } catch (Exception e) {
            System.out.println("Failed to read request: " + e.getMessage());
        }

        return requestStringVersion;
    }
}