package com.server;


import java.io.File;

public interface ResponseGenerator {
    public String getStatusLine(int statusCode);
    public byte[] generate();
    public File getFile(String relativePath);
}
