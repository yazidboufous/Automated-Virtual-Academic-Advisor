package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Graph {
  private List<Node> nodes;

  public Graph() {
    this.nodes = new ArrayList<Node>();
  }

  public void addNode(Node node) {
    nodes.add(node);
  }

  public List<Node> getNodes() {
    return nodes;
  }

  public void DFS(Node node, Set<Node> visited) {

    visited.add(node);
    for (Node n : node.getNeighborNodes()) {

      if (!visited.contains(n)) {
        DFS(n, visited);
      }
    }
  }

  /**
   * This function traverses all nodes and calulcates their rechability.
   */
  public void computeReachability() {
    for (Node node : nodes) {
      Set<Node> visited = new HashSet<>();
      DFS(node, visited);
      node.setReachability(visited.size() - 1);
    }
  }

  /** Update level of a node based on the prereq/coreq relation */
  public void updateLevel(Edge neighbor, Node parent) {
    int current_level = neighbor.getNode().getLevel();

    // if co req
    if (neighbor.getWeight() == 0)
      neighbor.getNode().setLevel(Math.max(current_level, parent.getLevel()));
    // if pre req
    else
      neighbor.getNode().setLevel(Math.max(current_level, parent.getLevel() + 1));

  }

  // Organize nodes in a hashmap accoridng to the level they belong to
  public HashMap<Integer, List<Node>> computeLevelMap() {
    HashMap<Integer, List<Node>> map = new HashMap<>();
    for (Node node : getNodes()) {
      List<Node> nodesAtLevel = map.getOrDefault(node.getLevel(), new ArrayList<>());
      nodesAtLevel.add(node);
      map.put(node.getLevel(), nodesAtLevel);
    }
    return map;

  }

  /**
   * Levelize entire graph. This uses BFS over all nodes, and compute the levels.
   */
  public void levelizeGraph() {
    for (Node root : nodes) {
      Set<Node> visitedNodes = new HashSet<>();
      Queue<Node> queue = new LinkedList<>();
      queue.add(root);
      visitedNodes.add(root);

      while (!queue.isEmpty()) {
        Node currentNode = queue.poll();

        List<Edge> neighbors = currentNode.getNeighbors(); // get edges with node and weight
        // System.out.println(neighbors);
        for (Edge neighbor : neighbors) {
          if (!visitedNodes.contains(neighbor.getNode())) {
            queue.add(neighbor.getNode());
            visitedNodes.add(neighbor.getNode());
            updateLevel(neighbor, currentNode);

          }
        }
      }
    }
  }

  /**
   * This levelizes starting from a root node.
   * It only traverse nodes that are connected to the root.
   */
  public void levelizefromRoot(Node root) {
    Set<Node> visitedNodes = new HashSet<>();
    Queue<Node> queue = new LinkedList<>();
    queue.add(root);
    visitedNodes.add(root);

    while (!queue.isEmpty()) {
      Node currentNode = queue.poll();

      List<Edge> neighbors = currentNode.getNeighbors(); // get edges with node and weight
      // System.out.println(neighbors);
      for (Edge neighbor : neighbors) {
        if (!visitedNodes.contains(neighbor.getNode())) {
          queue.add(neighbor.getNode());
          visitedNodes.add(neighbor.getNode());
          updateLevel(neighbor, currentNode);

        }
      }
    }
  }

}
