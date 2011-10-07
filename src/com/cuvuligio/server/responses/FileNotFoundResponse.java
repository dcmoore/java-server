package com.cuvuligio.server.responses;

import com.cuvuligio.server.ServerResponse;

import java.util.Map;


public class FileNotFoundResponse extends ServerResponse {
    public byte[] get(Map<String, String> request) {
        return (getResponseMessage(404) + "<html><body><h1>File Not Found</h1></body></html>").getBytes();
    }
}
