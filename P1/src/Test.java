import game.Map;

public class Test {
    private void doSomething() {
        Map m = new Map(Map.l24);
        m.print(m.getRobots());
        //m.runAlgo("BFS");
        //m.runAlgo("IDDFS");
        m.runAlgo("A*");
        m.runAlgo("Greedy");

       //Test precomputed moves
       /*for (int[][] targetMoves : m.getPrecomputedMoves()) {
           if (targetMoves != null) {
               for (int[] row : targetMoves) {
                   for (int i : row) {
                       if (i == Integer.MAX_VALUE)
                           System.out.print("X");
                       else
                           System.out.print(i);
                       System.out.print(" ");
                   }
                   System.out.print("\n");
               }
           }
       }*/
    }

    public static void main(String[] args) {
        Test t = new Test();
        t.doSomething();
    }
}


