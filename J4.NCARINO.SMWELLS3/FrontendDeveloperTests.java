import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Node;



public class FrontendDeveloperTests extends ApplicationTest {



  @BeforeEach
  public void setup() throws Exception {

    ApplicationTest.launch(FrontendImplementation.class);
  }

  /**
   * Test if the shortest path controls are created correctly.
   */
  @Test
  public void testShortestPathControls() {
    FrontendImplementation frontend = new FrontendImplementation();
    // Simulate creation of controls
    Pane parent = new Pane();
    frontend.createShortestPathControls(parent);

    // Check that 2 controls are created: 2 labels,
    Assertions.assertEquals(parent.getChildren().size(), 2);

    // Check if controls are created at the correct positions
    Assertions.assertEquals(parent.getChildren().get(0).getLayoutX(), 32);
    Assertions.assertEquals(parent.getChildren().get(0).getLayoutY(), 16);
    Assertions.assertEquals(parent.getChildren().get(1).getLayoutX(), 32);
    Assertions.assertEquals(parent.getChildren().get(1).getLayoutY(), 48);


    // Check if labels contain the correct writing
    Label pathStart = lookup("#pathStart").query();
    Assertions.assertEquals("Path Start Selector: ", pathStart.getText());
    Label pathEnd = lookup("#pathEnd").query();
    Assertions.assertEquals("Path End Selector: ", pathEnd.getText());



  }

  /**
   * Test if additional feature controls are created correctly.
   */
  @Test
  public void testAdditionalFeatureControls() {
    FrontendImplementation frontend = new FrontendImplementation();
    // Simulate creation of controls
    Pane parent = new Pane();
    frontend.createAdditionalFeatureControls(parent);

    // Check if controls are created at the correct positions
    // TravelTimesBox
    Assertions.assertEquals(parent.getChildren().get(0).getLayoutX(), 200);
    Assertions.assertEquals(parent.getChildren().get(0).getLayoutY(), 80);
    // FurthestDestination controls: 4
    Assertions.assertEquals(parent.getChildren().get(1).getLayoutX(), 400);
    Assertions.assertEquals(parent.getChildren().get(1).getLayoutY(), 16);
    Assertions.assertEquals(parent.getChildren().get(2).getLayoutX(), 525);
    Assertions.assertEquals(parent.getChildren().get(2).getLayoutY(), 16);
    Assertions.assertEquals(parent.getChildren().get(3).getLayoutX(), 550);
    Assertions.assertEquals(parent.getChildren().get(3).getLayoutY(), 80);
    Assertions.assertEquals(parent.getChildren().get(4).getLayoutX(), 400);
    Assertions.assertEquals(parent.getChildren().get(4).getLayoutY(), 48);

    // Check if checkbox, labels and buttons have the correct writing
    // Checkbox
    CheckBox travelTimesBox = lookup("#timesBox").query();
    Assertions.assertEquals("Show Walking Times", travelTimesBox.getText());
    // Labels
    Label locationSelect = lookup("#locationSelect").query();
    Assertions.assertEquals("Location Selector:  ", locationSelect.getText());
    Label furthestFrom = lookup("#furthestFrom").query();
    Assertions.assertEquals("Most Distant Location: ", furthestFrom.getText());
    // Button
    Button furthestButton = lookup("#furthestButton").query();
    Assertions.assertEquals("Find Most Distant Location", furthestButton.getText());



  }

