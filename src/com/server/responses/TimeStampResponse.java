package com.server.responses;


import com.server.HttpResponseGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class TimeStampResponse extends HttpResponseGenerator {
    public TimeStampResponse(Map<String, String> r) {
        super(r);
    }

    public byte[] get() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        for(long i = (7*Integer.MIN_VALUE); i < Integer.MAX_VALUE; i++) {}

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return (getStatusLine(200) + "Content-Type: text/html; charset=UTF-8\r\n\r\n" + dateFormat.format(date)).getBytes();
    }
}