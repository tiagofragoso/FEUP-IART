package algorithms;

import game.GameNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Greedy extends Algorithm {

    private HashSet<GameNode> visited = new HashSet<>();

    private int heuristic;

    public Greedy(GameNode root, int heuristic) {
        super(root);
        this.heuristic = heuristic;
    }

    private GameNode greedy(GameNode current) {
        this.expandedNodes++;
        if (current.isSolution())
            return current;

        visited.add(current);
        ArrayList<GameNode> children = current.getChildren();
        for (GameNode child : children) {
            double distanceToDest = Algorithm.heuristic(child, heuristic);
            child.setTotalDistance(distanceToDest);
        }
        Collections.sort(children);

        GameNode sol;
        for (GameNode child : children) {
            if (!visited.contains(child)) {
                child.setParent(current);
                if ((sol = greedy(child)) != null) {
                    return sol;
                }
            }
        }
        return null;
    }

    public void run() {
        this.startAlgo();
        GameNode sol;
        try {
            if ((sol = greedy(root)) != null) {
                this.solution(sol);
                return;
            }
        } catch (StackOverflowError ignored) {}
        this.solution(null);
    }

}
