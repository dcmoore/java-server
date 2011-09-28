package com.server.responses;


import com.server.HttpResponseGenerator;

import java.util.Iterator;
import java.util.Map;

public class EchoResponse extends HttpResponseGenerator {
    public EchoResponse(Map<String, String> r) {
        super(r);
    }

    public byte[] get() {
        String response = request.get("Method") + " " +
        request.get("Request-URI") + " " +
        request.get("HTTP-Version") + "\r\n";

        Iterator iterator = request.keySet().iterator();
        while(iterator. hasNext()){
            Object currentKey = iterator.next();
            if(notHeader(currentKey)) {
                response += currentKey + ": " + request.get(currentKey) + "\r\n";
            }
        }

        return (response + "\r\n").getBytes();
    }

    private boolean notHeader(Object key) {
        if(key.equals("Method") || key.equals("Request-URI") || key.equals("HTTP-Version")) {
            return false;
        }

        return true;
    }
}
