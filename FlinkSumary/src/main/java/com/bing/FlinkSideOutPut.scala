package com.bing

import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{OutputTag, StreamExecutionEnvironment}
import org.apache.flink.util.Collector
import org.apache.flink.api.scala._

/**
  *
  * @author guobing
  * @date 2020/6/11 上午8:28
  * @description
  * @version 1.0
  *
  */

object FlinkSideOutPut {
  def main(args: Array[String]): Unit = {
    //创建ExecutionEnvironment
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //创建边输出标志
    val outputTag = new OutputTag[String]("error"){}

    //创建DataStream数据流,处理边输出
    val stream = env.socketTextStream("spark",9999)
      .process(new ProcessFunction[String,String] {
        override def processElement(value: String, context: ProcessFunction[String, String]#Context, out: Collector[String]): Unit = {
          if(value.contains("error")){
            context.output(outputTag,value)
          }else{
            out.collect(value)
          }
        }
      })

    stream.print("info:")
    stream.getSideOutput(outputTag).print("error")

    //执行任务
    env.execute("outsideput")


  }

}
