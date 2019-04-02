package game;

import algorithms.Node;

import java.util.ArrayList;

public class Solution {
    private boolean solved;
    private ArrayList<Node> nodes;
    private int moveCount;
    private double runTime;
    private int expandedNodes;

    public Solution(GameNode dest, double runTime, int expandedNodes) {
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

    public ArrayList<Node> getNodes() {
        return this.nodes;
    }

    public String getResultString() {
        if (solved) {
            return "Move count: " + this.moveCount + "\n";
        } else {
            return "Couldn't find solution" + "\n";
        }
    }

    public String getRuntimeString() {
        return "Runtime: " + this.runTime + "ms" + "\n";
    }

    public String getExpandedNodesString() {
        return "Expanded nodes: " + this.expandedNodes + "\n";
    }

    public void print() {
        if (solved) {
            /*for (Node n: nodes) {
                if (((GameNode) n).getMove() != null) {
                    System.out.print("->" + n.toString());
                } else {
                    System.out.print("Start");
                }
            }*/
            System.out.print(";" + this.moveCount + ";");
        } else {
            System.out.print(";Couldn't find solution;");
        }
        System.out.println(this.runTime + "ms;" + this.expandedNodes);
    }


}
