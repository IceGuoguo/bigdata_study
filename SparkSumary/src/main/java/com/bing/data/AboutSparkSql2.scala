package com.bing.data

import java.util.Properties

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  *
  * @author guobing
  * @date 2019/12/31 下午3:24
  * @description
  * @version 1.0
  *
  */

//定义case class代替schema
case class Student(stuId:Int,stuName:String,stuAge:Int)

object AboutSparkSql2 {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)

    //创建SparkSession
    val spark = SparkSession.builder()
      .master("local[4]")
      .appName("AboutSparkSql2")
      .getOrCreate()

    //通过SparkContext读入数据
    val studentRDD = spark.sparkContext.textFile("/Users/JellyB/Documents/workspace_bing/BigDataSumary/SparkSumary/src/main/java/com/bing/data/Students.txt")
      .map(_.split("\\s+"))
    //将数据和case class关联
    val dataRDD = studentRDD.map(data => Student(data(0).toInt,data(1),data(2).toInt))
    //将rdd转化为dataframe
    //导入隐式增强
    import spark.sqlContext.implicits._
    val studentDF = dataRDD.toDF()

    //注册表或者视图
    studentDF.createOrReplaceTempView("student")
    //执行sql
//    spark.sql("select * from student where stuAge > 14").show()

    val resultDF = spark.sql("select * from student where stuAge = 14")

    //将结果保存在mysql，方法一
    val props = new Properties()
    props.setProperty("user","root")
    props.setProperty("password","111111")
    resultDF.write
      .mode(SaveMode.Append)
      .jdbc("jdbc:mysql://localhost:3306/emp","t_student",props)

    //将结果保存在mysql中，方法二
    /*resultDF.write.format("jdbc")
      .mode(SaveMode.Overwrite)
      .option("user","root")
      .option("password","111111")
      .option("url","jdbc:mysql://localhost:3306/emp")
      .option("dbtable","t_student")
      .save()*/

    spark.stop()

  }

}
