package com.bing

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.sql.types.StructType

import scala.util.Random

/**
  *
  * @author guobing
  * @date 2019/11/29 上午11:10
  * @description
  * @version 1.0
  *
  */

object AboutSortBy {

  def main(args: Array[String]): Unit = {
    //创建sparksession
    val sparkSession = SparkSession.builder()
      .appName("AboutSortBy")
      .master("local[4]")
      .getOrCreate()
    //导入spark隐式增强
    import sparkSession.implicits._

    //创建sparkContext
    val sparkContext = sparkSession.sparkContext

    //测试分区
    //按分区打印
    val rdd = sparkContext.parallelize(1 to 100)
      .mapPartitionsWithIndex((idx, iter) => {
        println("partitionIdx" + idx + ": " + iter.mkString(","))
        iter
      }).collect()

    //分区内打乱数据顺序
    val rdd1 = sparkContext.parallelize(1 to 100)
      .map(i => i + Random.nextInt(100))
      .mapPartitionsWithIndex((idx, iter) => {
        println("partitionIdx" + idx + ": " + iter.mkString(","))
        iter
      }).collect()

    //分区内打乱顺序再sortby
    val rdd2 = sparkContext.parallelize(1 to 100)
      .map(i => i + Random.nextInt(100))
      .sortBy(i => i)
      .mapPartitionsWithIndex((idx, iter) => {
        println("partitionIdx" + idx + ": " + iter.mkString(","))
        iter
      }).collect()


    //对k-v类型排序
    //对key排序
    val rddsort = sparkContext.parallelize(Array(("cc", 12), ("bb", 32), ("cc", 22), ("aa", 18), ("bb", 16), ("dd", 16), ("ee", 54), ("cc", 1), ("ff", 13), ("gg", 68), ("bb", 4)))
      .reduceByKey(_ + _)
      .sortByKey(true)
      .collect()
      .foreach(item => {
        println(item)
      })
    //对value进行排序
    val rddsort2 = sparkContext.parallelize(Array(("cc", 12), ("bb", 32), ("cc", 22), ("aa", 18), ("bb", 16), ("dd", 16), ("ee", 54), ("cc", 1), ("ff", 13), ("gg", 68), ("bb", 4)))
      .reduceByKey(_ + _)
      .sortBy(_._2, false)
      .collect()
      .foreach(item => {
        println(item)
      })


    sparkContext.stop()
    sparkSession.close()

  }

}
