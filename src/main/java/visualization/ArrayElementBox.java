/*
 * This file is part of PigeonholeSort
 * <p>
 * Created by Federico Bertani on 21/05/17.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package visualization;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;



/**
 * This class represent graphically an element of an array. It's made of a {@link StackPane}
 * containing a {@link javafx.scene.shape.Rectangle} and a {@link Text}.
 */
public class ArrayElementBox extends StackPane implements Cloneable {

  /**
   * the size of an array box. Both width and height
   */
  public final static int boxSize = 40;

  /**
   * the total margin on width or height of an array box.
   */
  public final static int boxMargin = 20;

  /**
   * the total size of array box. Equal to the size of a box + its margin
   */
  public final static int boxTotalSize = boxSize + boxMargin;

  /**
   * Reference to the text at the center of array box
   */
  @FXML
  private Text number;

  /**
   * Create a new array box.
   *
   * @param number int: the number that will be show inside the rectangle.
   */
  public ArrayElementBox(int number) {
    this(String.valueOf(number));
  }

  /**
   * Create a new array box.
   *
   * @param text String: a string representing that will be show inside the rectangle.
   */
  private ArrayElementBox(String text) {
    //load fxml representing the object
    loadFXML();
    //set the text given to the number
    this.number.setText(text);
    //set initial opacity of the element to 0%. The opacity will be increased with a fade in animation.
    this.setOpacity(0);
  }

  /**
   * load the fxml representing this {@link ArrayElementBox}.
   */
  protected void loadFXML() {
    //Create a new FXMLLoader that will load fxml from file
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/arrayElement.fxml"));
    //set the root element as this object
    fxmlLoader.setRoot(this);
    //set the controller as this object
    fxmlLoader.setController(this);
    try {
      //load the fxml from file
      fxmlLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Create a new fade transition for representing the appearance of the array box in the scene
   *
   * @param stepTransitionEventHandler MainController.StepTransitionEventHandler: a handler for
   * onFinished transition event.
   * @return FadeTransition: the fade transition from 0 to 100 opacity of the array box
   */
  public FadeTransition getCreationFadeTransition(
      MainController.StepTransitionEventHandler stepTransitionEventHandler) {
    //create a new fade transition for the appearance of the new array box in the scene
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(100), this);
    //the fade transition will be from 0% opacity
    fadeTransition.setFromValue(0);
    //to 100% opacity value
    fadeTransition.setToValue(100);
    //set the on finished event handler
    fadeTransition.setOnFinished(stepTransitionEventHandler);
    //bind the animation duration to the main duration property
    fadeTransition.durationProperty().bind(MainController.animationsDuration);
    //return the just created fade transition
    return fadeTransition;
  }

  public FadeTransition getRemovingFadeTransition() {
    //create a new fade transition for the disappearance of an array box
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(100), this);
    //set initial value of fade transition to 100% opacity
    fadeTransition.setFromValue(100);
    //set final value of fade transition
    fadeTransition.setToValue(0);
    //bind the animation duration to the main duration property
    fadeTransition.durationProperty().bind(MainController.animationsDuration);
    //return the just created fade transition
    return fadeTransition;
  }

  /**
   * get as int the value of the array element box
   *
   * @return int: the value of the array element box
   */
  public int getNumber() {
    //return the parsed string obtained from the TextField
    return Integer.parseInt(number.getText());
  }

  /**
   * Clone the array element box.
   *
   * @return ArrayElementBox: array element box with the same position and the same text
   */
  public ArrayElementBox clone() {
    //create a new array element box with the same number of the current
    ArrayElementBox arrayElementBox = new ArrayElementBox(this.number.getText());
    arrayElementBox.setOpacity(100);
    //clone the x position property
    arrayElementBox.setTranslateX(this.getTranslateX());
    //clone the y position property
    arrayElementBox.setTranslateY(this.getTranslateY());
    //return the new array element box
    return arrayElementBox;
  }
}
