package com.server;


import java.net.Socket;
import java.util.Map;

public class Task implements Runnable {
    private Socket socket;
    public boolean errorFree = true;

    public Task(Socket s) {
        socket = s;
    }

    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() + " is starting...");

        try {
            Map<String, String> parsedRequest = this.parseRequest();
            byte[] response = this.generateResponse(parsedRequest);
            this.sendResponse(response);
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorFree = false;
        }

        System.out.println("Thread " + Thread.currentThread().getName() + " is done.");
    }

    public Map<String, String> parseRequest() throws Exception {
        return new HttpRequestParser(socket.getInputStream()).parse();
    }

    public byte[] generateResponse(Map<String, String> parsedRequest) {
        return new HttpResponseGenerator(parsedRequest).generate();
    }

    public void sendResponse(byte[] response) throws Exception {
        socket.getOutputStream().write(response);
    }
}