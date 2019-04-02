package algorithms;

import game.GameNode;

import java.util.ArrayList;
import java.util.HashMap;

public class IDDFS extends Algorithm {

    private static int MAX_DEPTH = 25;

    public IDDFS(GameNode root) {
        super(root);
    }

    private HashMap<GameNode, Integer> visited = new HashMap<>();

    private GameNode dls(GameNode current, int depth) {
        this.expandedNodes++;
        if (current.isSolution())
            return current;

        if (depth <= 0)
            return null;

        visited.put(current, depth);

        ArrayList<GameNode> children = current.getChildren();
        GameNode sol;
        for (GameNode child: children) {
            if (!(visited.getOrDefault(child, -1) >= depth) && !(minMoves(child) > depth)) {
                child.setParent(current);
                if ((sol = dls(child, depth-1)) != null) {
                    return sol;
                }
            }
        }
        return null;
    }

    public void run() {
        this.startAlgo();
        GameNode sol;
        for (int depth = 1; depth <= MAX_DEPTH; depth++) {
            if ((sol = dls(root, depth)) != null) {
               this.solution(sol);
               return;
            }
        }
        this.solution(null);
    }

    private int minMoves(GameNode n) {
        int index = n.getMap().getColorMap().get(n.getMove().getColor());
        if (n.getMap().getTargets().get(index) != null)
            return n.getMap().getHeuristicMatrix(2).get(index)[n.getMove().getY()][n.getMove().getX()];
        else
            return -1;
    }
}
