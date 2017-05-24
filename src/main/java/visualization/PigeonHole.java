package visualization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * This file is part of PigeonholeSort
 * <p>
 * Created by Federico Bertani on 22/05/17.
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

/**
 * This class represent and element of the temporary array used for pigeonhole sorting. It's an
 * array of lists. The list contains elements of equal value of the array to sort. When and {@link
 * PigeonHole} is created it's only a {@link StackPane} with a {@link
 * javafx.scene.shape.Rectangle} and {@link javafx.scene.text.Text}. When a {@link ArrayElementBox}
 * is added it's moved under the already existent ones. Appropriates animations are provided.
 */
public class PigeonHole extends ArrayElementBox {

  /**
   * List of {@link ArrayElementBox} associated to this position of temporary array
   */
  private List<ArrayElementBox> listOfArrayBoxes;

  /**
   * Create a new temporary array element.
   *
   * @param number int: the number show in the center of the rectangle. It's reflect the array
   * position of this element.
   */
  public PigeonHole(int number) {
    super(number);
    //translate the box vertically
    this.setTranslateY(ArrayElementBox.boxTotalSize * 2);
    //translate the box horizontally according to element index in the temporary array
    this.setTranslateX(ArrayElementBox.boxTotalSize * number);
    //initialize list of elements
    listOfArrayBoxes = new ArrayList<>();
  }

  /**
   * Add a new {@link ArrayElementBox} to the {@link PigeonHole}
   *
   * @param arrayElementBox {@link ArrayElementBox}: the array box to add
   * @param stepTransitionEventHandler MainController.StepTransitionEventHandler: a handler for
   * OnFinished transition event
   * @return TranslateTransition: the animation for showing the addition
   */
  public TranslateTransition add(ArrayElementBox arrayElementBox,
      MainController.StepTransitionEventHandler stepTransitionEventHandler) {
    listOfArrayBoxes.add(arrayElementBox);
    //create a new translate transition for the array element box with a default duration of 1 seconds
    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1),
        arrayElementBox);
    //bind the animation duration to the main duration property
    translateTransition.durationProperty().bind(MainController.animationsDuration);
    //set the final X position as the X position of the current PigeonHole
    translateTransition.setToX(this.getTranslateX());
    //set the final Y position as the Y position of the current PigeonHole plus a value proportional
    // to the number of element in the PigeonHole
    translateTransition.setToY(this.getTranslateY() + (50 * this.listOfArrayBoxes.size()));
    //set the OnFinished event handler
    translateTransition.setOnFinished(stepTransitionEventHandler);
    //return the translate transition
    return translateTransition;
  }

  /**
   * Create the move back animation from the {@link PigeonHole} to the array to sort.
   *
   * @param arrayBoxesToSort List<ArrayElementBox>: list of graphics representation of array element
   * to sort
   * @param arrayToSortOffset int: index of the array where the elements of this {@link PigeonHole}
   * will be copied
   * @param stepTransitionEventHandler MainController.StepTransitionEventHandler: a handler for
   * OnFinished transition event
   * @return ParallelTransition: an animation for showing the move back phase ({@link
   * TranslateTransition} + {@link FadeTransition)}
   */
  public ParallelTransition getMoveBackAnimation(List<ArrayElementBox> arrayBoxesToSort,
      int arrayToSortOffset,
      MainController.StepTransitionEventHandler stepTransitionEventHandler) {
    //create a new parallelTransition
    ParallelTransition parallelTransition = new ParallelTransition();
    //for each array element box contained into this object list
    for (ArrayElementBox arrayElementBox : this.listOfArrayBoxes) {
      //create a new Translate transition for the array element box
      TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1),
          arrayElementBox);
      //bind the animation duration to the main duration property
      translateTransition.durationProperty().bind(MainController.animationsDuration);
      //get the array element box that occupy the position where the ordered element box will be placed
      ArrayElementBox arrayElementBoxOld = arrayBoxesToSort.get(arrayToSortOffset);
      //set the final X position as the old element box X position
      translateTransition.setToX(arrayElementBoxOld.getTranslateX());
      //set the final Y position as the old element box Y position
      translateTransition.setToY(arrayElementBoxOld.getTranslateY());
      //create a fade transition for removing the old element box from the scene
      FadeTransition fadeTransition = arrayElementBoxOld.getRemovingFadeTransition();
      //bind the animation duration to the main duration property
      fadeTransition.durationProperty().bind(MainController.animationsDuration);
      //add the fade and translate transition to the parallel transition
      parallelTransition.getChildren().addAll(translateTransition, fadeTransition);
      //TODO also remove old StackPane from the scene graph
      //increase array offset because an element has been overwrite
      arrayToSortOffset++;
    }
    //set parallel transition onFinished handler
    parallelTransition.setOnFinished(stepTransitionEventHandler);
    //return the parallel transition created
    return parallelTransition;
  }

  /**
   * Returns the size of the list of array box of this element
   *
   * @return int: the size of the list of array box of this element
   */
  public int getListSize() {
    return this.listOfArrayBoxes.size();
  }

  /**
   * load the fxml representing this graphical object. This method override the method declared in
   * {@link ArrayElementBox} for changing the fxml file path.
   */
  protected void loadFXML() {
    //create a new fxml loader for loading the fxml from file
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pigeon.fxml"));
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

}
