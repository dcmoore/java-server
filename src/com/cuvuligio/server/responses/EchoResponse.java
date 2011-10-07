package com.cuvuligio.server.responses;

import com.cuvuligio.server.ServerResponse;
import java.util.Iterator;
import java.util.Map;


public class EchoResponse extends ServerResponse {
    public byte[] get(Map<String, String> request) {
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

        return (getResponseMessage(200) + response).getBytes();
    }

    private boolean notHeader(Object key) {
        if(key.equals("Method") || key.equals("Request-URI") || key.equals("HTTP-Version")) {
            return false;
        }

        return true;
    }
}
