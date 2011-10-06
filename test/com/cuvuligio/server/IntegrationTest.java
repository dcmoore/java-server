package com.cuvuligio.server;


import com.cuvuligio.server.responses.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntegrationTest {
    @Test
    public void respondsToCustomRoutesTest() {
        Server server = new Server();
        ServerResponse response1 = new TimeResponse();
        ServerResponse response2 = new EchoResponse();

        server.addRoute("/time", response1);
        server.addRoute("/echo", response2);
        server.removeRoute("/echo");
        server.setCurrentPort(8383);

        server.start();
        while(!server.isActive()) {}

        //Send a request out with a Request-URI of /time
        //Test response

        try {
            while(!server.gracefulKill()) {}
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}