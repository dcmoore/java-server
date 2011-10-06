package com.cuvuligio.server;

import java.net.Socket;
import java.util.Map;


public class Task implements Runnable {
    public Task(Socket s, Map<String, ServerResponse> r) {

    }

    public void run() {
        System.out.println("Running Task");
    }
}
