package com.cuvuligio.server;


import java.util.Map;

public interface ServerResponse {
    public byte[] get(Map<String, String> request);
}
