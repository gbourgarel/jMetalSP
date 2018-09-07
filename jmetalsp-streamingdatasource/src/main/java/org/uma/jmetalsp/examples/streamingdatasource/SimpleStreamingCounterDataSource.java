package org.uma.jmetalsp.examples.streamingdatasource;

import org.uma.jmetalsp.StreamingDataProcessing;
import org.uma.jmetalsp.StreamingDataSource;
import org.uma.jmetalsp.observeddata.ObservedIntegerValue;
import org.uma.jmetalsp.observeddata.SingleObservedData;
import org.uma.jmetalsp.observer.Observable;
import org.uma.jmetalsp.observer.impl.DefaultObservable;
import org.uma.jmetalsp.observer.impl.KafkaObservable;

/**
 * This class emits the value of a counter periodically after a given delay (in milliseconds)
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class SimpleStreamingCounterDataSource
		implements StreamingDataProcessing<ObservedIntegerValue> {
	private Observable<ObservedIntegerValue> observable;
	private int dataDelay ;

	/**
	 *
	 * @param observable
	 * @param dataDelay Delay in milliseconds
	 */
	public SimpleStreamingCounterDataSource(Observable<ObservedIntegerValue> observable, int dataDelay) {
		this.observable = observable ;
		this.dataDelay = dataDelay ;
	}

	public SimpleStreamingCounterDataSource(int dataDelay) {
		this(new DefaultObservable<>(), dataDelay);
	}

	public SimpleStreamingCounterDataSource(int dataDelay, Observable<ObservedIntegerValue> observable) {
		this(observable, dataDelay) ;
	}

	@Override
	public void run() {
		int counter = 0 ;
		while (true) {
			try {
				Thread.sleep(dataDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			observable.setChanged(); ;
			observable.notifyObservers(new ObservedIntegerValue(counter));
			counter ++ ;
		}
	}

	@Override
	public Observable<ObservedIntegerValue> getObservable() {
		return this.observable;
	}


	/**
	 * main() method to run the streaming por
	 * @param args
	 */

	public static void main(String[] args) {
		String topicName = "prueba-int-topic-from-main" ;

		SimpleStreamingCounterDataSource simpleStreamingCounterDataSource =
				new SimpleStreamingCounterDataSource(
						new KafkaObservable<>(topicName, new ObservedIntegerValue()), 2000) ;

		simpleStreamingCounterDataSource.run();
	}
}

