import game.Map;

public class Test {
    public void doSomething() {
        Map m = new Map(Map.l1);
        m.print();
        m.runAlgo("BFS");
        m.runAlgo("DFS");
    }

    public static void main(String[] args) {
        Test t = new Test();
        t.doSomething();
    }
}


