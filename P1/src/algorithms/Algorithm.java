package algorithms;

import game.Element;
import game.GameNode;
import game.Map;
import game.Solution;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class Algorithm implements Runnable{
    GameNode root;
    boolean debug = false;
    int expandedNodes = 0;
    private Solution sol;
    private long startTime;

    Algorithm(GameNode root) { this.root = root; }

    void startAlgo() {
        this.startTime = System.nanoTime();
    }

    void solution(GameNode dest) {
        long endTime = System.nanoTime();
        double runTime = Math.floor( (endTime - startTime)/1000000 );
        this.sol = new Solution(dest, runTime, expandedNodes);
    }

    public void printSolution() {
        this.sol.print();
    }

    public Solution getSolution() {
        return sol;
    }

    static double heuristic(GameNode node, int i) {
        double p = 0;
        Map m = node.getMap();
        HashMap<Element.Color, Integer> colorMap = m.getColorMap();
        ArrayList<Element> robots = node.getRobots();
        ArrayList<Element> targets = m.getTargets();
        for (Element t: targets) {
            if (t != null) {
                int index = colorMap.get(t.getColor());
                Element robot = robots.get(index);
                int[][] heuristic = m.getHeuristicMatrix(i).get(index);
                p += heuristic[robot.getY()][robot.getX()];
            }
        }
        return p;
    }

}
