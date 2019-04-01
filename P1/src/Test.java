import game.Map;

import java.io.FileNotFoundException;

public class Test {
    private void doSomething() {
        try {
            Map m = Map.fromFile("/Users/tiagofragoso/FEUP/3ANO/2SEM/iart/P1/maps/l4.txt");
            m.print(m.getRobots());
            m.runAllAlgos();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Test t = new Test();
        t.doSomething();
    }
}


