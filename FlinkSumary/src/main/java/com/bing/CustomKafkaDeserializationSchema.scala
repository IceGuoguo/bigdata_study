package com.bing

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.flink.api.scala._

/**
  *
  * @author guobing
  * @date 2020/7/7 上午8:54
  * @description
  * @version 1.0
  *
  */

class CustomKafkaDeserializationSchema extends KafkaDeserializationSchema[(String,String,Int,Long)]{

  override def isEndOfStream(t: (String, String, Int, Long)): Boolean = false

  override def deserialize(consumerRecord: ConsumerRecord[Array[Byte], Array[Byte]]): (String, String, Int, Long) = {
    var key = ""
    if(consumerRecord.key() != null && consumerRecord.key().size != 0){
      key = new String(consumerRecord.key())
    }
    val value = new String(consumerRecord.value())
    (key,value,consumerRecord.partition(),consumerRecord.offset())

  }

  override def getProducedType: TypeInformation[(String, String, Int, Long)] = {
    createTypeInformation[(String,String,Int,Long)]
  }
}
