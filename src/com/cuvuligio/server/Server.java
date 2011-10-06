package com.cuvuligio.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable {
    private ServerSocket server;
    private int port;
    private Map<String, ServerResponse> routes;
    private int numRequests;
    ThreadGroup taskGroup;

    public Server() {
        port = 8765;
        routes = new HashMap<String, ServerResponse>();
        numRequests = 0;
        taskGroup = new ThreadGroup("Tasks");
    }

    public void setCurrentPort(int p) {
        port = p;
    }

    public int getCurrentPort() {
        return port;
    }

    public void addRoute(String route, ServerResponse response) {
        routes.put(route, response);
    }

    public void removeRoute(String route) {
        routes.remove(route);
    }

    public Map<String, ServerResponse> getRoutes() {
        return routes;
    }

    public int getNumRequests() {
        return numRequests;
    }

    public int getNumActiveTasks() {
        return taskGroup.activeCount();
    }

    public boolean isActive() {
        return !(server == null);
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        System.out.println("Starting the server on port: " + port);

        try {
            server = new ServerSocket(port);
            while(true) {
                System.out.println("Waiting for a request...");

                Socket socket = this.acceptRequest();
                numRequests++;
                Runnable task = this.createTask(socket, routes);
                new Thread(taskGroup, task, (numRequests + "")).start();
            }
        } catch (SocketException e) {
            System.out.println("Closed the server on port: " + port);
        } catch (Exception e) {}
    }

    private Socket acceptRequest() throws IOException {
        return server.accept();
    }

    private Runnable createTask(Socket s, Map<String, ServerResponse> r) {
        return new Task(s, r);
    }

    public boolean gracefulKill() throws IOException {
        while(this.getNumActiveTasks() != 0) {}
        return kill();
    }

    public boolean kill() throws IOException {
        if(server != null && !server.isClosed()) {
            server.close();
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        new Server().start();
    }
}