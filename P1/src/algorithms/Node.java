package algorithms;

import java.util.Objects;

public class Node implements Comparable<Node>{
    private double totalDistance = Double.POSITIVE_INFINITY;
    private int depth;
    private Node parent = null;
    private int x;
    private int y;

    public Node(int depth) { this.depth = depth; }

    public Node(int x, int y, int depth) {
        this.x = x;
        this.y = y;
        this.depth = depth;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public Node getParent() {
        return parent;
    }

    public int getDepth() { return depth; }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(Node o) {
        if (this.equals(o)) {
            return 0;
        } else {
            return (this.totalDistance < o.getTotalDistance())? -1 : 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x &&
                y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
