package com.example.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.storm.kafka.spout.*;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.List;


public class KafkaConf {
    private static final String CONSUMER_TOPIC = "streaming";
    private static final String Zk_COLLECT = "localhost:9092,localhost:9092,localhost:9092";
    private static final String GROUP_ID = "OnlineRank";

    public static KafkaSpoutConfig<String, String> getSpout() {

        return  KafkaSpoutConfig
                .builder(Zk_COLLECT, CONSUMER_TOPIC)
                .setProp(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID)
                .setFirstPollOffsetStrategy(
                        FirstPollOffsetStrategy.LATEST)
                .setOffsetCommitPeriodMs(10000)
                .setRecordTranslator(VALUES_FUNC, new Fields("kafkaMsg"))
                .setRetry(getRetry())
                .build();
    }


    private static KafkaSpoutRetryService getRetry() {
        return new KafkaSpoutRetryExponentialBackoff(
                KafkaSpoutRetryExponentialBackoff
                        .TimeInterval.microSeconds(500),
                KafkaSpoutRetryExponentialBackoff
                        .TimeInterval.milliSeconds(2),
                Integer.MAX_VALUE,
                KafkaSpoutRetryExponentialBackoff
                        .TimeInterval.seconds(10));
    }

    private static Func<ConsumerRecord<String, String>, List<Object>>
            VALUES_FUNC = (Func<ConsumerRecord<String, String>, List<Object>>) record -> new Values(record.value());

}
