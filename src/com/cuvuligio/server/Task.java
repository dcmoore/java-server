package com.cuvuligio.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;


public class Task implements Runnable {
    Socket socket;
    Map<String, ServerResponse> routes;

    public Task(Socket s, Map<String, ServerResponse> r) {
        socket = s;
        routes = r;
    }

    public void run() {
        try {
            Map<String, String> parsedRequest = new HttpRequestParser(this.getInputStream()).parse();
            this.addConnectionDetails(parsedRequest);

            byte[] response = new HttpResponseGenerator(parsedRequest, routes).generate();
            this.getOutputStream().write(response);
            socket.close();
        } catch (Exception e) {}

        System.out.println("Thread " + Thread.currentThread().getName() + " is done.");
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public void addConnectionDetails(Map<String, String> parsedRequest) {
        parsedRequest.put("Address", socket.getInetAddress().toString());
        parsedRequest.put("Port", (socket.getLocalPort() + ""));
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }
}
