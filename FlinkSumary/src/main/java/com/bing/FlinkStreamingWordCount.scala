package com.bing

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

/**
  *
  * @author guobing
  * @date 2020/6/9 下午2:34
  * @description
  * @version 1.0
  *
  */

case class WordPair(word: String, count: Int)

object FlinkStreamingWordCount {

  def main(args: Array[String]): Unit = {
    //1、创建StreamExecutionEnvironment
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //2、创建输入流
    val lines:DataStream[String] = env.socketTextStream("spark",9999)

    //3、wordcount
    lines.flatMap(_.split("\\s+"))
      .map(WordPair(_,1))
      .keyBy(_.word)
      .sum("count")
      .print()

    //其他形式
    lines.flatMap(_.split("\\s+"))
      .map((_,1))
      .keyBy(_._1)
      .sum(1)
      .print()

    //4、执行任务
    env.execute("wordcount")

  }

}
