package org.uma.jmetalsp.spark.streamingdatasource;

import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetalsp.observeddata.SingleObservedData;
import org.uma.jmetalsp.observer.Observable;
import org.uma.jmetalsp.observer.impl.DefaultObservable;
import org.uma.jmetalsp.spark.SparkStreamingDataSource;

import java.util.List;

/**
 * This class emits the value of a counter periodically after a given delay (in milliseconds)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class SimpleSparkStreamingCounterDataSource
        implements SparkStreamingDataSource<SingleObservedData<Integer>> {
  private Observable<SingleObservedData<Integer>> observable;

  private JavaStreamingContext streamingContext;
  private String directoryName;


  public SimpleSparkStreamingCounterDataSource(
          Observable<SingleObservedData<Integer>> observable,
          String directoryName) {
    this.observable = observable;
    this.directoryName = directoryName;
  }

  public SimpleSparkStreamingCounterDataSource(String directoryName) {
    this(new DefaultObservable<>(), directoryName);
  }

  @Override
  public void run() {
    JMetalLogger.logger.info("Run method in the streaming data source invoked");
    JMetalLogger.logger.info("Directory: " + directoryName);

    JavaDStream<Integer> time = streamingContext
            .textFileStream(directoryName)
            .map(Integer::parseInt);

    time.foreachRDD(numbers -> {
      if (numbers.rdd().count() > 0) {
        int value = numbers.reduce((value1, value2)-> value1) ;
        //int value = numbers.collect().get(0) // Does not work after Spark 1.6
        System.out.println("Value: "  + value) ;
        observable.setChanged();
        observable.notifyObservers(new SingleObservedData<Integer>(value));
      }
    });
  }

  @Override
  public Observable<SingleObservedData<Integer>> getObservable() {
    return observable;
  }

  @Override
  public void setStreamingContext(JavaStreamingContext streamingContext) {
    this.streamingContext = streamingContext;
  }

}