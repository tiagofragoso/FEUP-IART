package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Map {
    private boolean[][] walls = new boolean[16][16];
    private Element[] targets = new Element[5];
    private Element[] robots = new Element[5];
    private HashMap<Element.Color, Integer> colorMap = new HashMap<>();
    public Map(String[][] matrix) {
        for (boolean[] row: walls)
            Arrays.fill(row, false);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                final String el = matrix[i][j];
                if (el.equals("W"))
                    walls[i][j] = true;
                else if (!el.equals("")){
                    Element.Color c = null;
                    switch (el.charAt(1)) {
                        case 'B':
                            c = Element.Color.BLUE;
                            break;
                        case 'R':
                            c = Element.Color.RED;
                            break;
                        case 'Y':
                            c = Element.Color.YELLOW;
                            break;
                        case 'G':
                            c = Element.Color.GREEN;
                            break;
                        case 'S':
                            c = Element.Color.SILVER;
                            break;
                    }
                    Element e = new Element(j, i, c);
                    Integer index = colorMap.get(c);
                    if (index == null) {
                        index = colorMap.size();
                        colorMap.put(c, index);
                    }
                    if (el.charAt(0) == 'R') {
                        robots[index] = e;
                    } else  if (el.charAt(0) == 'T') {
                        targets[index] = e;
                    }

                }
            }
        }
    }

   public void print() {
        int coordY = 16;
       for (boolean[] row: this.walls) {
            System.out.print(" " + coordY);
            if (coordY > 9) {
                System.out.print(" ");
            } else {
                System.out.print("  ");
            }
            coordY--;
           for (boolean el: row) {
               if (el)
                   System.out.print(" X ");
               else System.out.print("   ");
           }
           System.out.print("\n");
       }

       System.out.print("\n    ");
       char coordX = 'A';
       for (int i = 0; i < 16; i++) {
           System.out.print(" " + coordX + " ");
           coordX++;

       }
       System.out.print("\n");
       System.out.println("Robots :"); //print robots
       //print targets
   }

    public static String[][] l1 = new String[][]{
            {"W", "W", "", "", "", "", "", "", "", "", "", "", "", "", "W", "W"},
            {"W", "", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "", "", "W"},
            {"", "", "W", "W", "", "", "", "", "", "", "", "", "W", "W", "", ""},
            {"", "W", "W", "TB", "", "", "", "", "", "", "", "", "TY", "W", "W", ""},
            {"", "W", "", "", "", "", "", "", "", "", "", "", "", "", "W", ""},
            {"", "W", "", "", "", "", "", "", "", "", "", "", "", "", "W", ""},
            {"", "W", "", "", "", "", "", "", "", "", "", "", "", "", "W", ""},
            {"", "W", "", "", "", "RY", "", "", "", "", "", "", "", "", "W", ""},
            {"", "W", "", "", "", "", "", "", "", "", "", "", "", "", "W", ""},
            {"", "W", "", "", "", "", "", "RR", "", "", "", "", "", "", "W", ""},
            {"", "W", "", "", "", "", "", "", "", "", "", "", "", "", "W", ""},
            {"", "W", "", "", "", "", "", "", "", "", "RB", "", "", "", "W", ""},
            {"", "W", "W", "TR", "", "", "", "", "", "", "", "", "", "W", "W", ""},
            {"", "", "W", "W", "", "", "", "", "", "", "", "", "W", "W", "", ""},
            {"W", "", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "", "", "W"},
            {"W", "W", "", "", "", "", "", "", "", "", "", "", "", "", "W", "W"},
    };
}
