package com.server;


import javax.crypto.Mac;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseGenerator implements ResponseGenerator {
    Map<String, String> request;

    public HttpResponseGenerator(Map<String, String> r) {
        request = r;
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

        return "HTTP/1.1 " + statusLines.get(statusCode) + "\n";
    }

    public byte[] generate() {
        if(request.get("Request-URI").equals("/echo")) {
              return echoResponse();
        }
        else if(request.get("Method").equals("GET") && request.get("Request-URI").equals("/form")) {
            return formResponse();
        }
        else if(request.get("Method").equals("POST") && request.get("Request-URI").equals("/formData")) {
            return formDataResponse();
        }
        else if(request.get("Method").equals("GET") && request.get("Request-URI").equals("/time")) {
            return timeStampResponse();
        }
        else {
            File file = getFile(request.get("Request-URI"));
            String[] children = file.list();

            if (children == null) {
                if(file.exists()) {
                    return fileResponse(file);
                }
                else {
                    return fileNotFoundResponse();
                }
            }
            else {
                return directoryResponse(children);
            }
        }
    }

    public byte[] echoResponse() {
        return (request.get("Method") + " " +
                request.get("Request-URI") + " " +
                request.get("HTTP-Version") + "\n" +
                request.get("Body") + "\n").getBytes();
    }

    public byte[] formResponse() {
        return (getStatusLine(200) + "Content-Type: text/html; charset=UTF-8\n\n<html><body><form name=\"input\" action=\"/formData\" method=\"post\"><input type=\"text\" name=\"text1\" /><input type=\"text\" name=\"text2\" /><input type=\"submit\" value=\"Submit\" /></form></body></html>").getBytes();
    }

    public byte[] formDataResponse() {
        return (getStatusLine(200) +
                "Content-Type: text/html; charset=UTF-8\n" +
                "\n" +
                "<html><body><p>" + request.get("Post-text1") +
                "</p><p>" + request.get("Post-text2") +
                "</p></body></html>").getBytes();
    }

    public byte[] timeStampResponse() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        for(long i = (7*Integer.MIN_VALUE); i < Integer.MAX_VALUE; i++) {}

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return (getStatusLine(200) + "Content-Type: text/html; charset=UTF-8\n\n" + dateFormat.format(date)).getBytes();
    }

    public File getFile(String relativePath) {
        return new File(System.getProperty("user.dir") + relativePath);
    }

    public byte[] fileResponse(File file) {
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

    public byte[] fileNotFoundResponse() {
        return (getStatusLine(404) + "Content-Type: text/html; charset=UTF-8\n\n" +
                "<html><body><h1>404 File Not Found</h1><p>Please try another file</p><body></html>").getBytes();
    }

    public byte[] directoryResponse(String[] children) {
        String output = getStatusLine(200) + "Content-Type: text/html; charset=UTF-8\n\n<html><body>";

        for(String fileName : children) {
            File file = getFile(request.get("Request-URI") + fileName);
            if(file.list() == null) {
                output += "<a href=\"http://localhost:8765" + request.get("Request-URI") + fileName + "\">" + fileName + "</a><br />\n";
            }
            else {
                output += "<a href=\"http://localhost:8765" + request.get("Request-URI") + fileName + "/\">" + fileName + "</a><br />\n";
            }
        }

        output += "</body></html>";
        return output.getBytes();
    }
}