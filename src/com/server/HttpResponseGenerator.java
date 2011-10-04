package com.server;


import com.server.responses.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseGenerator implements ResponseGenerator {
    protected Map<String, String> request;
    protected Map<String, String> routes;

    public HttpResponseGenerator(Map<String, String> r) {
        request = r;
    }

    public Map<String, String> getRoutes() {
        return routes;
    }

    public String getStatusLine(int statusCode) {
        Map<Integer, String> statusLines = new HashMap<Integer, String>();

        statusLines.put(100, "100 Continue");
        statusLines.put(101, "101 Switching Protocols");
        statusLines.put(200, "200 OK");
        statusLines.put(201, "201 Created");
        statusLines.put(202, "202 Accepted");
        statusLines.put(203, "203 Non-Authoritative Information");
        statusLines.put(204, "204 No Content");
        statusLines.put(205, "205 Reset Content");
        statusLines.put(206, "206 Partial Content");
        statusLines.put(300, "300 Multiple Choices");
        statusLines.put(301, "301 Moved Permanently");
        statusLines.put(302, "302 Found");
        statusLines.put(303, "303 See Other");
        statusLines.put(304, "304 Not Modified");
        statusLines.put(305, "305 Use Proxy");
        statusLines.put(307, "307 Temporary Redirect");
        statusLines.put(400, "400 Bad Request");
        statusLines.put(401, "401 Unauthorized");
        statusLines.put(402, "402 Payment Required");
        statusLines.put(403, "403 Forbidden");
        statusLines.put(404, "404 Not Found");
        statusLines.put(405, "405 Method Not Allowed");
        statusLines.put(406, "406 Not Acceptable");
        statusLines.put(407, "407 Proxy Authentication Required");
        statusLines.put(408, "408 Request Time-Out");
        statusLines.put(409, "409 Conflict");
        statusLines.put(410, "410 Gone");
        statusLines.put(411, "411 Length Required");
        statusLines.put(412, "412 Precondition Failed");
        statusLines.put(413, "413 Request Entity Too Large");
        statusLines.put(414, "414 Request-URI Too Large");
        statusLines.put(415, "415 Unsupported Media Type");
        statusLines.put(416, "416 Requested Range Not Satisfiable");
        statusLines.put(417, "417 Expectation Failed");
        statusLines.put(500, "500 Internal Server Error");
        statusLines.put(501, "501 Not Implemented");
        statusLines.put(502, "502 Bad Gateway");
        statusLines.put(503, "503 Service Unavailable");
        statusLines.put(504, "504 Gateway Time-Out");
        statusLines.put(505, "505 HTTP Version Not Supported");

        return "HTTP/1.1 " + statusLines.get(statusCode) + "\r\n";
    }

    public byte[] generate() {
        parseRoutes();

        if(isCustomRoute()) {
            return new CustomResponse(request).get();
        }
        else if(request.get("Request-URI").equals("/echo")) {
            return new EchoResponse(request).get();
        }
        else if(request.get("Method").equals("GET") && request.get("Request-URI").equals("/form")) {
            return new FormResponse(request).get();
        }
        else if(request.get("Method").equals("POST") && request.get("Request-URI").equals("/formData")) {
            return new FormDataResponse(request).post();
        }
        else if(request.get("Method").equals("GET") && request.get("Request-URI").equals("/time")) {
            return new TimeStampResponse(request).get();
        }
        else {
            File file = getFile(request.get("Request-URI"));

            if (file.isDirectory()) {
                String[] children = file.list();
                return new DirectoryResponse(request).get(children);
            }
            else if (file.isFile()) {
                return new FileResponse(request).get(file);
            }
            else {
                return new FileNotFoundResponse(request).get();
            }
        }
    }

    public void parseRoutes() {
        String routesString = readInRoutes();
        routes = new HashMap<String, String>();
        String[] allRoutes = routesString.split("\n");

        for(String route : allRoutes) {
            routes.put(route.split("\\|")[0].trim(), route.split("\\|")[1].trim());
        }
    }

    public String readInRoutes() {
        File routesFile = getFile("/../config/routes.txt");
        String routesString = "";

        try {
            BufferedReader routeReader = new BufferedReader(new FileReader(routesFile));

            while(routeReader.ready()) {
                int nextChar = routeReader.read();
                routesString += (char)nextChar;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return routesString;
    }

    public boolean isCustomRoute() {
        if(routes.isEmpty()) {
            return false;
        }
        else if(routes.containsKey(request.get("Request-URI"))) {
            return true;
        }

        return false;
    }

    public File getFile(String relativePath) {
        return new File(System.getProperty("user.dir") + "/public" + relativePath);
    }
}