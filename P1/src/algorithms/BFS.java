package algorithms;

import game.GameNode;
import game.Solution;
import graph.Edge;
import graph.Graph;
import graph.Node;
import sun.swing.BakedArrayList;

import java.lang.reflect.Array;
import java.util.*;

public class BFS extends Algorithm {

    private HashMap<GameNode, Integer> visited = new HashMap<>();

    public BFS(GameNode root) {
        super(root);
    }

    public void run() {
        this.startAlgo();
        Queue<GameNode> toVisit = new LinkedList<>();
        toVisit.add(this.root);
        while(!toVisit.isEmpty()) {
            GameNode node = toVisit.poll();
            visited.put(node, node.getDepth());
            this.expandedNodes++;
            for (GameNode child: node.getChildren()) {
                if (visited.getOrDefault(child, Integer.MAX_VALUE) >= child.getDepth()) {
                    child.setParent(node);
                    if (child.isSolution()) {
                        this.solution(child);
                        return;
                    }
                    else
                        toVisit.add(child);
                }
            }
        }
        this.solution(null);
    }
}
