package com.server;


import java.io.File;
import java.util.Map;

public interface ResponseGenerator {
    public Map<String, String> getRoutes();
    public String readInRoutes();
    public void parseRoutes();
    public boolean isCustomRoute();
    public String getStatusLine(int statusCode);
    public byte[] generate();
    public File getFile(String relativePath);
}
