package visualization;

import static visualization.ArrayElementBox.boxTotalSize;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * This file is part of PigeonholeSort
 * <p>
 * Created by Federico Bertani on 20/05/17.
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
public class MainController {

  /**
   * global animation durations
   */
  public static ObservableValue<Duration> animationsDuration;
  /**
   * event handler used in onFinished animation event. Used for creating a step-by-step animation.
   */
  private final StepTransitionEventHandler stepTransitionEventHandler = new StepTransitionEventHandler();
  /**
   * TextField containing the maximum length of the array to sort.
   */

  @FXML
  private StackPane animationPane;
  /**
   * Animation speed slider.
   */
  @FXML
  private Slider animationSpeedSlider;
  /**
   * Maximum value of the generated random array.
   */
  private int max;
  /**
   * list of ArrayElementBox representing the array to sort.
   */
  private ArrayList<ArrayElementBox> arrayBoxesToSort;
  /**
   * Flag for know if the user requested a stepByStep animation.
   */
  private Boolean stepByStep;
  /**
   * Flag for know if the sorting animation is already prepared.
   */
  private Boolean sortingAnimationPrepared;
  /**
   * Queue of Transitions composing animations. Used for creating a step-by step animation.
   */
  private Queue<Transition> transitionQueue;

  /**
   * Get the desired maximum possible value of the generated array to sort from a TextField.
   *
   * @return int: the desired maximum possible value
   */
  private int getMaxRandomValue() {
    //get maxValue from TextField
    //TODO check if the maxValue is a correct value
    TextInputDialog dialog = new TextInputDialog("10");
    dialog.setTitle("Max random value input");
    dialog.setHeaderText("Insert the maximum possible value");

    // Traditional way to get the response value.
    Optional<String> result = dialog.showAndWait();
    int maxValue;
    maxValue = result.map(Integer::parseInt).orElse(10);
    //return the maxValue
    return maxValue;
  }

  /**
   * Get the desired length of generated array
   *
   * @return int: the desired length of generated array
   */
  private int getArrayLength() {
    //get arrayToSort length from TextField
    //TODO check if the length is a correct value
    TextInputDialog dialog = new TextInputDialog("10");
    dialog.setTitle("Array length insert");
    dialog.setHeaderText("Insert the length of the array");

    // Traditional way to get the response value.
    Optional<String> result = dialog.showAndWait();
    int length;
    length = result.map(Integer::parseInt).orElse(10);
    //return the length
    return length;
  }

  /**
   * Handler of step button click.
   * Plays the next animation.
   */
  @FXML
  private void onStepButtonClick() {
    //set that the user requested a step by step animation
    stepByStep = true;
    //if the sorting animation is not yet prepared
    if (!sortingAnimationPrepared) {
      //prepare the sorting animation
      prepareSortingAnimation();
    }
    //if the queue of animation for the sorting is not empty
    if (!transitionQueue.isEmpty()) {
      //play the animation in the head of the queue and remove it
      playNextAnimation();
    }
  }

  private void playNextAnimation() {
    //this ugly code is caused by javafx non permitting to change animation speed
    // at animation runtime. Also there isn't a unbind method on Transition
    // because ParallelTransition and SequentialTransition doesn't support it.
    // It's repeated on every other Transition subclasses.
    if (!transitionQueue.isEmpty()) {
      Transition transition = transitionQueue.remove();
      transition.play();
      if (transition instanceof ParallelTransition) {
        ParallelTransition parallelTransition = (ParallelTransition) transition;
        parallelTransition.getChildren().forEach(this::unbindAnimation);
      } else if (transition instanceof TranslateTransition) {
        unbindAnimation((TranslateTransition)transition);
      } else if (transition instanceof FadeTransition) {
        unbindAnimation((FadeTransition)transition);
      }
    }
  }

  private void unbindAnimation(Animation animation) {
    if (animation instanceof TranslateTransition) {
      unbindAnimation((TranslateTransition)animation);
    } else {
      unbindAnimation((FadeTransition)animation);
    }
  }

  private void unbindAnimation(TranslateTransition translateTransition) {
    translateTransition.durationProperty().unbind();
  }

  private void unbindAnimation(FadeTransition fadeTransition) {
    fadeTransition.durationProperty().unbind();
  }

