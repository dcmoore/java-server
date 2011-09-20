package com.server;

import java.net.ServerSocket;

public interface RequestHandler {
    public boolean fulfillRequests(ServerSocket server);
}