package com.server.responses;


import com.server.HttpResponseGenerator;

import java.io.File;
import java.util.Map;


public class DirectoryResponse extends HttpResponseGenerator {
    public DirectoryResponse(Map<String, String> r) {
        super(r);
    }

    public byte[] get(String[] children) {
        String output = getStatusLine(200) + "Content-Type: text/html; charset=UTF-8\r\n\r\n<html><body>";

        for(String fileName : children) {
            File file = getThisFile(request.get("Request-URI") + fileName);
            if(file.list() == null) {
                output += "<a href=\"http://localhost:8765" + request.get("Request-URI") + fileName + "\">" + fileName + "</a><br />\r\n";
            }
            else {
                output += "<a href=\"http://localhost:8765" + request.get("Request-URI") + fileName + "/\">" + fileName + "</a><br />\r\n";
            }
        }

        output += "</body></html>";
        return output.getBytes();
    }

    public File getThisFile(String relativePath) {
        return new File(System.getProperty("user.dir") + relativePath);
    }
}
