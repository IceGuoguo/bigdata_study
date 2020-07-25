package com.bing

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

/**
  *
  * @author guobing
  * @date 2020/7/22 上午8:22
  * @description
  * @version 1.0
  *
  */

object FlinkSocketSource {

  def main(args: Array[String]): Unit = {
    //创建ExecutionEnvironment
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //创建数据流
    val dataStream = env.socketTextStream("spark", 9999)
    val dataStream2 = env.socketTextStream("spark", 8888)

    //union算子
    //    dataStream.union(dataStream2)
    //      .flatMap(_.split("\\s+"))
    //      .map((_,1))
    //      .keyBy(_._1)
    //      .sum(1)
    //      .print("union")

    //reduce算子
    //    dataStream
    //      .flatMap(_.split("\\s+"))
    //      .map((_, 1))
    //      .keyBy(0)
    //      .reduce((v1, v2) => (v1._1, v1._2 + v2._2))
    //      .print("reduce")

    //fold算子

    dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(0)
      .fold(("", 5))((v1, v2) => (v1._1, v1._2 + v2._2))
      .print("fold")



    //执行
    env.execute("flinksocketsource")
  }

}
