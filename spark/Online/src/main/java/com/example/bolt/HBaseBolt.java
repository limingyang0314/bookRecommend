package com.example.bolt;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hbase.thirdparty.io.netty.handler.codec.http2.Http2FrameReader;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.io.IOException;
import java.util.Map;

public class HBaseBolt extends BaseBasicBolt {
    private Connection connection;
    private Table table;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        Configuration conf = HBaseConfiguration.create();
        try {
            connection = ConnectionFactory.createConnection(conf);
            table = connection.getTable(
                    TableName.valueOf("online_action"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cleanup() {
        try {
            if(table != null) table.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {

        String click_type = tuple.getValueByField("click_type").toString();
        String uid = tuple.getValueByField("uid").toString();
        try {
            Put put = new Put(Bytes.toBytes(uid));
            put.addColumn(Bytes.toBytes("action"),
                    Bytes.toBytes("click"),
                    Bytes.toBytes(click_type));
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
