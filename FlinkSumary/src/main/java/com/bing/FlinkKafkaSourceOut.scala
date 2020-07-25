package com.bing

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.flink.api.scala._
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}

/**
  *
  * @author guobing
  * @date 2020/7/14 下午4:48
  * @description
  * @version 1.0
  *
  */

object FlinkKafkaSourceOut {

  def main(args: Array[String]): Unit = {

    //创建ExecutionEnvironment
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //配置kafka连接参数
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"spark:9092")

    //创建kafka消费者
    val kafkaProducer = new FlinkKafkaProducer[String]("testproducer",new SimpleStringSchema(),props)

    //创建消费者数据流
    val lines = env.socketTextStream("spark",9999)

    //对数据实现算子转化

    lines
      .addSink(kafkaProducer)

    //执行任务
    env.execute("keyedsource")

  }

}
