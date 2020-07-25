package com.bing

import org.apache.log4j.{Level, Logger}
import org.apache.spark.TaskContext
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

/**
  *
  * @author guobing
  * @date 2019/12/31 上午10:40
  * @description
  * @version 1.0
  *
  */

object AboutSparkSql1 {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)

    //创建SparkSession对象
    val spark = SparkSession.builder()
      .master("local[4]")
      .appName("AboutSparkSql")
      .getOrCreate()

    //wordcount
    val wordRDD = spark.sparkContext.textFile("/Users/JellyB/Documents/workspace_bing/BigDataSumary/SparkSumary/src/main/java/com/bing/data/wordcount")
     // .flatMap(_.split(" "))
      //  .flatMap(_.substring(0))
        .flatMap(item => item)
      .map((_,1))
      .reduceByKey(_+_)
      .sortBy(_._2,false).collect()
      .foreach(word => {
        println(word)
      })

    /*
      1 张三 15
      2 李四 16
      3 赵武 14
      4 王六 15
    */
    //创建SparkContext读入数据
    val studentRDD = spark.sparkContext.textFile("/Users/JellyB/Documents/workspace_bing/BigDataSumary/SparkSumary/src/main/java/com/bing/data/Students.txt")
      .map(_.split("\\s+"))

    //查看分区
    println("分区数为：" + studentRDD.getNumPartitions)
    studentRDD.foreach(item => {
      println("这是第 " + TaskContext.getPartitionId() + " 个分区")
      println(item)
    })


    //将RDD映射成Row
    val rddRow = studentRDD.map(student => Row(student(0).toInt, student(1), student(2).toInt))
    //使用StrucType定义表结构Schema
    val schema = StructType(List(
      StructField("id", IntegerType, true),
      StructField("name", StringType, true),
      StructField("age", IntegerType, true)
    ))

    //创建dataframe
    val studentDF = spark.createDataFrame(rddRow, schema)
      .createOrReplaceTempView("student")

    //执行sql
    spark.sql("select * from student order by age").show()

    //关闭资源
    spark.stop()

  }

}
