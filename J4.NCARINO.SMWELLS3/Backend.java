// === CS400 File Header Information ===
// Name: Sean Wells
// Email: smwells3@wisc.edu
// Lecturer: Florian Heimerl
// Notes to Grader:
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the Backend half of a project that utilizes a graph of various locations represented using
 * nodes and edges. This half can perform various calculations and actions such as loading graph data,
 * calculating the shortest paths between two locations, finding the most distant location from one
 * starting location, etc.
 */
public class Backend implements BackendInterface {
  
  /**
   * GraphADT<String, Double> object representing this network of nodes and edges.
   */
  public GraphADT<String, Double> graph;
  
  /**
   * List of all unique locations on graph.
   */
  public List<String> locations = new ArrayList<>();

  /**
  * Constructor for the backend class, assigns a GraphADT object to manage node and edge information.
  */
  public Backend(GraphADT<String, Double> graph) {
    this.graph = graph;
  }

  /**
   * Loads graph data from a dot file.
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was a problem reading in the specified file
   */
  @Override
  public void loadGraphData(String filename) throws IOException {
    try {
      List<String> lines = Files.readAllLines(Paths.get("campus.dot"));
      Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*->\\s*\"([^\"]+)\"\\s*\\[seconds=(\\d+\\.?\\d*)\\];");
      for (String line : lines) {
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
          String source = matcher.group(1);
          String destination = matcher.group(2);
          double weight = Double.parseDouble(matcher.group(3));
          if(!graph.containsNode(source)) { graph.insertNode(source); }
          if(!locations.contains(source)) { locations.add(source); }
          if(!graph.containsNode(destination)) {graph.insertNode(destination);}
          if(!locations.contains(destination)) { locations.add(destination); }
          if(!graph.containsEdge(source, destination)) {
            graph.insertEdge(source, destination, weight);
          }
        }
      }
    } catch (IOException e) {
      System.out.println("The provided file could not be found.");
    }
  }

  /**
   * Returns a list of all locations (nodes) available on the backend's graph.
   * @return list of all location names
   */
  @Override
  public List<String> getListOfAllLocations() {
    return locations;
  }

  /**
   * Return the sequence of locations along the shortest path from startLocation to endLocation, or
   * an empty list if no such path exists.
   * @param startLocation the start location of the path
   * @param endLocation the end location of the path
   * @return a list with the nodes along the shortest path from startLocation to endLocation, or
   *         an empty list if no such path exists
   */
  @Override
  public List<String> findShortestPath(String startLocation, String endLocation) {
    try {
      if(graph.containsNode(endLocation) && graph.containsNode(startLocation)) {
        return graph.shortestPathData(startLocation, endLocation);
      } else { return new ArrayList<>(); }
    } catch (NoSuchElementException e) { return new ArrayList<>(); }
  }
  
  /**
   * Return the walking times in seconds between each two nodes on the shortest path from startLocation
   * to endLocation, or an empty list of no such path exists.
   * @param startLocation the start location of the path
   * @param endLocation the end location of the path
   * @return a list with the walking times in seconds between two nodes along the shortest path from
   *         startLocation to endLocation, or an empty list if no such path exists
   */
  @Override
  public List<Double> getTravelTimesOnPath(String startLocation, String endLocation) {
    List<String> path = findShortestPath(startLocation, endLocation);
    List<Double> pathWeights = new ArrayList<>();
    if(!path.isEmpty()) {
      for (int i = 0; i < path.size()-1; i++) {
        pathWeights.add(graph.getEdge(path.get(i), path.get(i+1)));
      }
      return pathWeights;
    } else { return new ArrayList<>(); }
  }

  /**
   * Return the most distant location from startLocation that is reachable in the graph.
   * @param startLocation the location to find the most distant location for
   * @return the location that is most distant (has the longest overall walking time)
   * @throws NoSuchElementException if startLocation does not exist
   */
  @Override
  public String getMostDistantLocation(String startLocation) throws NoSuchElementException {
    if(graph.containsNode(startLocation)) {
      String furthestLoc = "";
      Double furthestWeight = 0.0;
      for (String location : locations) {
        if(graph.containsNode(location)) {
          if(!findShortestPath(startLocation, location).equals(new ArrayList<>())) {
            if(graph.shortestPathCost(startLocation, location) > furthestWeight) {
              furthestWeight = graph.shortestPathCost(startLocation, location);
              furthestLoc = location;
            }
          }
        }
      }
//      graph.shortestPathData(startLocation, furthestLoc);
      return furthestLoc;
    } else { throw new NoSuchElementException("The provided location does not exist on the graph."); }
  }

}
