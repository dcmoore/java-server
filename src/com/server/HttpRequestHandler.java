package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpRequestHandler implements RequestHandler {
    private long numRequests = 0; //Go infinitely
    public boolean errorFree = true;

    public void setNumRequests(long n) {
        numRequests = n;
    }

    public boolean fulfillRequests(ServerSocket server) {
        boolean serveRequests = true;
        long id = 0;
        int numStartingThreads = Thread.activeCount();

        while(serveRequests) {
            try {
                System.out.println("Waiting for a request...");

                Socket socket = server.accept();
                Runnable task = this.createTask(socket);
                new Thread(new ThreadGroup("Tasks"), task, (id++ + "")).start();

                if(id == numRequests) {
                    serveRequests = false;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                this.errorFree = false;
            }
        }

        while(true) {
            if(Thread.activeCount() == numStartingThreads)
                return this.errorFree;
        }
    }

    public Runnable createTask(Socket s) throws IOException{
        return new Task(s);
    }
}