package algorithms;

import game.GameNode;

import java.util.ArrayList;
import java.util.HashMap;

public class IDDFS extends Algorithm {

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
            if (!(visited.getOrDefault(child, -1) >= depth)) {
                child.setParent(current);
                if ((sol = dls(child, depth-1)) != null) {
                    return sol;
                }
            }
        }
        return null;
    }

    public void run(int maxDepth) {
        this.startAlgo();
        GameNode sol;
        for (int depth = 1; depth <= maxDepth; depth++) {
            if (debug) System.out.println("Running depth=" + depth);
            if ((sol = dls(root, depth)) != null) {
               this.solution(sol);
               return;
            }
        }
        this.solution(null);
    }
}
