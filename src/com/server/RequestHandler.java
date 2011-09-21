package com.server;

import java.net.ServerSocket;

public interface RequestHandler {
    public long getNumRequests();
    public void setNumRequests(long n);
    public boolean fulfillRequests(ServerSocket server);
}