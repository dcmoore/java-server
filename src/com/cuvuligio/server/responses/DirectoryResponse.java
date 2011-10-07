package com.cuvuligio.server.responses;

import com.cuvuligio.server.ServerResponse;

import java.io.File;
import java.util.Map;


public class DirectoryResponse extends ServerResponse {
    public byte[] get(Map<String, String> request) {
        String output = getResponseMessage(200) + "<html><body>";
        File directory = getFile(request.get("Request-URI"));

        for(String fileName : directory.list()) {
            output += "<a href=\"http://" + request.get("Address") + ":" + request.get("Port") + request.get("Request-URI") + "/" + fileName + "\">" + fileName + "</a><br />\r\n";
        }

        output += "</body></html>";
        return output.getBytes();
    }

    public File getFile(String relativePath) {
        return new File(System.getProperty("user.dir") + relativePath);
    }
}
