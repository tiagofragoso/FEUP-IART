package graph;

import java.util.HashMap;
import java.util.ArrayList;

public class Graph {
    private HashMap<String, Node> nodes = new HashMap<>();
    private HashMap<Node, ArrayList<Edge>> edges = new HashMap<>();

    public HashMap<Node, ArrayList<Edge>> getEdges() {
        return edges;
    }

    public HashMap<String, Node> getNodes() {
        return nodes;
    }

    public Node addNode(String name) {
        final Node n = new Node(name);
        this.nodes.put(name, n);
        this.edges.put(n, new ArrayList<>());
        return n;
    }

    public Node getNode(String name) {
        return this.nodes.get(name);
    }

    public void addEdge(String src, String dest) {
        this.addEdge(src, dest, 1);
    }

    public void addEdge(String src, String dest, double value) {
        Node srcNode = nodes.get(src);
        Node destNode = nodes.get(dest);
        if (srcNode == null) {
            srcNode = this.addNode(src);
        }
        if (destNode == null) {
            destNode = this.addNode(dest);
        }
        this.edges.get(srcNode).add(new Edge(srcNode, destNode, value));
    }

    public void printEdges() {
        for (Node node : this.nodes.values()) {
            for (Edge edge : this.edges.get(node)) {
                System.out.println(edge.getSrc().getName() + " --> " + edge.getDest().getName());
            }
        }
    }
}
