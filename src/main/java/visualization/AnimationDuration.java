/*
 * This file is part of PigeonholeSort
 *
 * Created by Federico Bertani on 23/05/17.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package visualization;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;


/**
 * This class represent the global value of animation duration.
 * It's an Observable value, so animations duration can be bound to it.
 * Internally it's an adapter from a ObservableValue of Double to an
 * ObservableValue of {@link Duration}.
 */
public class AnimationDuration implements ObservableValue<Duration> {

  private final DoubleProperty milliseconds;
  private final Map<ChangeListener<? super javafx.util.Duration>, ChangeListener<Number>> changeListeners;

  /**
   * Create a new AnimationDuration with the {@link DoubleProperty} given
   * @param milliseconds {@link DoubleProperty}: a double property
   * representing the duration in milliseconds
   */
  public AnimationDuration(DoubleProperty milliseconds) {
    this.milliseconds = milliseconds;
    changeListeners = new HashMap<>();
  }

  private ChangeListener<Number> convertDurationLister(
      ChangeListener<? super javafx.util.Duration> listener) {
    return (observable, oldValue, newValue) -> listener.changed(
        this,
        Duration.millis(oldValue.doubleValue()),
        Duration.millis(newValue.doubleValue()));
  }

  @Override
  public void addListener(ChangeListener<? super javafx.util.Duration> listener) {
    ChangeListener<Number> numberChangeListener = convertDurationLister(listener);
    changeListeners.put(listener, numberChangeListener);
    milliseconds.addListener(numberChangeListener);
  }

  @Override
  public void removeListener(ChangeListener<? super javafx.util.Duration> listener) {
    if (changeListeners.containsKey(listener)) {
      milliseconds.removeListener(changeListeners.get(listener));
    }
  }

  @Override
  public javafx.util.Duration getValue() {
    return javafx.util.Duration.millis(milliseconds.getValue());
  }

  @Override
  public void addListener(InvalidationListener listener) {
    milliseconds.addListener(listener);
  }

  @Override
  public void removeListener(InvalidationListener listener) {
    milliseconds.removeListener(listener);
  }
}

