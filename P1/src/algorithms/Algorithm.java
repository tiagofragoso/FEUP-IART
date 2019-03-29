package algorithms;

import game.GameNode;
import graph.Graph;
import graph.Node;

import java.util.ArrayList;

public class Algorithm {
    private boolean debug = false;
    protected Node root;
    protected Graph graph;

    public Algorithm(Graph graph, String root) {
        this.graph = graph;
        this.root = graph.getNode(root);
    }

    protected static ArrayList<Node> solution(GameNode dest) {
        Node current = dest;
        ArrayList<Node> sol = new ArrayList<>();
        sol.add(current);
        while (current.getParent() != null) {
            sol.add(0, current.getParent());
            current = current.getParent();
        }
        return sol;
    }
}
