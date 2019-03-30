import algorithms.*;
import game.Map;
import graph.Graph;

public class Test {
    public void doSomething() {
        /*Graph graph = new Graph();
        graph.addEdge("Oradea", "Zerind", 71);
        graph.addEdge("Oradea", "Sibiu", 151);
        graph.addEdge("Zerind", "Arad", 75);
        graph.addEdge("Arad", "Sibiu", 140);
        graph.addEdge("Arad", "Timisoara", 118);
        graph.addEdge("Timisoara", "Lugoj", 111);
        graph.addEdge("Lugoj", "Mehadia", 70);
        graph.addEdge("Mehadia", "Dobreta", 75);
        graph.addEdge("Dobreta", "Craiova", 120);
        graph.addEdge("Sibiu", "Rimnicu Vilcea", 80);
        graph.addEdge("Sibiu", "Fagaras", 99);
        graph.addEdge("Craiova", "Rimnicu Vilcea", 148);
        graph.addEdge("Craiova", "Pitesti", 138);
        graph.addEdge("Rimnicu Vilcea", "Pitesti", 97);
        graph.addEdge("Fagaras", "Bucharest", 211);
        graph.addEdge("Pitesti", "Bucharest", 101);
        graph.addEdge("Bucharest", "Giurgiu", 90);
        graph.addEdge("Bucharest", "Urziceni", 85);
        graph.addEdge("Urziceni", "Hirsova", 98);
        graph.addEdge("Urziceni", "Vaslui", 142);
        graph.addEdge("Vaslui", "Iasi", 92);
        graph.addEdge("Iasi", "Neamt", 87);
        graph.addEdge("Hirsova", "Eforie", 86);

        System.out.println(new AStar(graph, "Arad").run("Bucharest").toString());*/

        (new Map(Map.l2)).runAlgo("DFS");
    }

    public static void main(String[] args) {
        Test t = new Test();
        t.doSomething();
    }
}


