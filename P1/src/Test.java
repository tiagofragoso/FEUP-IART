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
       for (int i = 1; i <= 24; i++) {
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
        /*for (int[][] tgt : m.getHeuristicMatrix(3)) {
            if (tgt == null) continue;
            System.out.print("\n");
            for (int[] row : tgt) {
                for (int i : row) {
                    if (i == Integer.MAX_VALUE)
                        System.out.print("X  ");
                    else System.out.print(i + "  ");
                }
                System.out.print("\n");
            }
        }*/
        (new Map(Map.l24)).runAlgo("A* #1");
        (new Map(Map.l24)).runAlgo("A* #2");
        (new Map(Map.l24)).runAlgo("A* #3");
    }
}


