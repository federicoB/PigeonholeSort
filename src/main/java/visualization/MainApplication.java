package visualization; /**
 * This file is part of PigeonholeSort
 * <p>
 * Created by Federico Bertani on 19/05/17.
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

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Project main application. TabPane layout.
 */
public class MainApplication extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    //load fxml from file
    TabPane root = FXMLLoader.load(getClass().getResource("/main.fxml"));
    //create a new scene with first child element the tabPane
    Scene scene = new Scene(root);
    //set window title
    primaryStage.setTitle("algorithm.PigeonholeSort sort");
    //set the stage scene
    primaryStage.setScene(scene);
    //show the stage
    primaryStage.show();
  }
}
