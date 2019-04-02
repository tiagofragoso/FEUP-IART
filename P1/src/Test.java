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

    private static void runAllTests() {
        String base = "/Users/tiagofragoso/FEUP/3ANO/2SEM/iart/P1/maps/l";
       for (int i = 1; i <= 4; i++) {
           try {
               Map map = Map.fromFile(base + i + ".txt");
               System.out.println("\nLevel " + i +"");
               map.runAllAlgos();
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }
       }
    }

    public static void main(String[] args) {
        (new Map(Map.l16)).runAllAlgos();
    }
}


