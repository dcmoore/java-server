package com.server.responses;
import com.server.HttpResponseGenerator;

import java.util.Map;

public class CustomResponse extends HttpResponseGenerator {
    public CustomResponse(Map<String, String> r) {
        super(r);
    }

    public byte[] get() {
        return new byte[0];
    }
}
