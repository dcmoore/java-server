package com.server;

import com.sun.tools.internal.ws.processor.model.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Task implements Runnable {
    private Socket connection;
    private String name;
    private String response = "";

    public String getResponse() {
        return response;
    }

    public Task(Socket c, String n) {
        connection = c;
        name = n;
    }

    public void run() {
        System.out.println("Thread " + name + " is starting...");

        Map<String, String> request = this.parseRequest();
        response = this.generateResponse(request);
        this.sendResponse();

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

            formattedRequest.put("Body", body);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return formattedRequest;
    }

    public String generateResponse(Map<String, String> request) {
        return  request.get("Method") + " " +
                request.get("Request-URI") + " " +
                request.get("HTTP-Version") + "\n";
    }

    public void sendResponse() {
        try {
            PrintWriter sender = new PrintWriter
              (connection.getOutputStream(), true);

            sender.println(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}