package com.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Task implements Runnable {
    private Socket connection;
    private Thread runner;

    public Task(Socket c, String name) {
        connection = c;
        runner = new Thread(this, name);
    }

    public void run() {
        System.out.println("Thread " + runner.getName() + " is starting...");

//        Map<String, String> request = this.parseRequest();
//        String response = ResponseGenerator.generate(request);
//        this.sendResponse(response);

        System.out.println("Thread " + runner.getName() + " is done.");
    }

    public Map<String, String> parseRequest() {
        return new HashMap<String, String>();
    }

    public void sendResponse(String response) {}
}


/*
        try {
            BufferedReader request = new BufferedReader
              (new InputStreamReader(connection.getInputStream()));
            PrintWriter response = new PrintWriter
              (connection.getOutputStream(), true);

            String temp = "<html><body>";
            String line = request.readLine();
            System.out.println("Fulfilling request: " + line);
            while(!(line.equals(""))) {
                temp += line + "<br />";
                line = request.readLine();
            }

            response.println(temp + "</body></html>");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Thread " + runner.getName() + " is done.");
*/