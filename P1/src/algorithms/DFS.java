package algorithms;

import game.GameNode;
import graph.Graph;
import graph.Node;

import java.util.ArrayList;

public class DFS extends Algorithm {

    public DFS(Graph graph, String root) {
        super(graph, root);
    }

    private static ArrayList<Node> dls(GameNode current, int depth) {
        if (current.isSolution())
            return Algorithm.solution(current);

        if (depth <= 0)
            return null;

        ArrayList<GameNode> children = current.getChildren();
        ArrayList<Node> sol;
        for (GameNode child: children) {
            child.setParent(current);
            if ((sol = dls(child, depth-1)) != null) {
                return sol;
            }
        }
        return null;
    }

    public static ArrayList<Node> run(GameNode root, int maxDepth) {
        ArrayList<Node> sol;
        for (int depth = 0; depth <= maxDepth; depth++) {
            if ((sol = dls(root, depth)) != null) {
                return sol;
            }
        }
        return null;
    }
}
