package com.server.responses;
import com.server.HttpResponseGenerator;
import sun.tools.java.ClassNotFound;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

public class CustomResponse extends HttpResponseGenerator {
    public CustomResponse(Map<String, String> r) {
        super(r);
    }

    public byte[] get(Map<String, String> responseLocation) {
        System.out.println(responseLocation);

        try {
            System.out.println("Test1");
            String classname = responseLocation.get("package") + "." + responseLocation.get("class");
            System.out.println("classname = " + classname);
            Class<?> responseClass = Class.forName(classname);
            System.out.println("Test2");
            Method method = responseClass.getMethod(responseLocation.get("method"), new Class[0]);
            System.out.println("Test3");
            return (byte[]) method.invoke(responseClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            return ("Error: " + e.toString()).getBytes();
        }
    }
}
