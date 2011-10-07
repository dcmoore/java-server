package com.cuvuligio.server;


import com.cuvuligio.server.responses.*;

import java.io.File;
import java.util.Map;

public class HttpResponseGenerator {
    Map<String, String> request;
    Map<String, ServerResponse> routes;

    public HttpResponseGenerator(Map<String, String> requestMap, Map<String, ServerResponse> routeMap) {
        request = requestMap;
        routes = routeMap;
    }

    public byte[] generate() {
        if(routes.containsKey(request.get("Request-URI"))) {
            ServerResponse response = routes.get(request.get("Request-URI"));
            return response.get(request);
        }

        File file = getFile(request.get("Request-URI"));

        if (file.isDirectory()) return this.getDirectoryResponse();
        if (file.isFile()) return this.getFileResponse();
        return this.get404Response();
    }

    public File getFile(String relativePath) {
        return new File(System.getProperty("user.dir") + relativePath);
    }

    public byte[] getDirectoryResponse() {
        return new DirectoryResponse().get(request);
    }

    public byte[] getFileResponse() {
        return new FileResponse().get(request);
    }

    public byte[] get404Response() {
        if(routes.get("404") != null)
            return routes.get("404").get(request);
        return new FileNotFoundResponse().get(request);
    }
}
