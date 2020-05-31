package com.example.bolt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.example.model.EventModel;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MsgBolt extends BaseRichBolt {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgBolt.class);

    private static final Base64.Decoder decoder = Base64.getDecoder();

    private OutputCollector collector;

    private Fields fields;


    public MsgBolt(){

        this.fields = new Fields("user_id","book_id","action", "tagIds","time");
    }


    @Override
    public void prepare(Map map,
                        TopologyContext topologyContext,
                        OutputCollector outputCollector) {

        this.collector = outputCollector;

    }

    private List<EventModel> doJson(String jsonData) throws Exception {

        List<String> list = JSON.parseArray(jsonData, String.class);
        List<EventModel> res = new ArrayList<>();
        for (String s : list) {
            JSONObject object = JSON.parseObject(s);

            List<String> tags = JSON.parseArray(object.getString(("tagIds")), String.class);
            String tagString = StringUtils.join(tags, '-');

            EventModel eventData = JSON.toJavaObject(
                    object,
                    EventModel.class);
            if (eventData != null) {
                eventData.setTagIds(tagString);

                res.add(eventData);
            }
        }
        return res;

    }


    @Override
    public void execute(Tuple tuple) {

        String str = tuple.getValueByField("kafkaMsg").toString();
        System.out.println("execute");

        try {
            String msg = doDecode(str);

            Pattern pattern = Pattern.compile("(\\d*)$");
            Matcher matcher = pattern.matcher(msg);
            if (matcher.find()) {
                String time = matcher.group();
                String[] jsonData = msg.split(time);

                String msgData = jsonData[0];

                List<EventModel> listData = doJson(msgData);

                Long timeLong = Long.parseLong(time);

                    for (EventModel e : listData) {
                        Values values = new Values();
                        values.add(e.getUserId());
                        values.add(e.getBookId());
                        values.add(e.getAction());
                        values.add(timeLong);

                        collector.emit(tuple, values);

                    }
                collector.ack(tuple);
            }
        } catch (Exception e) {
            LOGGER.error("");
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(this.fields);
    }

    private static String doDecode(String data) throws Exception {

        return new String(decoder.decode(data), StandardCharsets.UTF_8);

    }
}
