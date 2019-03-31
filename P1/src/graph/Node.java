package graph;

import java.util.Objects;

public class Node implements Comparable<Node>{
    private String name;
    private double totalDistance = Double.POSITIVE_INFINITY;
    private int depth;
    private Node parent = null;

    public Node(String name) {
        this.name = name;
    }
    public Node(int depth) { this.depth = depth; }

    public String getName() {
        return name;
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
    public String toString() {
        return this.name;
    }

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name);
    }*/

   /* @Override
    public int hashCode() {
        return Objects.hash(name);
    }*/
}
