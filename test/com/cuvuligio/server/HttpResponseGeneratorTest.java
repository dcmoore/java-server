package com.cuvuligio.server;


import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseGeneratorTest {
    class TestResponseGenerator extends HttpResponseGenerator {
        public TestResponseGenerator(Map<String, String> requestMap, Map<String, ServerResponse> routeMap) {
            super(requestMap, routeMap);
        }

        public File getFile(String relativePath) {
            return new File(System.getProperty("user.dir") + "/test/test_data" + relativePath);
        }

        public byte[] getDirectoryResponse() {
            return "Directory".getBytes();
        }

        public byte[] getFileResponse() {
            return "File".getBytes();
        }

        public byte[] get404Response() {
            if(routes.get("404") != null)
                return "Custom 404".getBytes();
            return "Generic 404".getBytes();
        }
    }

    TestResponseGenerator generator1;

    @Test
    public void getsMyFile() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Request-URI", "/dog.jpg");

        generator1 = new TestResponseGenerator(request, new HashMap<String, ServerResponse>());
        assertEquals("File", new String(generator1.generate()));
    }

    @Test
    public void getsMyDirectory() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Request-URI", "/directory");

        generator1 = new TestResponseGenerator(request, new HashMap<String, ServerResponse>());
        assertEquals("Directory", new String(generator1.generate()));
    }

    @Test
    public void getsMyGeneric404() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Request-URI", "/invalid");

        generator1 = new TestResponseGenerator(request, new HashMap<String, ServerResponse>());
        assertEquals("Generic 404", new String(generator1.generate()));
    }

    @Test
    public void getsMyCustom404() {
        Map<String, String> request = new HashMap<String, String>();
        request.put("Request-URI", "/invalid");

        Map<String, ServerResponse> routes = new HashMap<String, ServerResponse>();
        routes.put("404", new ServerResponse() {
            @Override
            public byte[] get(Map<String, String> request) {
                return new byte[0];
            }
        });

        generator1 = new TestResponseGenerator(request, routes);
        assertEquals("Custom 404", new String(generator1.generate()));
    }
}
