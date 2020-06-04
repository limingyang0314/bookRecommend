package com.example.utils;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.example.model.EventModel;

public class Decode implements Serializable {
    private static Base64.Decoder decoder = Base64.getDecoder();

    private static String doDecode(String code) throws Exception {

        return new String(
                decoder.decode(code), StandardCharsets.UTF_8);
    }

    private static List<EventModel> doJson(String jsonData, long time) throws Exception {

        List<String> list = JSON.parseArray(jsonData, String.class);
        Map<String,String> map =  dataFormat(time);
        int year = Integer.parseInt(map.get("year"));
        int month = Integer.parseInt(map.get("month"));
        int hour = Integer.parseInt(map.get("hour"));
        int day = Integer.parseInt(map.get("day"));
        List<EventModel> res = new ArrayList<>();
        for (String s : list) {
            JSONObject object = JSON.parseObject(s);

            List<String> tags = JSON.parseArray(object.getString(("tagIds")), String.class);
            String tagString = StringUtils.join(tags, '-');

            EventModel eventData = JSON.toJavaObject(
                    object,
                    EventModel.class);
            if (eventData != null) {
                if (tagString != null) {
                    eventData.setTagIds(tagString);
                }
                eventData.setYear(year);
                eventData.setMonth(month);
                eventData.setDay(day);
                eventData.setHour(hour);
                res.add(eventData);
            }
        }
        return res;

    }

    public static List<EventModel> doFormat(String data) throws Exception {
        if (data == null) return null;

        String decodeString = doDecode(data);

        Pattern pattern = Pattern.compile("(\\d*)$");
        Matcher matcher = pattern.matcher(decodeString);
        if (matcher.find()) {
            String time = matcher.group();
            String[] jsonData = decodeString.split(time);
            return doJson(jsonData[0], Long.parseLong(time));
        }
        return null;
    }

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

    public static String filter(String str) {

        Pattern pattern = Pattern.compile("(?<=\\?).\\S*");
        String[] lineArr = str.split(" ");
        String _data = lineArr[6];
        Matcher matcher = pattern.matcher(_data);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static List<EventModel> handleData(String str) throws Exception {
        String realData = filter(str);
        return doFormat(realData);
    }
}
