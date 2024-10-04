import java.util.List;
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
import javafx.application.Platform;
import java.io.IOException;
import java.util.NoSuchElementException;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class Frontend extends Application implements FrontendInterface {

  private static BackendInterface back;

  public static void setBackend(BackendInterface back) {
    try {
      back.loadGraphData("campus.dot");
    } catch (IOException e) {
      System.out.println("Campus.dot could not be found in this directory.");
    }
    Frontend.back = back;
  }

  public List<String> shortestPath;
  public List<Double> travelTimesOnPath;
  public boolean timeBoxChecked = false;
  
  public void start(Stage stage) {
    Pane root = new Pane();
    createAllControls(root);

    Scene scene = new Scene(root, 800, 600);
    stage.setScene(scene);
    stage.setTitle("P211");
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

    TextField srcText = new TextField();
    srcText.setLayoutX(200);
    srcText.setLayoutY(16);
    parent.getChildren().add(srcText);

    Label dst = new Label("Path End Selector: ");
    dst.setLayoutX(32);
    dst.setLayoutY(48);
    dst.setId("pathEnd");
    parent.getChildren().add(dst);

    TextField dstText = new TextField();
    dstText.setLayoutX(200);
    dstText.setLayoutY(48);
    parent.getChildren().add(dstText);

    Button find = new Button("Find Shortest Path");
    find.setLayoutX(32);
    find.setLayoutY(80);
    find.setId("findButton");
    // Event-handling
    find.addEventHandler(ActionEvent.ACTION, (event) -> {
      shortestPath = back.findShortestPath(srcText.getText(), dstText.getText());
      travelTimesOnPath = back.getTravelTimesOnPath(srcText.getText(), dstText.getText());
      createPathListDisplay(parent);
    });

    parent.getChildren().add(find);

  }

  public void createPathListDisplay(Pane parent) {
    parent.getChildren().removeIf(node -> node.getId() != null && (node.getId().equals("#pathWithoutTimes") || node.getId().equals("pathWithTimes")));
    String shortestPathString = "";
    if(shortestPath != null) {
      for (String temp : shortestPath) {
        shortestPathString += "\t" + temp + "\n";
      }
    }

    String shortestPathWithTimesString = "";
    if(shortestPath != null && !shortestPath.isEmpty()){
      shortestPathWithTimesString += "\t" + shortestPath.get(0) + "\n";
    }
    int counter = 1;
    if(travelTimesOnPath != null && !travelTimesOnPath.isEmpty()) {
      for(Double temp: travelTimesOnPath) {
        shortestPathWithTimesString += "\t-(" + temp + ") -> " + shortestPath.get(counter) + "\n";
        counter ++;
      }
    }
    
    Label path = new Label("Results List: \n" + shortestPathString);
    path.setLayoutX(32);
    path.setLayoutY(112);
    path.setId("#pathWithoutTimes");
    parent.getChildren().add(path);
    
    Label pathWithTimes = new Label("Results List (with walking times): \n" + shortestPathWithTimesString);
    pathWithTimes.setLayoutX(350);
    pathWithTimes.setLayoutY(112);
    pathWithTimes.setId("pathWithTimes");
    if(timeBoxChecked) {
      parent.getChildren().add(pathWithTimes);
    } else {
    parent.getChildren().remove(pathWithTimes);
    }
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
     parent.getChildren().removeIf(node -> node.getId() != null && (node.getId().equals("pathWithTimes")));
     timeBoxChecked = showTimesBox.isSelected();
     createPathListDisplay(parent);
   });

    parent.getChildren().add(showTimesBox);
  }

  public void createFurthestDestinationControls(Pane parent) {
    Label locationSelector = new Label("Location Selector:  ");
    locationSelector.setLayoutX(450);
    locationSelector.setLayoutY(16);
    locationSelector.setId("locationSelect");
    parent.getChildren().add(locationSelector);
    
    TextField furthestSrc = new TextField();
    furthestSrc.setLayoutX(575);
    furthestSrc.setLayoutY(16);
    parent.getChildren().add(furthestSrc);
    
    Button furthestFromButton = new Button("Find Most Distant Location");
    furthestFromButton.setLayoutX(450);
    furthestFromButton.setLayoutY(48);
    furthestFromButton.setId("furthestButton");
    parent.getChildren().add(furthestFromButton);
    
    //Label furthestFromLabel = new Label("Most Distance Location: ");
    //furthestFromLabel.setLayoutX(450);
    //furthestFromLabel.setLayoutY(80);
    //furthestFromLabel.setId("furthestFrom");
    //parent.getChildren().add(furthestFromLabel);
    
    Label furthestResult = new Label();
    furthestResult.setLayoutX(450);
    furthestResult.setLayoutY(85);
    furthestResult.setId("furthestResult");
    //Event-handler button
    parent.getChildren().add(furthestResult);

    furthestFromButton.setOnAction(event -> {
      try {
        String furthestLocation = back.getMostDistantLocation(furthestSrc.getText());
        furthestResult.setText("Most Distant Location: " + furthestLocation);
      } catch (NoSuchElementException e) {
          furthestResult.setText("Error: " + e.getMessage());
        }
    });

  }

  public void createAboutAndQuitControls(Pane parent) {

    Button quit = new Button("Quit");
    quit.setLayoutX(96);
    quit.setLayoutY(560);
    quit.setId("quitButton");
    parent.getChildren().add(quit);
    quit.addEventHandler(ActionEvent.ACTION, (event) ->Platform.exit());

    Button aboutButton = new Button("About");
    aboutButton.setLayoutX(170);
    aboutButton.setLayoutY(560);
    aboutButton.setId("aboutButton");
    parent.getChildren().add(aboutButton);
    aboutButton.addEventHandler(ActionEvent.ACTION, (event) -> {
      ScrollPane scroll = new ScrollPane();
      scroll.setFitToWidth(true);
      TextArea textArea = new TextArea();
      textArea.setEditable(false);
      textArea.setWrapText(true);
      List<String> locations = back.getListOfAllLocations();
      for(String location : locations) {
        textArea.appendText(location + "\n");
      }
      scroll.setContent(textArea);
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("About");
      alert.setHeaderText("All Possible Locations:");
      alert.getDialogPane().setContent(scroll);
      alert.showAndWait();
    });
  }
}
