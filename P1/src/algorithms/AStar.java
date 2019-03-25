package algorithms;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.lang.reflect.Array;
import java.util.*;

public class AStar extends Algorithm {

    private Node destNode;

    public AStar(Graph graph, String root) {
        super(graph, root);
    }

    public ArrayList run(String dest) {
        this.destNode = this.graph.getNode(dest);
        Set<Node> solvedNodes = new LinkedHashSet<>();
        Queue<Node> pQueue = new PriorityQueue<>();

        this.root.setTotalDistance(0);
        pQueue.add(this.root);

        while (!pQueue.isEmpty()) {
            Node current = pQueue.poll();

            if (!solvedNodes.contains(current)) {
                solvedNodes.add(current);

                if (current.equals(destNode)) {
                    return solution();
                }

                for (Edge e : this.graph.getEdges().get(current)) {
                    Node child = e.getDest();

                    if (!solvedNodes.contains(child)) {
                        double distanceToDest = calculateDistance(child);
                        double totalDistance = current.getTotalDistance() + e.getValue() + distanceToDest;
                        if (totalDistance < child.getTotalDistance()) {
                            child.setTotalDistance(totalDistance);
                            child.setParent(current);
                            pQueue.add(child);
                        }
                    }
                }
            }
        }

        return new ArrayList();
    }

    private ArrayList<Node> solution() {
        Node current = this.destNode;
        ArrayList<Node> sol = new ArrayList<>();
        sol.add(current);
        while (current != this.root) {
            sol.add(current.getParent());
            current = current.getParent();
        }
        Collections.reverse(sol);
        return sol;
    }

    private static final HashMap<String, Double> testMap = new HashMap<String,Double>(){
        {
        put("Arad", 366.0);
        put("Bucharest", 0.0);
        put("Craiova", 160.0);
        put("Dobreta", 242.0);
        put("Eforie", 161.0);
        put("Fagaras", 176.0);
        put("Giurgiu", 77.0);
        put("Hirsova", 151.0);
        put("Iasi", 226.0);
        put("Lugoj", 244.0);
        put("Mehadia", 241.0);
        put("Neamt", 234.0);
        put("Oradea", 380.0);
        put("Pitesti", 10.0);
        put("Rimnicu Vilcea", 193.0);
        put("Sibiu", 253.0);
        put("Timisoara", 329.0);
        put("Urziceni", 80.0);
        put("Vaslui", 199.0);
        put("Zerind", 374.0);
        }

    };

    private double calculateDistance(Node node){
        return testMap.get(node.getName());
    }

}
