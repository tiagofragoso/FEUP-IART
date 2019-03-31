
package algorithms;

import game.Element;
import game.GameNode;
import game.Map;

import java.util.*;

public class AStar extends Algorithm {

    public AStar(GameNode root) {
        super(root);
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
                        double distanceToDest = calculateDistance(child);
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

    public static double calculateDistance(GameNode node){
        double p = 0;
        Map m = node.getMap();
        HashMap<Element.Color, Integer> colorMap = m.getColorMap();
        ArrayList<Element> robots = node.getRobots();
        ArrayList<Element> targets = m.getTargets();
        for (Element t: targets) {
            if (t != null) {
                Element robot = robots.get(colorMap.get(t.getColor()));
                p += ( (robot.getX() != t.getX())? 1: 0) + ( (robot.getY() != t.getY())? 1: 0);
            }
        }
        return p;
    }

}

