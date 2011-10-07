package com.cuvuligio.server.responses;

import com.cuvuligio.server.ServerResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class TimeResponse extends ServerResponse {
    public byte[] get(Map<String, String> request) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return (getResponseMessage(200) + dateFormat.format(date)).getBytes();
    }
}
