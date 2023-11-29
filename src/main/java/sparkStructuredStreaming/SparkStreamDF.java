package sparkStructuredStreaming;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class SparkStreamDF {
    public static void main(String[] args) throws StreamingQueryException, TimeoutException {

        SparkSession spark = SparkSession.builder().appName("StreamFrame").master("local[*]").getOrCreate();

        Dataset<Row> inputData = spark.readStream().format("socket").
                option("host", "localhost").
                option("port", "1111").
                load();

        Dataset<String> words = inputData.as(Encoders.STRING())
                .flatMap((FlatMapFunction<String, String>) line -> Arrays.asList(line.split(" ")).iterator(),
                Encoders.STRING());

        Dataset<Row> resultTable = words.groupBy("value").count();

        StreamingQuery query = resultTable.writeStream().format("console").outputMode("complete").start();

        query.awaitTermination();
    }

}
