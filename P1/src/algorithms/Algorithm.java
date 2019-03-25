package algorithms;

import graph.Graph;
import graph.Node;

public class Algorithm {
    private boolean debug = false;
    protected Node root;
    protected Graph graph;

    public Algorithm(Graph graph, String root) {
        this.graph = graph;
        this.root = graph.getNode(root);
    }
}