  private void addNumberToArray(int number,int position) {
    //if the new number is greater than the max counter
    if (number > max) {
      //update the max counter
      max = number;
    }
    //create a new ArrayElementBox showing the generated random number
    ArrayElementBox arrayElementBox = new ArrayElementBox(number);
    //translate the arrayElementBox according to it's position in the array
    arrayElementBox.setTranslateX(position * boxTotalSize);
    //add the arrayElement box to the list of array boxes.
    arrayBoxesToSort.add(arrayElementBox);
    //add the fade transition to the total sequential transition
    transitionQueue.add(arrayElementBox.getCreationFadeTransition(stepTransitionEventHandler));
    //add the array box to the scene
    animationPane.getChildren().add(arrayElementBox);
  }

  private void initializeAnimationArea() {
    //clear animation pane from every possible past animations
    animationPane.getChildren().clear();
    //create a new list of stackPanes representing the array to sort
    arrayBoxesToSort = new ArrayList<>();
    transitionQueue.clear();
    max = 0;
    //set the step by step mode flag to false
    stepByStep = false;
    //set the sorting animation as not prepared
    sortingAnimationPrepared = false;
  }

  /**
   * Handler of generate button click.
   * Create and play an animation for showing the generation of a random array.
   */
  @FXML
  private void onGenerateArrayButtonClick() {
    //get array length requested from user
    int length = getArrayLength();
    //get maximum random value requested from user
    int randomMaxValue = getMaxRandomValue();
    initializeAnimationArea();
    //increase the animation pane width for containing the array
    animationPane.setPrefWidth(length * (boxTotalSize + 1));
    //create a new random number generator
    Random generator = new Random();
    //initialize maximum value counter to 0
    max = 0;
    //for a number of times equal to length value
    for (int i = 0; i < length; i++) {
      //generate a random number between 0 and randomMaxValue
      int number = generator.nextInt(randomMaxValue);
      addNumberToArray(number,i);
    }
    //play the first animation on the animation queue
    playNextAnimation();

  }

  @FXML
  private void onUserInputButtonClick() {
    initializeAnimationArea();
    boolean userTerminatedInput = false;
    int position=0;
    do {
      TextInputDialog dialog = new TextInputDialog("43");
      dialog.setTitle("Array values input");
      dialog.setHeaderText("Insert a value into the array. Press cancel for terminate");

      // Traditional way to get the response value.
      Optional<String> result = dialog.showAndWait();
      int value;
      if (result.isPresent()) {
        value = Integer.parseInt(result.get());
        addNumberToArray(value,position);
        position++;
        //play the first animation on the animation queue
        playNextAnimation();
      } else {
        userTerminatedInput = true;
      }
      } while (!userTerminatedInput);
  }

