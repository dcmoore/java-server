package com.server;


import org.junit.Test;
import static org.junit.Assert.*;

import java.net.ServerSocket;
import java.net.Socket;

public class IntegrationTest {
    class ThreadedServer implements Runnable {
        public void run() {
            try {
                ServerSocket server = new ServerSocket(8865);
                RequestHandler requests = new HttpRequestHandler();
                requests.setNumRequests(1);
                assertTrue(requests.fulfillRequests(server));    //***** Assertion here
                server.close();
            } catch (Exception e) {}
        }
    }

    @Test
    public void handlesBasicRequest() {
        (new Thread(new ThreadedServer())).start();

        try {
            Socket test = new Socket("localhost", 8865);
            test.getOutputStream().write("GET /echo HTTP/1.1\r\nAccept: Everything\r\n".getBytes());
            test.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
