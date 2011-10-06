package com.cuvuligio.server.responses;

import com.cuvuligio.server.ServerResponse;


public class EchoResponse implements ServerResponse {
    public byte[] get() {
        return "".getBytes();
    }
}
