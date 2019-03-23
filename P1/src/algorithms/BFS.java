package algorithms;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BFS extends Algorithm {

    private Node root;

    public BFS(Graph graph, String root) {
        super(graph);
        this.root = graph.getNode(root);
    }

    public Set run() {
        Set<Node> visited = new LinkedHashSet<>();
        Queue<Node> toVisit = new LinkedList<>();
        toVisit.add(this.root);
        while(!toVisit.isEmpty()) {
            Node node = toVisit.poll();
            visited.add(node);
            for (Edge e: this.graph.getEdges().get(node)) {
                Node child = e.getDest();
                if (!visited.contains(child)) {
                    toVisit.add(child);
                }
            }
        }
        return visited;
    }
}
