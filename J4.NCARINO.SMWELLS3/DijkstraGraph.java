// === CS400 File Header Information ===
// Name: Sean Wells
// Email: smwells3@wisc.edu
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referenced by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new PlaceholderMap<>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        PriorityQueue<SearchNode> priorityQueue = new PriorityQueue<>();
        MapADT<NodeType, SearchNode> visitedNodes = new PlaceholderMap<>();
        if(this.containsNode(start) && this.containsNode(end)) {
          //start priorityQueue with outgoing edges from start node
         for (Edge leaving : this.nodes.get(start).edgesLeaving) {
           double cost = leaving.data.doubleValue();
           SearchNode newSearch = new SearchNode(leaving.successor, cost, null);
           priorityQueue.add(newSearch);
          }          
         //main loop of algo
         while (!priorityQueue.isEmpty()) {
           SearchNode pred = priorityQueue.poll();
           if (visitedNodes.containsKey(pred.node.data)) {
             continue; //we've already found a lower cost path to this node
           } else {
             visitedNodes.put(pred.node.data, pred);
             for (Edge leaving : pred.node.edgesLeaving) {
               double cost = leaving.data.doubleValue();
               SearchNode newSearch = new SearchNode(leaving.successor, pred.cost + cost, pred);
               priorityQueue.add(newSearch);
              }
            }
          }
         SearchNode endNode = visitedNodes.get(end);
         if (endNode == null) {
           throw new NoSuchElementException("No path exists between the provided start and end nodes.");
         } else {
           return endNode;
         }
        } else {
          throw new NoSuchElementException("The provided start or end node does not exist in this graph.");
        }
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shortest path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        SearchNode solution = computeShortestPath(start, end);
        List<NodeType> shortestData = new LinkedList<NodeType>();
        shortestData.add(this.nodes.get(end).data);
        SearchNode previous = solution.predecessor;
        while(previous != null) { // will repeat until the starting node (w/ pred == null) is found
          Node pred = previous.node;
          shortestData.add(0, pred.data);
          previous = previous.predecessor;
        }
        shortestData.add(0, this.nodes.get(start).data);
        return shortestData;
	}

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path from the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
      SearchNode solution = computeShortestPath(start, end);
        return solution.cost;
    }

}
