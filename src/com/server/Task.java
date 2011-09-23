package com.server;


import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Task implements Runnable {
    private Socket connection;
    private String name;
    private Object response = "";

    public Object getResponse() {
        return response;
    }

    public Task(Socket c, String n) {
        connection = c;
        name = n;
    }

    public void run() {
        System.out.println("Thread " + name + " is starting...");

        sendResponse(parseRequest());

        this.closeConnection();
        System.out.println("Thread " + name + " is done.");
    }

    public Map<String, String> parseRequest() {
        Map<String, String> formattedRequest = new HashMap<String, String>();

        try {
            BufferedReader request = new BufferedReader
                  (new InputStreamReader(connection.getInputStream()));

            String line = request.readLine();

            formattedRequest.put("Method", line.split(" ")[0]);
            formattedRequest.put("Request-URI", line.split(" ")[1]);
            formattedRequest.put("HTTP-Version", line.split(" ")[2]);

            String body = "";
            while(!(line.equals(""))) {
                line = request.readLine();
                body += line + "\n";
            }

            if(formattedRequest.get("Method").equals("POST")) {
                int c;
                int contentLength = Integer.parseInt(body.split("Content-Length: ")[1].split("\n")[0]);

                for(int n = 0; n < contentLength; n++) {
                    c = request.read();
                    body += (char)c;
                }
            }

            formattedRequest.put("Body", body);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return formattedRequest;
    }

    public void sendResponse(Map<String, String> request) {
        if(request.get("Request-URI").equals("/echo")) {
              echoResponse(request);
        }
        else if(request.get("Method").equals("GET") && request.get("Request-URI").equals("/form")) {
            formResponse(request);
        }
        else if(request.get("Method").equals("POST") && request.get("Request-URI").equals("/formData")) {
            formDataResponse(request);
        }
        else if(request.get("Method").equals("GET") && request.get("Request-URI").equals("/time")) {
            timeStampResponse(request);
        }
        else {
            File file = getFilePath(request.get("Request-URI"));
            String[] children = file.list();

            if (children == null) {
                if(file.exists()) {
                    if(file.getName().split("\\.")[1].equals("html")) {
                        htmlResponse(request, file);
                    }
                    else if(isImage(file.getName().split("\\.")[1])) {
                        sendImageResponse(file);
                    }
                }
                else {
                    invalidRequestResponse(request);
                }
            }
            else {
                directoryResponse(request, children);
            }
        }
    }

    public File getFilePath(String relativePath) {
        return new File(System.getProperty("user.dir") + relativePath);
    }

    public boolean isImage(String fileExtension) {
        if(fileExtension.equals("jpg") || fileExtension.equals("png") || fileExtension.equals("jpeg") || fileExtension.equals("gif")) {
            return true;
        }

        return false;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendStringResponse() {
        try {
            PrintWriter sender = new PrintWriter
              (connection.getOutputStream(), true);

            sender.println(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendImageResponse(File file) {
        FileInputStream inputStream = null;
        OutputStream output = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            output = connection.getOutputStream();
            output.write(inputStream.read(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }


//        try {
//            FileInputStream fstream = new FileInputStream(System.getProperty("user.dir") + "/dog.jpg");
//            DataInputStream in = new DataInputStream(fstream);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            PrintStream writer = new PrintStream(connection.getOutputStream());
//            int x;
//            while ((x = reader.read()) != -1)
//                writer.write(x);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }





    public void echoResponse(Map<String, String> request) {
        response = request.get("Method") + " " +
                request.get("Request-URI") + " " +
                request.get("HTTP-Version") + "\n" +
                request.get("Body") + "\n";
        sendStringResponse();
    }

    public void formResponse(Map<String, String> request) {
        response =  "HTTP/1.1 200 OK\nContent-Type: text/html; charset=UTF-8\n\n<html><body><form name=\"input\" action=\"/formData\" method=\"post\"><input type=\"text\" name=\"text1\" /><input type=\"text\" name=\"text2\" /><input type=\"submit\" value=\"Submit\" /></form></body></html>";
        sendStringResponse();
    }

    public void formDataResponse(Map<String, String> request) {
        response = "HTTP/1.1 200 OK\n" +
                "Content-Type: text/html; charset=UTF-8\n" +
                "\n" +
                "<html><body><p>" + request.get("Body").split("text1=")[1].split("&")[0] +
                "</p><p>" + request.get("Body").split("text2=")[1] +
                "</p></body></html>";
        sendStringResponse();
    }

    public void timeStampResponse(Map<String, String> request) {
//        try {
//            Thread.sleep(5000);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

        for(long i = (7*Integer.MIN_VALUE); i < Integer.MAX_VALUE; i++) {}

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        response = "HTTP/1.1 200 OK\nContent-Type: text/html; charset=UTF-8\n\n" + dateFormat.format(date);
        sendStringResponse();
    }

    public void htmlResponse(Map<String, String> request, File file) {
        try {
            String fileString = readFile(file);
            response = "HTTP/1.1 200 OK\nContent-Type: text/html; charset=UTF-8\n\n" + fileString;
        }
        catch (Exception e) {
            response = "HTTP/1.1 500 OK\n\n<html><body>Error reading file</body></html>";
        }

        sendStringResponse();
    }

    public String readFile(File file) throws IOException {
        String stringFile = "";
        String line = "";
        BufferedReader reader = new BufferedReader(new FileReader(file));

        do {
            line = reader.readLine();
            if(line != null)
                stringFile += line;
        } while(line != null);

        return stringFile;
    }

    public void directoryResponse(Map<String, String> request, String[] children) {
        String output = "HTTP/1.1 200 OK\nContent-Type: text/html; charset=UTF-8\n\n<html><body>";

        for(String fileName : children) {
            File file = getFilePath(request.get("Request-URI") + fileName);
            String[] nextChildren = file.list();
            if(nextChildren == null) {
                output += "<a href=\"http://localhost:8765" + request.get("Request-URI") + fileName + "\">" + fileName + "</a><br />\n";
            }
            else {
                output += "<a href=\"http://localhost:8765" + request.get("Request-URI") + fileName + "/\">" + fileName + "</a><br />\n";
            }
        }

        response = output + "</body></html>";
        sendStringResponse();
    }

    public void invalidRequestResponse(Map<String, String> request) {
        response = "HTTP/1.1 404 NF\nContent-Type: text/html; charset=UTF-8\n\n<html><body><h1>404 File not found</h1></body></html>";
        sendStringResponse();
    }

    public void imageResponse (Map<String, String> request) {
//        response = "put something here";
//        sendImageResponse();
    }
}