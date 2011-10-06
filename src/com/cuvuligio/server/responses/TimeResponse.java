package com.cuvuligio.server.responses;

import com.cuvuligio.server.ServerResponse;

import java.util.Map;


public class TimeResponse implements ServerResponse {
    public byte[] get(Map<String, String> request) {
        return "".getBytes();
    }
}
