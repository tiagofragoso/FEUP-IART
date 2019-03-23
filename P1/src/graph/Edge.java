package graph;

public class Edge {
    private Node srcNode;
    private Node destNode;
    private double value;

    public Edge(Node src, Node dest) {
        this.srcNode = src;
        this.destNode = dest;
    }

    public Node getSrc() {
        return this.srcNode;
    }

    public Node getDest() {
        return this.destNode;
    }
}
