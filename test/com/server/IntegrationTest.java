package com.server;


import org.junit.Test;

import java.net.ServerSocket;
import java.net.Socket;

public class IntegrationTest {
    class ThreadedServer implements Runnable {
        public void run() {
            try {
                ServerSocket server = new ServerSocket(8865);
                RequestHandler requests = new HttpRequestHandler();
                requests.setNumRequests(1);
                requests.fulfillRequests(server);
                server.close();
            } catch (Exception e) {}
        }
    }

    @Test
    public void handlesBasicRequest() {
        (new Thread(new ThreadedServer())).start();

        try {
            Socket test = new Socket("localhost", 8865);
            test.getOutputStream().write("GET /echo HTTP/1.1\nAccept: Everything\n".getBytes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
