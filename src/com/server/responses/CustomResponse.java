package com.server.responses;
import com.server.HttpResponseGenerator;

import java.util.Map;

public class CustomResponse extends HttpResponseGenerator {
    public CustomResponse(Map<String, String> r) {
        super(r);
    }

    public byte[] get(String relativeFilePath) {
        System.out.println(relativeFilePath);
        return new byte[0];
    }
}
