package graph;

import java.util.ArrayList;

public class Node {
    private int id;
    private String name;
    private ArrayList<Edge> edges = new ArrayList<>();

    public Node(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addEdge(Edge e) {
        this.edges.add(e);
    }

}
