//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetalsp.consumer;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetalsp.AlgorithmDataConsumer;
import org.uma.jmetalsp.DynamicAlgorithm;
import org.uma.jmetalsp.updatedata.AlgorithmResultData;
import org.uma.jmetalsp.perception.Observable;
import org.uma.jmetalsp.perception.Observer;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class SimpleSolutionListConsumer implements AlgorithmDataConsumer<AlgorithmResultData> {
  private DynamicAlgorithm<?, AlgorithmResultData> dynamicAlgorithm;

  @Override
  public void setAlgorithm(DynamicAlgorithm<?, AlgorithmResultData> algorithm) {
    this.dynamicAlgorithm = algorithm;
  }

  @Override
  public DynamicAlgorithm<?, AlgorithmResultData> getAlgorithm() {
    return dynamicAlgorithm;
  }

  @Override
  public Observer getObserver() {
    return this;
  }

  @Override
  public void run() {
    if (dynamicAlgorithm == null) {
      throw new JMetalException("The algorithm is null");
    }

    dynamicAlgorithm.getObservable().register(this);

    while (true) {
      try {
        Thread.sleep(1000000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void update(Observable<?> observable, Object data) {
    //if ("algorithm".equals(observable.getName())) {
    AlgorithmResultData algorithmResultData = (AlgorithmResultData) data;
    System.out.println("Number of generated fronts: " + algorithmResultData.getIterations());
    System.out.println("Size of the front: " + algorithmResultData.getSolutionList().size());

    //}
  }
}