import game.Map;

public class Test {
    public void doSomething() {
        Map m = new Map(Map.l8);
        m.print(m.getRobots());
        m.runAlgo("BFS");
        m.runAlgo("DFS");
        m.runAlgo("A*");
    }

    public static void main(String[] args) {
        Test t = new Test();
        t.doSomething();
    }
}


