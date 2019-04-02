
package algorithms;

import game.GameNode;

import java.util.*;

public class AStar extends Algorithm {

    private int heuristic;

    public AStar(GameNode root, int heuristic) {
        super(root);
        this.heuristic = heuristic;
    }

    public void run() {
        this.startAlgo();
        Set<GameNode> solvedNodes = new LinkedHashSet<>();
        Queue<GameNode> pQueue = new PriorityQueue<>();

        this.root.setTotalDistance(0);
        pQueue.add(this.root);

        while (!pQueue.isEmpty()) {
            GameNode current = pQueue.poll();
            this.expandedNodes++;
            if (!solvedNodes.contains(current)) {
                solvedNodes.add(current);

                if (current.isSolution()) {
                    this.solution(current);
                    return;
                }

                for (GameNode child : current.getChildren()) {
                    if (!solvedNodes.contains(child)) {
                        double distanceToDest = Algorithm.heuristic(child, heuristic);
                        double totalDistance = current.getTotalDistance() + 1 + distanceToDest;
                        if (totalDistance < child.getTotalDistance()) {
                            child.setTotalDistance(totalDistance);
                            child.setParent(current);
                            pQueue.add(child);
                        }
                    }
                }
            }
        }

        this.solution(null);
    }
}

