package algorithms;

import graph.Edge;
import graph.Graph;
import graph.Node;


import java.util.*;

public class DFS extends Algorithm {

    private Node root;

    public DFS(Graph graph, String root) {
        super(graph);
        this.root = graph.getNode(root);
    }

    public Set run() {
        Set<Node> visited = new LinkedHashSet<>();
        Stack<Node> toVisit = new Stack<>();
        toVisit.push(this.root);
        while(!toVisit.isEmpty()) {
            Node node = toVisit.pop();
            if (!visited.contains(node)) {
                visited.add(node);
                final ArrayList<Edge> edges = this.graph.getEdges().get(node);
                for (int i = edges.size() - 1; i >= 0; i--) { //traversing in reverse to get the same result as recursive DFS
                    final Edge e = edges.get(i);
                    Node child = e.getDest();
                    toVisit.push(child);
                }
            }
        }
        return visited;
    }
}
