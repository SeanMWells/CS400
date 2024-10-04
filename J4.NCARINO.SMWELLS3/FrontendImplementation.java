import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
// import javafx.application.Platform;

public class FrontendImplementation extends Application implements FrontendInterface {

  private static Backend back;

  public static void setBackend(Backend back) {
    FrontendImplementation.back = back;
  }

  // private String[] sourceAndDestStrings = new String[2];
  public boolean timeBoxChecked = false;
  List<String> shortestPath;
  List<Double> travelTimesOnPath;

  public void start(Stage stage) {
    Pane root = new Pane();

    createAllControls(root);

    Scene scene = new Scene(root, 800, 600);
    stage.setScene(scene);
    stage.setTitle("P213");
    stage.show();
  }

  public void createAllControls(Pane parent) {
    createShortestPathControls(parent);
    createPathListDisplay(parent);
    createAdditionalFeatureControls(parent);
    createAboutAndQuitControls(parent);
  }



  public void createShortestPathControls(Pane parent) {
    Label src = new Label("Path Start Selector: ");
    src.setLayoutX(32);
    src.setLayoutY(16);
    src.setId("pathStart");
    parent.getChildren().add(src);

    Label dst = new Label("Path End Selector: ");
    dst.setLayoutX(32);
    dst.setLayoutY(48);
    dst.setId("pathEnd");
    parent.getChildren().add(dst);

  }



  public void createPathListDisplay(Pane parent) {
    TextField dstText = new TextField();
    dstText.setLayoutX(200);
    dstText.setLayoutY(48);
    dstText.setId("dstTextField");
    parent.getChildren().add(dstText);


    TextField srcText = new TextField();
    srcText.setLayoutX(200);
    srcText.setLayoutY(16);
    srcText.setId("srcTextField");
    parent.getChildren().add(srcText);



    // Create controls, set layout and ids, add to pane
    Label pathNoTime = new Label("");
    pathNoTime.setLayoutX(32);
    pathNoTime.setLayoutY(112);
    pathNoTime.setId("pathWithoutTimes");
    parent.getChildren().add(pathNoTime);

    Label pathWithTimes = new Label("");
    pathWithTimes.setLayoutX(300);
    pathWithTimes.setLayoutY(112);
    pathWithTimes.setId("pathWithTimes");
    parent.getChildren().add(pathWithTimes);

    Button find = new Button("Find Shortest Path");
    find.setLayoutX(32);
    find.setLayoutY(80);
    find.setId("findButton");
    parent.getChildren().add(find);

    find.addEventHandler(ActionEvent.ACTION, (event) -> {
      try {
      if (srcText.getText() != null && !srcText.getText().equals("") && dstText.getText() != null
          && !dstText.getText().equals("")) {
        shortestPath = back.findShortestPath(srcText.getText(), dstText.getText());

        travelTimesOnPath = back.getTravelTimesOnPath(srcText.getText(), dstText.getText());
        if(shortestPath.isEmpty() || travelTimesOnPath.isEmpty()) {
          throw new NoSuchElementException("Error: Please input two valid locations");
        }
        
        String[] results = getPathsResultString();
        String shortestPathString = results[0];
        String shortestPathWithTimesString = results[1];

        pathNoTime.setText("Results List: \n" + shortestPathString);

        if (timeBoxChecked) {
          pathWithTimes
              .setText("Results List (with walking times):\n" + shortestPathWithTimesString);
        } else {
          pathWithTimes.setText("");
        }
      } else {
        throw new NoSuchElementException("Error: Please input two valid locations");
      }
      }catch (NoSuchElementException e) {
        pathNoTime.setText(e.getMessage());
	pathWithTimes.setText("");
      }
    });

  }