  /**
   * Test if about and quit controls are created correctly.
   */
  @Test
  public void testAboutAndQuitControl() {
    FrontendInterface frontend = new FrontendImplementation();
    // Simulate creation of controls
    Pane parent = new Pane();
    frontend.createAboutAndQuitControls(parent);

    Button quit = lookup("#quitButton").query();
    Button about = lookup("#aboutButton").query();
    Label aboutLabel = lookup("#aboutLabel").query();

    // Check if controls are created at the correct positions
    checkNodeLayout(quit, 96, 560);
    checkNodeLayout(about, 30, 560);
    checkNodeLayout(aboutLabel, 150, 540);

    // Check if controls have the correct text
    Assertions.assertEquals("Quit", quit.getText());
    Assertions.assertEquals("About", about.getText());
    Assertions.assertEquals("", aboutLabel.getText());

    // Simulate clicking aboutButton
    clickOn(about);

    // Check that about label updates
    Assertions.assertEquals(
        "This program allows you to find the shortest path between\n two locations on the UW Campus, display the path with times,\n and find the furthest location from any other location!",
        aboutLabel.getText());

    // Simulate clicking aboutButton again
    clickOn(about);

    // Check that about label updates
    Assertions.assertEquals("", aboutLabel.getText());
  }

  /**
   * Test if the path list display is created correctly.
   */
  @Test
  public void testPathListDisplay() {
    FrontendImplementation frontend = new FrontendImplementation();
    // Simulate creation of path list display
    Pane parent = new Pane();

    frontend.createPathListDisplay(parent);

    TextField dstText = lookup("#dstTextField").query();
    TextField srcText = lookup("#srcTextField").query();
    Label pathNoTime = lookup("#pathWithoutTimes").query();
    Label pathWithTime = lookup("#pathWithTimes").query();
    Button find = lookup("#findButton").query();

    // Check if controls are created in the correct position
    checkNodeLayout(dstText, 200, 48);
    checkNodeLayout(srcText, 200, 16);
    checkNodeLayout(pathNoTime, 32, 112);
    checkNodeLayout(pathWithTime, 300, 112);
    checkNodeLayout(find, 32, 80);

    // Check if controls labels and button are initialized with correct strings
    Assertions.assertEquals("", pathNoTime.getText());
    Assertions.assertEquals("", pathWithTime.getText());
    Assertions.assertEquals("Find Shortest Path", find.getText());



  }


  private void checkNodeLayout(Node node, int x, int y) {
    Assertions.assertEquals(node.getLayoutX(), x);
    Assertions.assertEquals(node.getLayoutY(), y);
  }



  // INTEGRATION TESTS

  @Test
  public void integrationTestCreatePathListDisplay() {

    Backend backend = new Backend(new DijkstraGraph<String, Double>());
    FrontendImplementation frontend = new FrontendImplementation();
    FrontendImplementation.setBackend(backend);
    try {
      backend.loadGraphData("campus.dot");

    } catch (IOException e) {
      System.out.println("Campus.dot could not be found in this directory.");
    }
    // Create a pane to simulate controls
    Pane parent = new Pane();
    frontend.createPathListDisplay(parent);

    // Create references to controls
    TextField dstText = lookup("#dstTextField").query();
    TextField srcText = lookup("#srcTextField").query();
    Label pathNoTime = lookup("#pathWithoutTimes").query();
    Label pathWithTime = lookup("#pathWithTimes").query();
    Button find = lookup("#findButton").query();

    // Simulate user to input Union South in srcText
    clickOn(srcText);
    write("Union South");
    // Simulate user to input Atmospheric, Oceanic and Space Sciences in dstText
    clickOn(dstText);
    write("Atmospheric, Oceanic and Space Sciences");

    // Simulate clicking Find Shortest Path Button
    clickOn(find);

    // check that pathNoTime updates
    String expectedResult =
        "Results List: \n\tUnion South\n\tAtmospheric, " + "Oceanic and Space Sciences\n";

    Assertions.assertEquals(expectedResult, pathNoTime.getText());

    // Simulate clicking on getTravelTimes box
    frontend.createTravelTimesBox(parent);
    CheckBox showTimesBox = lookup("#timesBox").query();
    clickOn(showTimesBox);

    // Simulate clicking Find Shortest Path Button again (to update pathWitTime label)
    clickOn(find);

    // Check that pathWithTime updates
    String expectedResultWithTime =
        "Results List (with walking times):\n\tUnion South\n\t-(182.20 seconds) -> Atmospheric, Oceanic and Space Sciences\n";
    Assertions.assertEquals(expectedResultWithTime, pathWithTime.getText());

    // Simulate unchecking (by clicking on) getTravelTimes box and then clicking on Find Shortest
    // Path button
    clickOn(showTimesBox);
    clickOn(find);

    // Check that pathWithTime updates
    Assertions.assertEquals("", pathWithTime.getText());


    // CHECK INPUT ERROR HANDLING
    // Simulate a user putting no text in srcText and invalid text in dstText
    clickOn(srcText);
    write(" ");
    clickOn(dstText);
    write("Invalid Location Example");

    // Simulate clicking Find Shortest Path button again
    clickOn(find);

    // Check that pathNoTime label is updated with error message and pathWithTime is cleared
    Assertions.assertEquals("Error: Please input two valid locations", pathNoTime.getText());
    Assertions.assertEquals("", pathWithTime.getText());



  }

