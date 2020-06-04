package com.example.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.model.EventModel;
import org.apache.commons.lang.StringUtils;


import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OperaFormat {
    private static Base64.Decoder decoder = Base64.getDecoder();

    private static String doDecode(String code) throws Exception {

        return new String(
                decoder.decode(code), StandardCharsets.UTF_8);
    }

    private static void doJson(String jsonData, long time) throws Exception {

        List<String> list = JSON.parseArray(jsonData, String.class);
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

                writeToHDFS(eventData, time);
            }
        }

    }

    public static void doFormat(String data) throws Exception {
        String decodeString = doDecode(data);

        Pattern pattern = Pattern.compile("(\\d*)$");
        Matcher matcher = pattern.matcher(decodeString);
        if (matcher.find()) {
            String time = matcher.group();
            String[] jsonData = decodeString.split(time);
            doJson(jsonData[0], Long.parseLong(time));
        }
    }

    public static void writeToHDFS(
            EventModel eventData,
            Long timestamp) throws Exception {

        Map<String, String> map = FormatTool.dataFormat(timestamp);
        String _path = "hive/book-recommend/behavior/";

        String year = map.get("year");
        String month = map.get("month");
        String day = map.get("day");
        String hour = map.get("hour");

        StringBuilder sb = new StringBuilder();

        String dirPath = sb.append(year).append("/")
                .append(month).append("/")
                .append(day).append("/")
                .toString();

        String fileName = year + month + day + hour;

        String wholePath =  _path + dirPath + fileName;
        String line =
                eventData.getUuid() + "," +
                eventData.getUserId() + "," +
                eventData.getBookId() + "," +
                eventData.getTagIds() + "," +
                eventData.getAction() + "," +

                year + "," + month + "," + day + "," + hour + "\n";


        HDFSUtil.writeHDFSFile(wholePath, line);
    }
}