  @FXML
  private void onFileInputButtonClick(ActionEvent ae) {
    initializeAnimationArea();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open text file");
    fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
    Node source = (Node) ae.getSource();
    Window theStage = source.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(theStage);
    if (selectedFile!=null) {
      Charset charset = Charset.defaultCharset();
      String line = null;
      try (BufferedReader reader = Files.newBufferedReader(selectedFile.toPath(), charset)) {
        int i=0;
        while ((line = reader.readLine()) != null) {
          int number = Integer.parseInt(line);
          addNumberToArray(number,i);
          i++;
        }
        //play the first animation on the animation queue
        playNextAnimation();
      } catch (IOException x) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error opening the file");
        alert.showAndWait();
      } catch (NumberFormatException x) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error parsing the file");
        alert.setContentText("Cannot parse "+line+" to a number.");
        initializeAnimationArea();
        alert.showAndWait();
      }
    }
  }

  /**
   * Create the three main animation for the sorting phase.
   */
  private void prepareSortingAnimation() {
    //set a new width for the animation pane only if the new width is grater than the previous
    if ((max + 1) * boxTotalSize > animationPane.getPrefWidth()) {
      //set a new width equal to the maximum value in the array multiplied by the a box size
      animationPane.setPrefWidth((max + 1) * boxTotalSize);
    }
    //create a new hash map for representing temporary array. The keys are Integer and they will be from 0 to max.
    Map<Integer, ElementOfTemporaryArray> tmpArrayVisualization = new HashMap<>(max);
    //create animations for appearance temporary array
    initializeTemporaryArray(tmpArrayVisualization);
    //creates animations for moving array elements from the array to sort to the temporary array
    createFillTempArrayAnimation(tmpArrayVisualization);
    //create animations for moving back array elements from the temporary array to the array to sort, in a sorted way.
    createLastAnimationPhase(tmpArrayVisualization);
    //set the flag for know if the sorting animation has been prepared to true
    sortingAnimationPrepared = true;
  }

  /**
   * Create a temporary array with fade animation.
   *
   * @param tmpArrayVisualization Map<Integer, ElementOfTemporaryArray>: map between array to sort
   * values and elements with those values
   */
  private void initializeTemporaryArray(
      Map<Integer, ElementOfTemporaryArray> tmpArrayVisualization) {
    //for a number of times equal to the maximum value of the array to sort
    for (int i = 0; i <= max; i++) {
      //create empty rectangle + structure for handling array elements
      ElementOfTemporaryArray elementOfTemporaryArray = new ElementOfTemporaryArray(i);
      //add the element to the hash map
      tmpArrayVisualization.put(i, elementOfTemporaryArray);
      //add the fade animation to the total sort transition
      //pass as parameter also an event handler for step-by-step animation
      transitionQueue
          .add(elementOfTemporaryArray.getCreationFadeTransition(stepTransitionEventHandler));
      //add the element to the animation pane
      animationPane.getChildren().add(elementOfTemporaryArray);
    }
  }

  /**
   * Creates the animation for translating each array box from the array to sort to the tmp array.
   *
   * @param tmpArrayVisualization Map<Integer, ElementOfTemporaryArray>: map between array to sort
   * values and elements with those values
   */
  private void createFillTempArrayAnimation(
      Map<Integer, ElementOfTemporaryArray> tmpArrayVisualization) {
    //for each element of the array to sort
    for (ArrayElementBox arrayToSortElement : arrayBoxesToSort) {
      //get the element value
      Integer number = arrayToSortElement.getNumber();
      //clone element
      ArrayElementBox clonedElement = arrayToSortElement.clone();
      //get the ElementOfTemporaryArray from the map.
      ElementOfTemporaryArray elementOfTemporaryArray = tmpArrayVisualization.get(number);
      //add a the cloned element to the ElementOfTemporaryArray list. save the TranslateTransition generated.
      //pass as parameter also an event handler for step-by-step animation
      TranslateTransition translateTransition = elementOfTemporaryArray
          .add(clonedElement, stepTransitionEventHandler);
      //add the translate transition to the total sort transition
      transitionQueue.add(translateTransition);
      //add the cloned element to the scene
      animationPane.getChildren().add(clonedElement);
    }
  }

  /**
   * Create animation for the last animation phase where elements are moved from the temporary array
   * to the initial array in a sorted order.
   *
   * @param tmpArrayVisualization Map<Integer, ElementOfTemporaryArray>: map between array to sort
   * values and elements with those values
   */
  private void createLastAnimationPhase(
      Map<Integer, ElementOfTemporaryArray> tmpArrayVisualization) {
    //declare a counter for keep count of elements of the array to sort already sorted
    int arrayOffset = 0;
    //for each element of the temporary array
    for (ElementOfTemporaryArray elementOfTemporaryArray : tmpArrayVisualization.values()) {
      //get the number of elements in the list of the temporary array element
      int listLength = elementOfTemporaryArray.getListSize();
      //if the list in not empty (this if is for performance reason)
      if (listLength > 0) {
        //declare a parallel transition
        ParallelTransition parallelTransition;
        //parallel transition (fade+translate) generated from moving back the elements from tmp array to original array
        //pass as parameter also an event handler for step-by-step animation
        parallelTransition =
            elementOfTemporaryArray
                .getMoveBackAnimation(arrayBoxesToSort, arrayOffset, stepTransitionEventHandler);
        //add the parallel transition to the total sort animation
        transitionQueue.add(parallelTransition);
        //increase the array offset by a value equal to the number of elements moved back
        arrayOffset += listLength;
      }
    }
  }


  /**
   * Handler of sort button click.
   */
  @FXML
  private void OnSortArrayButtonClick() {
    //set step by step mode to false
    stepByStep = false;
    //if the sorting animation has not been prepared
    if (!sortingAnimationPrepared) {
      //prepare the sorting animation
      prepareSortingAnimation();
    }
    //if the queue of sort animation is not empty
    if (!transitionQueue.isEmpty()) {
      //remove and animation from the queue and play it
      playNextAnimation();
    }
  }

  /**
   * Method for initialization of the controller. It's called by the FXMLLoader.
   */
  @FXML
  public void initialize() {
    transitionQueue = new LinkedList<>();
    sortingAnimationPrepared = false;
    animationsDuration = new AnimationSpeed(animationSpeedSlider.valueProperty());
  }


  /**
   * Event handler for terminating an animation.
   */
  public class StepTransitionEventHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
      //if the queue is not empty and the user didn't request a step by step transition
      if (!transitionQueue.isEmpty() && !stepByStep) {
        //remove last animation and play it
        playNextAnimation();
      }
    }
  }

}
