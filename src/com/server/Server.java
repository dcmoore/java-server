package com.server;

import java.net.ServerSocket;

/*
 * This file runs the server
 */

public class Server {
    public static void main (String[] args) {
        runServer(8765, new HttpRequestHandler());
    }

    public static boolean runServer(int port, RequestHandler requestListener) {
        boolean success;

        System.out.println("Starting the server on port: " + port);

        try {
            ServerSocket server = new ServerSocket(port);
            success = requestListener.fulfillRequests(server);
            server.close();
            System.out.println("Closed the server on port: " + server.getLocalPort());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            success = false;
        }

        return success;
    }
}
