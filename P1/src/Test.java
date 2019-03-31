import game.Map;

public class Test {
    public void doSomething() {
        Map m = new Map(Map.l1);
        m.print(m.getRobots());
        m.runAlgo("BFS");
        m.runAlgo("IDDFS");
        m.runAlgo("A*");
    }

    public static void main(String[] args) {
        Test t = new Test();
        t.doSomething();
    }
}


