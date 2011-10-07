package com.cuvuligio.server.responses;

import com.cuvuligio.server.ServerResponse;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;


//TODO - get a proper response message prepended to the FileResponse
public class FileResponse extends ServerResponse {
    public byte[] get(Map<String, String> request) {
        File file = getFile(request.get("Request-URI"));
        byte[] fileBytes = new byte[]{};

        try {
            FileInputStream inputStream = new FileInputStream(file);
            fileBytes = new byte[(int) file.length()];
            inputStream.read(fileBytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return fileBytes;
    }

    public File getFile(String relativePath) {
        return new File(System.getProperty("user.dir") + relativePath);
    }
}
