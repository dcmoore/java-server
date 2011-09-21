package com.server;

import java.net.ServerSocket;
import java.net.Socket;

public class HttpRequestHandler implements RequestHandler {
    private long numRequests = 10;

    public long getNumRequests() {
        return numRequests;
    }

    public void setNumRequests(long n) {
        numRequests = n;
    }

    public boolean fulfillRequests(ServerSocket server) {
        boolean success = true;
        boolean serveRequests = true;
        long id = 0;

        while(serveRequests) {
            try {
                System.out.println("Waiting for a request...");
                Socket connection = server.accept();

                new Thread(new Task(connection, (id++ + ""))).start();

                if(id == numRequests) {
                    serveRequests = false;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                success = false;
            }
        }

        return success;
    }
}