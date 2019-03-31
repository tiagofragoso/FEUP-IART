package algorithms;

import game.GameNode;
import game.Solution;


public class Algorithm {
    GameNode root;
    boolean debug = false;
    int expandedNodes = 0;
    private Solution sol;
    private long startTime;
    private long endTime;

    Algorithm(GameNode root) { this.root = root; }

    void startAlgo() {
        this.startTime = System.currentTimeMillis();
    }

    void solution(GameNode dest) {
        this.endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        this.sol = new Solution(dest, runTime, expandedNodes);
    }

    public void printSolution() {
        this.sol.print();
    }
}
