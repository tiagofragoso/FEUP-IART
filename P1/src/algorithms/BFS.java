package algorithms;

import game.GameNode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BFS extends Algorithm {

    private HashSet<GameNode> visited = new HashSet<>();

    public BFS(GameNode root) {
        super(root);
    }

    public void run() {
        this.startAlgo();
        Queue<GameNode> toVisit = new LinkedList<>();
        toVisit.add(this.root);
        while(!toVisit.isEmpty()) {
            GameNode node = toVisit.poll();
            visited.add(node);
            this.expandedNodes++;
            for (GameNode child: node.getChildren()) {
                if (!visited.contains(child)) {
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
