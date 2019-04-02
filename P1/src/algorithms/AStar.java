
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
        HashMap<GameNode, GameNode> pQueueNodes = new HashMap<>();
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
                        double totalDistance = current.getTotalDistance() + 10 + distanceToDest;
                        GameNode childRef;
                        if ((childRef = pQueueNodes.get(child)) != null) {
                            if (totalDistance < childRef.getTotalDistance()) {
                                System.out.println(childRef.getTotalDistance());
                                childRef.setTotalDistance(totalDistance);
                                childRef.setParent(current);
                                pQueue.remove(childRef);
                                pQueue.add(childRef);
                            }
                        } else {
                            child.setTotalDistance(totalDistance);
                            child.setParent(current);
                            pQueueNodes.put(child, child);
                            pQueue.add(child);
                        }

                    }
                }
            }
        }

        this.solution(null);
    }
}

