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
        int numStartingThreads = Thread.activeCount();
        ThreadGroup tg = new ThreadGroup("Tasks");

        while(serveRequests) {
            try {
                System.out.println("Waiting for a request...");

                //new Thread(tg, this.createTask(server.accept(), (id++ + "")), (id + ""), 100).start();
                new Thread(this.createTask(server.accept(), (id++ + ""))).start();

                if(id == numRequests) {
                    serveRequests = false;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                success = false;
            }
        }

        while(true) {
            if(Thread.activeCount() == numStartingThreads)
                return success;
        }
    }

    public Runnable createTask(Socket s, String name) {
        return new Task(s, name);
    }
}