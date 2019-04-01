import game.Map;

import java.io.FileNotFoundException;

public class Test {
    private void doSomething() {
        try {
            Map m = Map.fromFile("/Users/tiagofragoso/FEUP/3ANO/2SEM/iart/P1/maps/l24.txt");
            m.print(m.getRobots());
            m.runAlgo("IDDFS");
            m.runAlgo("A*");
            m.runAlgo("Greedy");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //m.runAlgo("BFS");

    }

    public static void main(String[] args) {
        Test t = new Test();
        t.doSomething();
    }
}


