import algorithms.*;
import graph.Graph;

public class Test {
    public void doSomething() {
        Graph graph = new Graph();
        graph.addEdge("A", "B");
        graph.addEdge("B", "E");
        graph.addEdge("B", "F");
        graph.addEdge("F", "G");
        graph.addEdge("F", "H");
        graph.addEdge("B", "I");
        graph.addEdge("B", "J");
        graph.addEdge("J", "K");
        graph.addEdge("J", "L");
        graph.addEdge("L", "M");
        graph.addEdge("L", "N");
        graph.addEdge("L", "O");
        graph.addEdge("J", "P");
        graph.addEdge("A", "C");
        graph.addEdge("A", "D");

        System.out.println(new DFS(graph, "A").run().toString());

    }

    public static void main(String[] args) {
        Test t = new Test();
        t.doSomething();
    }
}


