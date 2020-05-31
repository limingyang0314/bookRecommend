package com.example;

import com.example.bolt.FTLR_bolt;
import com.example.bolt.HBaseBolt;
import com.example.bolt.MsgBolt;
import com.example.config.KafkaConf;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.topology.TopologyBuilder;

public class StormServer {
    public static int NUM_WORKERS = 1;
    public static int NUM_ACKERS = 1;
    public static int MSG_TIMEOUT = 180;

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {

        TopologyBuilder topology = new TopologyBuilder();

        topology.setSpout("kafka_reader",
                new KafkaSpout<>(KafkaConf.getSpout()),
                4);


        topology.setBolt("msg_bolt", new MsgBolt(), 2)
                .shuffleGrouping("kafka_reader");

        //  topology.setBolt("hbase_bolt",new HBaseBolt(),2)
        //     .shuffleGrouping("msg_bolt");
        topology.setBolt("FTLR_bolt", new FTLR_bolt(), 2)
                .shuffleGrouping("msg_bolt");

        Config config = new Config();
        config.setNumWorkers(NUM_WORKERS);
        config.setNumAckers(NUM_ACKERS);
        config.setMessageTimeoutSecs(MSG_TIMEOUT);
        config.setMaxSpoutPending(5000);

        StormSubmitter.submitTopology("test", config, topology.createTopology());

    }
}
