package com.bing

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.flink.api.scala._

/**
  *
  * @author guobing
  * @date 2020/6/10 上午8:47
  * @description
  * @version 1.0
  *
  */

object FlinkKafkaSource {
  def main(args: Array[String]): Unit = {
    //1、创建ExecutionEnvironment环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //2、设置kafka配置参数
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"spark:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG,"bing")

    //3、创建kafka消费者
    val kafkaConsumer = new FlinkKafkaConsumer("topic01",new SimpleStringSchema(),props)

    //4、设置kafka source
    val lines:DataStream[String] = env.addSource(kafkaConsumer)

    //5、对lines实现算子转化
    lines.flatMap(_.split("\\s+"))
      .map((_,1))
      .keyBy(0)
      .sum(1)
      .print()

    //6、执行任务
    env.execute("wordcount")

  }

}
