package com.bing

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{OutputTag, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.flink.api.scala._

/**
  *
  * @author guobing
  * @date 2020/7/15 上午9:10
  * @description
  * @version 1.0
  *
  */

object FlinkKafkaSourceIn {

  def main(args: Array[String]): Unit = {
    //创建ExecutionEnvironment
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //设置kafka参数
    //消费者
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "spark:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "bing")
    //生产者
    val props2 = new Properties()
    props2.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "spark:9092")

    //创建kafka生产者和消费者
    val kafkaConsumer = new FlinkKafkaConsumer("testconsumer", new SimpleStringSchema(), props)
    val kafkaProducer = new FlinkKafkaProducer[(String, String)]("testproducer", new CustomKeyedSerializationSchema(), props2)


    //创建边输出流
    val errorTag = new OutputTag[String]("error")

    //创建数据流
    val dataStream = env.addSource(kafkaConsumer)
//      .filter(str => str.contains("success"))
      .process(new ProcessFunction[String, String] {
        override def processElement(value: String, ctx: ProcessFunction[String, String]#Context, out: Collector[String]): Unit = {
          //输出错误信息
          if (value.contains("error")) {
            ctx.output(errorTag, value)
          } else {
            //输出正确数据流
            out.collect(value)
          }
        }
      })

    //错误信息边输出
    val errorInfo = dataStream.getSideOutput(errorTag)
    errorInfo.print("error")
    dataStream.print("success")

    //将正确信息流输出到kafka生产者
    dataStream.map(str => ("success",str))
//      .print("success")
      .addSink(kafkaProducer)

    //执行流计算
    env.execute()

  }

}
