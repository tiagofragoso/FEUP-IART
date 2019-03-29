package algorithms;

import game.GameNode;
import graph.Edge;
import graph.Graph;
import graph.Node;
import sun.swing.BakedArrayList;

import java.lang.reflect.Array;
import java.util.*;

public class BFS extends Algorithm {

    public BFS(Graph graph, String root) {
        super(graph, root);
    }

    public static ArrayList<Node> run(GameNode root) {
        Queue<GameNode> toVisit = new LinkedList<>();
        toVisit.add(root);
        while(!toVisit.isEmpty()) {
            GameNode node = toVisit.poll();
            for (GameNode child: node.getChildren()) {
                child.setParent(node);
                if (child.isSolution())
                    return Algorithm.solution(child);
                else
                    toVisit.add(child);
            }
        }
        return null;
    }
}
