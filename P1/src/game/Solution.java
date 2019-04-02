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

    public String getPrint(){
        String returnStatement = "";
        if (solved) {
            for (Node n: nodes) {
                if (((GameNode) n).getMove() != null) {
                    returnStatement = returnStatement + ("->" + n.toString());
                } else {
                    returnStatement = returnStatement + ("Start");
                }
            }
            returnStatement = returnStatement + ("\nMove count: " + this.moveCount + "\n");
        } else {
            returnStatement = returnStatement + ("Couldn't find solution" + "\n");
        }
        returnStatement = returnStatement + ("Runtime: " + this.runTime + "ms" + "\n");
        returnStatement = returnStatement + ("Expanded nodes: " + this.expandedNodes + "\n");

        return returnStatement;
    }

    public String getTextAreaInfo() {
        String returnStatement = "";
        if (solved) {
            returnStatement = returnStatement + ("\nMove count: " + this.moveCount + "\n");
        } else {
            returnStatement = returnStatement + ("Couldn't find solution" + "\n");
        }
        returnStatement = returnStatement + ("Runtime: " + this.runTime + "ms" + "\n");
        returnStatement = returnStatement + ("Expanded nodes: " + this.expandedNodes + "\n");

        return returnStatement;
    }

    public void print() {
        System.out.print(this.getPrint());
    }


}
