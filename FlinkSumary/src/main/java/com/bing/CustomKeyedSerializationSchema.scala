package com.bing

import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema

/**
  *
  * @author guobing
  * @date 2020/7/13 下午2:29
  * @description
  * @version 1.0
  *
  */

class CustomKeyedSerializationSchema extends KeyedSerializationSchema[(String,String)]{
  override def serializeKey(t: (String, String)): Array[Byte] = {
    println(t._1)
    t._1.getBytes()
  }

  override def getTargetTopic(t: (String, String)): String = {
    println(t._1)
    t._1
  }

  override def serializeValue(t: (String, String)): Array[Byte] = {
    println(t._2)
    t._2.getBytes()
  }
}
