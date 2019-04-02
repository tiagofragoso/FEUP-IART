
package algorithms;

import game.GameNode;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStar extends Algorithm {

    private int heuristic;

    public AStar(GameNode root, int heuristic) {
        super(root);
        this.heuristic = heuristic;
    }

    public void run() {
        this.startAlgo();
        HashMap<GameNode, Double> solvedNodes = new HashMap<>();
        Queue<GameNode> pQueue = new PriorityQueue<>();

        this.root.setTotalDistance(Algorithm.heuristic(this.root, heuristic));
        solvedNodes.put(root, root.getTotalDistance());
        pQueue.add(this.root);

        while (!pQueue.isEmpty()) {
            GameNode current = pQueue.poll();
            this.expandedNodes++;

            if (current.isSolution()) {
                this.solution(current);
                return;
            }

            for (GameNode child : current.getChildren()) {
                double distanceToDest = Algorithm.heuristic(child, heuristic);
                double totalDistance = current.getTotalDistance() + 1 + distanceToDest;
                if (solvedNodes.getOrDefault(child, Double.MAX_VALUE) > totalDistance) {
                    child.setTotalDistance(totalDistance);
                    child.setParent(current);
                    solvedNodes.put(child, totalDistance);
                    pQueue.remove(child);
                    pQueue.add(child);
                }
            }
        }

        this.solution(null);
    }
}

