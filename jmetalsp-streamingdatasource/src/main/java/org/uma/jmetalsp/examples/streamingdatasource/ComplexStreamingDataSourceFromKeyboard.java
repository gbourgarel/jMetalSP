package org.uma.jmetalsp.examples.streamingdatasource;

import org.uma.jmetalsp.StreamingDataSource;
import org.uma.jmetalsp.observeddata.SingleObservedData;
import org.uma.jmetalsp.observer.Observable;
import org.uma.jmetalsp.observer.impl.DefaultObservable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This class reads double values from the keyboard and returns them as a list
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ComplexStreamingDataSourceFromKeyboard implements
        StreamingDataSource<SingleObservedData<List<Double>>> {
  private Observable<SingleObservedData<List<Double>>> observable;

  /**
   * @param observable
   */
  public ComplexStreamingDataSourceFromKeyboard(Observable<SingleObservedData<List<Double>>> observable) {
    this.observable = observable;
  }

  public ComplexStreamingDataSourceFromKeyboard() {
    this(new DefaultObservable<>());
  }


  @Override
  public void run() {
    Scanner scanner = new Scanner(System.in);

    List<Double> values = new ArrayList<>();
    while (true) {
      System.out.println("Introduce the new reference point(between commas):");
      String s = scanner.nextLine() ;
      Scanner sl= new Scanner(s);
      sl.useDelimiter(",");
      try {
        while (sl.hasNext()){
         values.add(Double.parseDouble(sl.next()));
        }
      }catch (Exception e){//any problem
        values.add(0.0);
        values.add(0.0);
      }
      System.out.println("REF POINT: " + values.toArray()) ;

      observable.setChanged();

      observable.notifyObservers(new SingleObservedData<>(values));
    }
  }

  @Override
  public Observable<SingleObservedData<List<Double>>> getObservable() {
    return observable;
  }
}
