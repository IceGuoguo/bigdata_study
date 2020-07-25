package com.bing;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
//import org.apache.spark.ml.recommendation.ALS;


import java.io.Serializable;

/**
 * @author guobing
 * @version 1.0
 * @date 2020/5/21 下午2:47
 * @description
 */

public class REcommendProJ {

    //Rating实体
    public static class Rating implements Serializable {
        private int userId;
        private int movieId;
        private float rating;
        private long timestamp;

        public Rating() {
        }

        public Rating(int userId, int movieId, float rating, long timestamp) {
            this.userId = userId;
            this.movieId = movieId;
            this.rating = rating;
            this.timestamp = timestamp;
        }

        public int getUserId() {
            return userId;
        }

        public int getMovieId() {
            return movieId;
        }

        public float getRating() {
            return rating;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public static Rating parseRating(String str) {
            String[] fields = str.split("::");
            if (fields.length != 4) {
                throw new IllegalArgumentException("Each line must contain 4 fields");
            }
            int userId = Integer.parseInt(fields[0]);
            int movieId = Integer.parseInt(fields[1]);
            float rating = Float.parseFloat(fields[2]);
            long timestamp = Long.parseLong(fields[3]);
            return new Rating(userId, movieId, rating, timestamp);
        }

    }

    //主程序
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("REcommendProJ")
                .getOrCreate();
        JavaRDD<Rating> ratingsRDD = spark.read()
                .textFile("/Users/JellyB/Documents/workspace_bing/BigDataSumary/SparkSumary/src/main/java/com/bing/data/ratingData")
                .javaRDD().map(Rating::parseRating);
        Dataset<Row> ratings = spark.createDataFrame(ratingsRDD, Rating.class);
        Dataset<Row>[] splits = ratings.randomSplit(new double[]{0.8, 0.2});
        Dataset<Row> traning = splits[0];
        Dataset<Row> test = splits[1];

//        org.apache.spark.ml.recommendation.ALS
//        org.apache.spark.mllib.linalg

    }

}
