package game;

import algorithms.Node;

import java.util.ArrayList;

public class Solution {
    private boolean solved;
    private ArrayList<Node> nodes;
    private int moveCount;
    private long runTime;
    private int expandedNodes;

    public Solution(GameNode dest, long runTime, int expandedNodes) {
        this.runTime = runTime;
        this.expandedNodes = expandedNodes;
        if (dest != null) {
            solved = true;
            Node current = dest;
            moveCount = dest.getDepth();
            this.nodes = new ArrayList<>();
            this.nodes.add(current);
            while (current.getParent() != null) {
                this.nodes.add(0, current.getParent());
                current = current.getParent();
            }
        } else {
            solved = false;
        }
    }

    public void print() {
        if (solved) {
            for (Node n: nodes) {
                if (((GameNode) n).getMove() != null) {
                    System.out.print("->" + n.toString());
                } else {
                    System.out.print("Start");
                }
            }
            System.out.println("\nMove count: " + this.moveCount);
        } else {
            System.out.println("Couldn't find solution");
        }
        System.out.println("Runtime: " + this.runTime + "ms");
        System.out.println("Expanded nodes: " + this.expandedNodes);
    }


}
