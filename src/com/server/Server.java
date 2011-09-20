package com.server;

import java.net.ServerSocket;

/**
 * This file runs the server
 */

public class Server {
    public static void main (String[] args) {
        startServer(8765, new HttpRequestHandler());
    }

    public static boolean startServer(int port, RequestHandler requestListener) {
        boolean success = true;
        boolean failure = false;

        System.out.println("Starting the server on port: " + port);

        try {
            ServerSocket server = new ServerSocket(port);
            requestListener.fulfillRequests(server);
            server.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return failure;
        }

        System.out.println("Closed the server on port: " + port);
        return success;
    }
}
