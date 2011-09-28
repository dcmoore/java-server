package com.server.responses;


import com.server.HttpResponseGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;


public class FileResponse extends HttpResponseGenerator {
    public FileResponse(Map<String, String> r) {
        super(r);
    }

    public byte[] get(File file) {
        byte[] bytes = new byte[]{};

        try {
            FileInputStream inputStream = new FileInputStream(file);
            bytes = new byte[(int) file.length()];
            inputStream.read(bytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return bytes;
    }
}