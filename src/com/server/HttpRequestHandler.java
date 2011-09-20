package com.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpRequestHandler implements RequestHandler {
    public boolean fulfillRequests(ServerSocket server) {
        int count = 0;

        while(count == 0) {
            try {
                System.out.println("Waiting for a request...");
                Socket connection = server.accept();
                BufferedReader request = new BufferedReader
                  (new InputStreamReader(connection.getInputStream()));
                PrintWriter response = new PrintWriter
                  (connection.getOutputStream(), true);

                String temp = "<html><body>";
                String line = request.readLine();
                while(!(line.equals(""))) {
                    System.out.println("Looping through request...");
                    System.out.println(line.equals(""));
                    temp += line + "<br />";
                    line = request.readLine();
                }

                System.out.println("Done looping through request");
                response.println(temp + "</body></html>");

                connection.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            count++;
        }

        return true;
    }
}