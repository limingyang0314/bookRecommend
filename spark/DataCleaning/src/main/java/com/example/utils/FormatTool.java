package com.example.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormatTool {
    public static Map<String,String> dataFormat(Long timestamp) {
        Map<String, String> map = new HashMap<>();

        String format = "yyyy-MM-dd-HH";
        SimpleDateFormat sf = new SimpleDateFormat(format);
        String dateString = sf.format(
                new Date(timestamp)
        );
        String[] dateArr = dateString.split("-");
        map.put("year", dateArr[0]);
        map.put("month", dateArr[1]);
        map.put("day", dateArr[2]);
        map.put("hour", dateArr[3]);

        return map;
    }
}