  private String[] getPathsResultString() {
    String[] returnArray = new String[2];
    String shortestPathString = "";
    if (shortestPath != null) {
      for (String temp : shortestPath) {
        shortestPathString += "\t" + temp + "\n";
      }
    }

    String shortestPathWithTimesString = "";

    if (shortestPath != null && !shortestPath.isEmpty()) {
      shortestPathWithTimesString += "\t" + shortestPath.get(0) + "\n";
    }
    int counter = 1;
    if (travelTimesOnPath != null && !travelTimesOnPath.isEmpty()) {
      for (Double temp : travelTimesOnPath) {
        String formattedTime = String.format("%.2f", temp);
        shortestPathWithTimesString +=
            "\t-(" + formattedTime + " seconds) -> " + shortestPath.get(counter) + "\n";
        counter++;
      }
    }
    returnArray[0] = shortestPathString;
    returnArray[1] = shortestPathWithTimesString;
    return returnArray;
  }


  public void createAdditionalFeatureControls(Pane parent) {
    this.createTravelTimesBox(parent);
    this.createFurthestDestinationControls(parent);
  }

  public void createTravelTimesBox(Pane parent) {
    CheckBox showTimesBox = new CheckBox("Show Walking Times");
    showTimesBox.setLayoutX(200);
    showTimesBox.setLayoutY(80);
    showTimesBox.setId("timesBox");
    // Event handler:
    showTimesBox.addEventHandler(ActionEvent.ACTION, (event) -> {
      timeBoxChecked = showTimesBox.isSelected();
    });

    parent.getChildren().add(showTimesBox);
  }

  public void createFurthestDestinationControls(Pane parent) {
    Label locationSelector = new Label("Location Selector:  ");
    locationSelector.setLayoutX(400);
    locationSelector.setLayoutY(16);
    locationSelector.setId("locationSelect");
    parent.getChildren().add(locationSelector);

    TextField furthestSrc = new TextField();
    furthestSrc.setLayoutX(525);
    furthestSrc.setLayoutY(16);
    furthestSrc.setId("furthestSrc");
    parent.getChildren().add(furthestSrc);

    Label furthestResult = new Label("");
    furthestResult.setLayoutX(550);
    furthestResult.setLayoutY(80);
    furthestResult.setId("furthestResult");
    parent.getChildren().add(furthestResult);

    Button furthestFromButton = new Button("Find Most Distant Location");
    furthestFromButton.setLayoutX(400);
    furthestFromButton.setLayoutY(48);
    furthestFromButton.setId("furthestButton");
    parent.getChildren().add(furthestFromButton);
    // Event-handler button
    furthestFromButton.addEventHandler(ActionEvent.ACTION, (event) -> {
      try {
      if(furthestSrc.getText().equals("") || furthestSrc.getText() == null) {
        throw new IOException("Error: Please input valid location");
      }
      //Might throw nossuch element exception
      furthestResult.setText(back.getMostDistantLocation(furthestSrc.getText()));

      }catch (IOException e) {
        furthestResult.setText(e.getMessage());
      }
    });

    Label furthestFromLabel = new Label("Most Distant Location: ");
    furthestFromLabel.setLayoutX(400);
    furthestFromLabel.setLayoutY(80);
    furthestFromLabel.setId("furthestFrom");
    parent.getChildren().add(furthestFromLabel);


  }


  public void createAboutAndQuitControls(Pane parent) {
    Label aboutLabel = new Label("");
    aboutLabel.setLayoutX(150);
    aboutLabel.setLayoutY(540);
    aboutLabel.setId("aboutLabel");
    parent.getChildren().add(aboutLabel);

    Button about = new Button("About");
    about.setLayoutX(30);
    about.setLayoutY(560);
    about.setId("aboutButton");
    parent.getChildren().add(about);
    about.setOnAction(actionEvent -> {
      if (aboutLabel.getText().equals("")) {

        aboutLabel.setText(
            "This program allows you to find the shortest path between\n two locations on the UW Campus, display the path with times,\n and find the furthest location from any other location!");
      } else {

        aboutLabel.setText("");
      }
    });

    Button quit = new Button("Quit");
    quit.setLayoutX(96);
    quit.setLayoutY(560);
    quit.setId("quitButton");
    parent.getChildren().add(quit);
    quit.addEventHandler(ActionEvent.ACTION, (event) -> Platform.exit());
  }
}
