// === CS400 File Header Information ===
// Name: Sean Wells
// Email: smwells3@wisc.edu
// Lecturer: Florian Heimerl
// Notes to Grader:
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
* This tester class aims to test the following four methods of a Backend
* type object: list all locations, find shortest path, get the travel times of a 
* specified path, and get the most distant location from a specified starting 
* location.
*/
public class BackendDeveloperTests extends ApplicationTest {

    @BeforeEach
    public void setup() throws Exception {
      ApplicationTest.launch(FrontendImplementation.class);
    }

    /**
     * This tester method ensures that a Backend object can access the location attribute to keep
     * record of all unique nodes in a graph. 
     */
    @Test
    public void testLocationListing(){
      GraphADT<String,Double> graph = new DijkstraGraph();
      Backend back = new Backend(graph);
      back.graph.insertNode("Union South");
      back.graph.insertNode("Computer Sciences and Statistics");
      back.graph.insertNode("Atmospheric, Oceanic and Space Sciences");
      back.graph.insertEdge("Union South", "Computer Sciences and Statistics", 176.0);
      back.graph.insertEdge("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences",127.2);
      //since the data is not loaded in traditionally locations is not updated as nodes are added
      back.locations = Arrays.asList("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
      List<String> allLocs = back.getListOfAllLocations();
      boolean pass = false;
      if(allLocs.contains("Union South") && allLocs.contains("Computer Sciences and Statistics") 
          && allLocs.contains("Atmospheric, Oceanic and Space Sciences")) { pass = true; }
      Assertions.assertEquals(pass, true);
    }

    /**
     * This tester method ensures that we can access the shortest path method of, in this case, the 
     * placeholder graph object. It also tests that an impossible path will generate an empty list.
     */
    @Test
    public void testShortestPath(){
      GraphADT<String,Double> graph = new DijkstraGraph();
      Backend back = new Backend(graph);
      back.graph.insertNode("Union South");
      back.graph.insertNode("Computer Sciences and Statistics");
      back.graph.insertNode("Atmospheric, Oceanic and Space Sciences");
      back.graph.insertEdge("Union South", "Computer Sciences and Statistics", 176.0);
      back.graph.insertEdge("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences", 127.2);
      List<String> path = back.findShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
      Assertions.assertEquals(path, Arrays.asList("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences"));
      Assertions.assertEquals(new ArrayList<>(), back.findShortestPath("Doesn't", "Exist"));
    }

     /**
      * This method checks that we can calculate each individual travel time along a shortest path.
      * We are also checking that the Backend method will identify when a certain path doesn't exist.
      */
    @Test
    public void testPathTravelTimes(){
      GraphADT<String,Double> graph = new DijkstraGraph();
      Backend back = new Backend(graph);
      back.graph.insertNode("Union South");
      back.graph.insertNode("Computer Sciences and Statistics");
      back.graph.insertNode("Atmospheric, Oceanic and Space Sciences");
      back.graph.insertEdge("Union South", "Computer Sciences and Statistics", 176.0);
      back.graph.insertEdge("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences", 127.2);
      List<Double> weights = back.getTravelTimesOnPath("Union South", "Atmospheric, Oceanic and Space Sciences");
      Assertions.assertEquals(weights, Arrays.asList(176.0, 127.2));
      Assertions.assertEquals(new ArrayList<>(), back.getTravelTimesOnPath("Doesn't", "Exist"));
    }

     /**
      * This method tests that we can obtain the correct furthest location in a graph given a specified
      * starting location. We also check to ensure that the correct exception is thrown when this node
      * does not exist on the graph.
      */
    @Test
    public void testMostDistantLoc() {
      GraphADT<String,Double> graph = new DijkstraGraph();
      Backend back = new Backend(graph);
      back.graph.insertNode("Union South");
      back.graph.insertNode("Computer Sciences and Statistics");
      back.graph.insertNode("Atmospheric, Oceanic and Space Sciences");
      back.graph.insertEdge("Union South", "Computer Sciences and Statistics", 176.0);
      back.graph.insertEdge("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences", 127.2);
      back.locations = Arrays.asList("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
      String furthestLoc = back.getMostDistantLocation("Union South");
      Assertions.assertNotEquals(furthestLoc, "Union South");
      Assertions.assertEquals(furthestLoc, "Atmospheric, Oceanic and Space Sciences");
      System.out.println(furthestLoc);
      boolean caught = false;
      try { back.getMostDistantLocation("Doesn't Exist"); } 
      catch (NoSuchElementException e) { caught = true; }
      Assertions.assertEquals(true, caught);
    }

    /**
    * This integration test method simply checks to make sure that given 
    * graph data can be loaded to a Backend object and subsequently passed
    * to the frontend without any exceptions being thrown. 
    */
    @Test
    public void testDataLoadIntegration() {
      FrontendImplementation front = new FrontendImplementation();
      GraphADT<String,Double> graph = new DijkstraGraph();
      Backend back = new Backend(graph);
      try {
        back.loadGraphData("campus.dot");
        front.setBackend(back);
        Assertions.assertEquals(true, true);
      } catch (IOException e) {
        Assertions.assertEquals(true, false);
      }
    }

    /**
    * This test method is a full integration test of the front and
    * backend using the P211 example from lecture. The ApplicationTest
    * class will be utilized to simulate user input and the results
    * will be compared with what we know. Specifically the path list
    * with and without walking times will be tested.
    */
    @Test
    public void fullIntegrationTest() {
      GraphADT<String,Double> lectureGraph = new DijkstraGraph<String, Double>();
      Backend back = new Backend(lectureGraph);
    
      back.locations = new ArrayList<>();
      back.locations.add("A");
      back.locations.add("B");
      back.locations.add("C");
      back.locations.add("D");
      back.locations.add("E");
      back.locations.add("F");
      back.locations.add("G");
      back.locations.add("H");
    
      lectureGraph.insertNode("A");
      lectureGraph.insertNode("B");
      lectureGraph.insertNode("C");
      lectureGraph.insertNode("D");
      lectureGraph.insertNode("E");
      lectureGraph.insertNode("F");
      lectureGraph.insertNode("G");
      lectureGraph.insertNode("H");
    
      lectureGraph.insertEdge("A", "B", 4.0);
      lectureGraph.insertEdge("A", "C", 2.0);
      lectureGraph.insertEdge("A", "E", 15.0);
      lectureGraph.insertEdge("B", "D", 1.0);
      lectureGraph.insertEdge("B", "E", 10.0);
      lectureGraph.insertEdge("C", "D", 5.0);
      lectureGraph.insertEdge("D", "F", 0.0);
      lectureGraph.insertEdge("D", "E", 3.0);
      lectureGraph.insertEdge("F", "D", 2.0);
      lectureGraph.insertEdge("F", "H", 4.0);
      lectureGraph.insertEdge("G", "H", 4.0);
      
      FrontendImplementation frontend = new FrontendImplementation();
      FrontendImplementation.setBackend(back);
      //create controls/input
      Pane parent = new Pane();
      frontend.createPathListDisplay(parent);
      TextField dstText = lookup("#dstTextField").query();
      TextField srcText = lookup("#srcTextField").query();
      Label pathWithout = lookup("#pathWithoutTimes").query();
      Label pathWith = lookup("#pathWithTimes").query();
      Button find = lookup("#findButton").query();
      //simulate user input src "A" and dst "H" without times
      clickOn(srcText);
      write("A");
      clickOn(dstText);
      write("H");
      clickOn(find);
      String expected = "Results List: \n\tA\n\tB\n\tD\n\tF\n\tH\n";
      Assertions.assertEquals(expected, pathWithout.getText());
      //check that times can also be provided
      frontend.createTravelTimesBox(parent);
      CheckBox showTimes = lookup("#timesBox").query();
      clickOn(showTimes);
      clickOn(find);
      String expectedTimes = "Results List (with walking times):\n\tA\n\t-(4.00 seconds) -> B\n";
      Assertions.assertEquals(true, pathWith.getText().startsWith(expectedTimes));
    }

    /**
     * This partner test checks that the correct message is outputted
     * when one the proposed shortest path is not possible.
     */
    @Test
    public void partnerTestInvalid() {
      GraphADT<String,Double> graph = new DijkstraGraph<String, Double>();
      Backend back = new Backend(graph);
      back.locations = new ArrayList<>();
      back.locations.add("A");
      back.locations.add("B");
      back.locations.add("C");
      graph.insertNode("A");
      graph.insertNode("B");
      graph.insertNode("C");
      graph.insertEdge("A", "B", 4.0);
      FrontendImplementation frontend = new FrontendImplementation();
      FrontendImplementation.setBackend(back);
      Pane parent = new Pane();
      frontend.createAboutAndQuitControls(parent);
      frontend.createPathListDisplay(parent);
      TextField dstText = lookup("#dstTextField").query();
      TextField srcText = lookup("#srcTextField").query();
      Label pathNoTime = lookup("#pathWithoutTimes").query();
      Button find = lookup("#findButton").query();
      clickOn(srcText);
      write("A");
      clickOn(dstText);
      write("C");
      clickOn(find);
      Assertions.assertEquals(pathNoTime.getText(), "Error: Please input two valid locations");
    }

    /**
     * This partner test checks that all buttons, checkbox, labels,
     * textfields, etc., can be found and accessed upon initialization.
     * This test will fail if any one of these ids cannot be found.
     */
    @Test
    public void partnerTestAllFieldsFound() {
      FrontendInterface frontend = new FrontendImplementation();
      Pane parent = new Pane();
      frontend.createAllControls(parent);
      lookup("#pathStart");
      lookup("#pathEnd");
      lookup("#dstTextField");
      lookup("#srcTextField");
      lookup("#pathWithoutTimes");
      lookup("#pathWithTimes");
      lookup("#findButton");
      lookup("#timesBox");
      lookup("#locationSelect");
      lookup("#furthestSrc");
      lookup("#furthestResult");
      lookup("#furthestButton");
      lookup("#furthestFrom");
      lookup("#aboutLabel");
      lookup("#aboutButton");
      lookup("#quitButton");
    }

}
