package javaDStream;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class NetcatStream {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("Spark Streaming").setMaster("local[*]");

        // Durations of micro batchs
        JavaStreamingContext sc = new JavaStreamingContext(conf, Durations.seconds(8));

        // Stream source
        JavaReceiverInputDStream<String> dStream = sc.socketTextStream("localhost", 8080);
        //JavaDStream<String> dStream = inputStream.map(s -> s);

        dStream.print();

        sc.start();
        sc.awaitTermination();
    }
}
