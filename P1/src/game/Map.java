package game;

import algorithms.BFS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Map {
    private boolean[][] walls = new boolean[16][16];
    private ArrayList<Element> targets = new ArrayList<>(5);
    private ArrayList<Element> robots = new ArrayList<>(5);
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
                        robots.add(index, e);
                    } else  if (el.charAt(0) == 'T') {
                        targets.add(index, e);
                    }

                }
            }
        }
        this.robots.trimToSize();
        this.targets.trimToSize();
    }

    public boolean[][] getWalls() {
        return walls;
    }

    public ArrayList<Element> getTargets() {
        return targets;
    }

    public void print() {
       for (boolean[] row: this.walls) {
           for (boolean el: row) {
               if (el)
                   System.out.print(" X ");
               else System.out.print(" B ");
           }
           System.out.print("\n");
       }
       System.out.println("Robots :"); //print robots
       //print targets
   }

   public void runAlgo(String algo) {
        switch (algo) {
            case "BFS":
                System.out.println(BFS.run(new GameNode(this, this.robots, 0)));
                break;
        }
   }

    public static String[][] l1 = new String[][]{
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "TR", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "", "", "W", "W", "W", "W", "", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "", "", "W", "W", "W", "", "", "", "", "W", "W"},
            {"W", "W", "", "", "", "", "", "W", "W", "", "", "", "", "", "W", "W"},
            {"W", "", "", "", "", "", "", "W", "W", "", "", "", "", "", "W", "W"},
            {"W", "", "", "W", "", "", "", "", "", "", "", "", "", "", "W", "W"},
            {"", "", "RR", "W", "W", "", "", "", "", "", "W", "", "W", "", "W", "W"},
            {"", "", "", "W", "W", "W", "W", "W", "W", "W", "W", "", "W", "", "W", "W"},
            {"", "", "", "W", "W", "W", "W", "W", "W", "W", "W", "", "", "", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}
    };

    public static String[][] l2 = new String[][]{
            {"W", "", "", "", "", "", "", "W", "W", "", "", "", "", "", "", "W"},
            {"W", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"", "", "W", "", "W", "", "", "", "", "", "", "W", "", "W", "", ""},
            {"W", "W", "W", "", "W", "W", "W", "W", "W", "W", "W", "W", "", "W", "W", "W"},
            {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "W"},
            {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "W"},
            {"", "", "W", "W", "W", "W", "", "W", "W", "W", "W", "", "", "", "", "W"},
            {"", "", "W", "TB", "", "W", "", "W", "W", "W", "W", "W", "", "", "", "W"},
            {"W", "", "W", "", "", "W", "", "", "", "W", "", "W", "W", "", "", "W"},
            {"W", "", "", "", "", "W", "", "", "RB", "W", "", "", "W", "", "", "W"},
            {"W", "W", "W", "", "", "W", "W", "W", "W", "W", "W", "W", "W", "", "", "W"},
            {"W", "W", "W", "", "", "", "", "", "", "", "", "", "", "", "", "W"},
            {"", "", "W", "", "", "", "W", "W", "W", "W", "W", "", "", "", "", "W"},
            {"", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "RR"}
    };

    public static String[][] l3 = new String[][]{
        {"W", "W", "TR", "", "", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        {"", "", "", "", "", "", "W", "", "", "", "RR", "", "", "W", "", "W"},
        {"", "", "", "", "", "", "", "", "", "W", "", "", "", "W", "", "W"},
        {"", "", "W", "W", "W", "W", "", "", "", "", "", "W", "W", "W", "", "W"},
        {"", "", "W", "W", "", "", "", "", "", "", "", "", "W", "W", "", "W"},
        {"", "W", "W", "", "", "", "", "W", "W", "", "", "", "W", "W", "", "W"},
        {"", "W", "", "", "", "", "", "", "", "", "", "", "W", "W", "", "W"},
        {"", "", "", "", "", "", "", "W", "W", "", "W", "W", "W", "W", "", "W"},
        {"", "W", "", "", "", "", "", "W", "W", "", "W", "W", "W", "W", "", "W"},
        {"", "W", "W", "W", "W", "", "", "", "", "", "", "", "", "W", "", "W"},
        {"", "W", "", "", "W", "", "", "W", "W", "", "", "", "", "W", "", "W"},
        {"", "W", "", "", "W", "", "", "W", "W", "", "", "", "", "W", "", "W"},
        {"", "W", "W", "W", "W", "", "", "W", "W", "W", "W", "", "", "W", "", "W"},
        {"", "", "W", "", "", "", "", "", "", "", "W", "", "", "W", "W", "W"},
        {"", "", "", "", "W", "W", "W", "", "", "W", "W", "", "", "", "", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "", "", "", "", "", "", "", "", "W"}
    };

    public static String[][] l4 = new String[][]{
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        {"W", "", "W", "W", "W", "", "W", "W", "W", "W", "", "W", "W", "W", "", "W"},
        {"W", "", "W", "W", "W", "", "", "W", "W", "", "", "W", "W", "W", "", "W"},
        {"W", "", "", "", "W", "W", "", "", "", "", "W", "W", "", "", "", "W"},
        {"W", "W", "W", "", "W", "W", "W", "W", "W", "W", "W", "W", "", "W", "W", "W"},
        {"W", "W", "W", "", "", "", "W", "W", "W", "W", "", "", "", "W", "W", "W"},
        {"W", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        {"W", "", "", "", "W", "W", "W", "W", "W", "W", "W", "W", "", "", "", "W"},
        {"W", "W", "W", "", "", "", "W", "W", "W", "W", "", "", "", "W", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "", "", "W", "W", "W", "W", "W", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "", "", "W", "W", "W", "", "", "W", "W"},
        {"W", "W", "", "", "", "", "", "RB", "", "", "", "", "", "", "W", "W"},
        {"W", "", "RR", "", "W", "W", "W", "W", "W", "", "", "", "", "", "W", "W"},
        {"W", "", "", "W", "W", "W", "W", "W", "W", "W", "", "", "W", "", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "", "", "", "", "TR", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}
    };
}
