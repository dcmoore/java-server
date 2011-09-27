package com.server;


import java.io.File;

public interface ResponseGenerator {
    public String getStatusLine(int statusCode);
    public byte[] generate();

    public byte[] echoResponse();
    public byte[] formResponse();
    public byte[] formDataResponse();
    public byte[] timeStampResponse();
    public File getFile(String relativePath);
    public byte[] fileResponse(File file);
    public byte[] fileNotFoundResponse();
    public byte[] directoryResponse(String[] children);
}
