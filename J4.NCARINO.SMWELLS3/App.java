import javafx.application.Application;
import java.io.IOException;

public class App {
  public static void main(String[] args) {
    System.out.println("v0.1");
    GraphADT<String, Double> graph = new DijkstraGraph<String,Double>();
    Backend back = new Backend(graph);
    try {
      back.loadGraphData("campus.dot");
    } catch (IOException e) {
      System.out.println("Campus.dot could not be found in this directory.");
    }
    Frontend.setBackend(back);
    Application.launch(Frontend.class, args);
  }
}