  @Test
  public void integrationTestFurthestDest() {

    Backend backend = new Backend(new DijkstraGraph<String, Double>());
    FrontendImplementation frontend = new FrontendImplementation();
    FrontendImplementation.setBackend(backend);
    // Load in campus.dot
    try {
      backend.loadGraphData("campus.dot");

    } catch (IOException e) {
      System.out.println("Campus.dot could not be found in this directory.");
    }
    // Create a pane to simulate controls
    Pane parent = new Pane();
    frontend.createFurthestDestinationControls(parent);

    // Create references to controls
    TextField srcText = lookup("#furthestSrc").query();
    Button furthestButton = lookup("#furthestButton").query();
    Label furthestResult = lookup("#furthestResult").query();

    // Simulate user input "Union South" into srcText
    clickOn(srcText);
    write("Union South");

    // Simulate clicking furthestResult Button
    clickOn(furthestButton);

    // Check that correct output is stored in the text field of furthestResult label
    String expectedResult = "Smith Residence Hall";
    Assertions.assertEquals(expectedResult, furthestResult.getText());


  }

  @Test
  public void partnerTestFindShortestPath() {
    GraphADT<String, Double> graph = new DijkstraGraph<String, Double>();
    Backend back = new Backend(graph);
    try {
      back.loadGraphData("campus.dot");

    } catch (IOException e) {
      System.out.println("Campus.dot could not be found in this directory.");
    }
    List<String> path = back.findShortestPath("Union South", "Memorial Union");
    Assertions.assertEquals(path,
        Arrays.asList("Union South", "Computer Sciences and Statistics", "Meiklejohn House",
            "Chemistry Building", "Thomas C. Chamberlin Hall", "Lathrop Hall", "Law Building",
            "Music Hall", "Science Hall", "Memorial Union"));
    Assertions.assertEquals(new ArrayList<>(), back.findShortestPath("Invalid", " "));
  }

  /**
   * This method checks that we can calculate each individual travel time along a shortest path. We
   * are also checking that the Backend method will identify when a certain path doesn't exist.
   */
  @Test
  public void partnerTestGetTravelTimesOnPath() {
    GraphADT<String, Double> graph = new DijkstraGraph<String, Double>();
    Backend back = new Backend(graph);
    try {
      back.loadGraphData("campus.dot");

    } catch (IOException e) {
      System.out.println("Campus.dot could not be found in this directory.");
    }
    List<Double> travelTimes = back.getTravelTimesOnPath("Union South", "Memorial Union");
    Assertions.assertEquals(travelTimes, Arrays.asList(176.0, 164.20000000000002, 128.9, 199.1,
        192.3, 147.4, 157.3, 202.29999999999998, 105.8));
    Assertions.assertEquals(new ArrayList<>(), back.getTravelTimesOnPath("Invalid", " "));
  }
}


