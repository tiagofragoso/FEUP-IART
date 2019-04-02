import game.Map;

import java.io.FileNotFoundException;

public class Solver {
    private static void runAllTests() {
        String base = "/Users/tiagofragoso/FEUP/3ANO/2SEM/iart/P1/maps/l";
       for (int i = 1; i <= 20; i++) {
           try {
               Map map = Map.fromFile(base + i + ".txt");
               System.out.println("\nLevel " + i +"");
               map.runAllAlgos();
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }
       }
    }

    public static void main(String[] args) throws FileNotFoundException {
        /*Map m = Map.fromFile("/Users/tiagofragoso/FEUP/3ANO/2SEM/iart/P1/maps/l16.txt");
        m.runAllAlgos();*/
        runAllTests();
    }
}


