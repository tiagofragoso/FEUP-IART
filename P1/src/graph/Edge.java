package graph;

public class Edge {
    private Node srcNode;
    private Node destNode;
    private double value;

    public Edge(Node src, Node dest) {
        this.srcNode = src;
        this.destNode = dest;
        this.value = 1;
    }

    public Edge(Node src, Node dest, double value) {
        this.srcNode = src;
        this.destNode = dest;
        this.value = value;
    }

    public Node getSrc() {
        return this.srcNode;
    }

    public Node getDest() {
        return this.destNode;
    }

    public double getValue() {
        return value;
    }
}
