package game;

import algorithms.BFS;
import algorithms.DFS;
import graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Map {
    private boolean[][] walls = new boolean[16][16];
    private ArrayList<Element> targets;
    private ArrayList<Element> robots;

    public Map(String[][] matrix) {
        for (boolean[] row : walls)
            Arrays.fill(row, false);

        Element[] robots = new Element[5];
        Element[] targets = new Element[5];

        HashMap<Element.Color, Integer> colorMap = new HashMap<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                final String el = matrix[i][j];
                if (el.equals("W"))
                    walls[i][j] = true;
                else if (!el.equals("")) {
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
                    } else if (el.charAt(0) == 'T') {
                        targets[index] = e;
                    }

                }
            }
        }
        this.robots = new ArrayList<>();
        for (Element e : robots) {
            if (e != null)
                this.robots.add(e);
        }
        this.targets = new ArrayList<>(Arrays.asList(targets));
        System.out.println(this.robots);
        System.out.println(this.targets);

    }

    public boolean[][] getWalls() {
        return walls;
    }

    public ArrayList<Element> getTargets() {
        return targets;
    }

    public ArrayList<Element> getRobots() {
        return robots;
    }

    public void print(ArrayList<Element> robots) {
        for (int y = 0; y < this.walls.length; y++) {
            System.out.print(" " + (y+1));
            if (y+1 > 9) {
                System.out.print(" ");
            } else {
                System.out.print("  ");
            }

            for (int x = 0; x < this.walls[y].length; x++) {
                if (this.walls[y][x])
                    System.out.print(" X  ");
                else if (printRobots(x, y, robots)) {
                    break;
                } else if (printTargets(x, y)) {
                    break;
                } else {
                    System.out.print("    ");
                }
            }
            System.out.print("\n\n");
        }

        System.out.print("    ");
        char coordX = 'A';
        for (int i = 0; i < 16; i++) {
            System.out.print(" " + coordX + "  ");
            coordX++;

        }
        System.out.print("\n");
        System.out.println("Robots :"); //print robots
        //print targets
    }

    public boolean printRobots(int x, int y, ArrayList<Element> robots) {
        for (Element robot : robots) {
            if (robot != null && robot.getX() == x && robot.getY() == y) {
                System.out.print(" R" + robot.getColorInitial() + " ");
                return true;
            }
        }

        return false;
    }

    public boolean printTargets(int x, int y) {
        for (Element target : this.targets) {
            if (target != null && target.getX() == x && target.getY() == y ) {
                System.out.print(" T" + target.getColorInitial() + " ");
                return true;
            }
        }

        return false;
    }

    public void runAlgo(String algo) {
        ArrayList<Node> sol = null;
        long start = System.currentTimeMillis();
        switch (algo) {
            case "BFS":
                System.out.println("Using BFS:");
                sol = BFS.run(new GameNode(this, this.robots, 0));
                break;
            case "DFS":
                System.out.println("Using IDDFS:");
                sol = DFS.run(new GameNode(this, this.robots, 0), 50);
                break;
        }
        long end = System.currentTimeMillis();
        if (sol != null) {
            System.out.println(sol);
            System.out.println("Move count: " + sol.size());
            System.out.println("Elapsed time: " + (end - start) + "ms");
        } else {
            System.out.println("Couldn't find solution");
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

    public static String[][] l5 = new String[][]{
        {"", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "", ""},
        {"", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "", ""},
        {"W", "", "", "", "", "", "W", "W", "W", "W", "", "", "", "", "", "W"},
        {"W", "", "", "", "", "", "W", "W", "W", "W", "", "", "", "", "", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        {"W", "W", "W", "", "", "", "", "W", "W", "", "", "", "TB", "W", "W", "W"},
        {"W", "W", "W", "", "", "", "", "", "", "", "", "", "", "W", "W", "W"},
        {"", "", "W", "W", "W", "RR", "", "W", "W", "", "", "W", "W", "W", "", ""},
        {"", "", "", "", "W", "", "", "W", "W", "", "W", "W", "", "", "", ""},
        {"", "", "", "", "W", "", "", "", "", "", "W", "W", "", "", "", ""},
        {"W", "W", "", "", "W", "", "", "", "", "", "", "W", "", "", "W", "W"},
        {"W", "W", "", "", "W", "", "", "W", "W", "", "", "W", "", "", "W", "W"},
        {"W", "W", "", "", "W", "RB", "", "", "", "", "TR", "W", "", "", "W", "W"},
        {"W", "W", "", "", "W", "W", "W", "W", "W", "W", "W", "W", "", "", "W", "W"},
        {"W", "W", "", "", "W", "W", "W", "", "", "W", "W", "W", "", "", "W", "W"},
        {"W", "W", "W", "", "", "", "", "", "", "", "", "", "", "W", "W", "W"}
    };

    public static String[][] l6 = new String[][]{
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        {"W", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "", "W"},
        {"W", "W", "W", "TR", "", "", "", "", "", "", "", "", "", "W", "W", "W"},
        {"W", "W", "", "", "", "", "", "", "", "", "", "", "", "", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "", "", "W", "W", "W"},
        {"W", "", "W", "", "W", "", "W", "", "W", "", "W", "", "", "W", "", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "", "", "W", "W", "W"},
        {"W", "", "W", "", "W", "W", "W", "W", "W", "W", "W", "", "", "W", "W", "W"},
        {"W", "W", "W", "W", "W", "", "W", "W", "W", "W", "W", "", "", "W", "W", "W"},
        {"W", "", "", "", "W", "W", "W", "", "W", "", "W", "", "", "W", "W", "W"},
        {"W", "", "", "", "", "", "W", "", "W", "W", "W", "", "", "", "W", "W"},
        {"W", "", "", "", "", "", "RB", "", "", "", "", "", "", "W", "W", "W"},
        {"W", "", "W", "W", "RR", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        {"W", "", "W", "W", "", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        {"W", "", "", "", "", "", "W", "W", "", "W", "", "W", "", "W", "", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}
    };

    public static String[][] l7 = new String[][]{
        {"", "", "", "", "W", "W", "W", "W", "W", "W", "W", "W", "", "", "", ""},
        {"", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "", "", ""},
        {"", "", "W", "W", "W", "W", "", "", "", "", "W", "W", "W", "W", "", ""},
        {"W", "W", "W", "W", "", "", "", "", "", "", "", "", "W", "W", "", ""},
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", ""},
        {"W", "W", "W", "TB", "", "", "", "", "", "", "", "", "W", "W", "W", "W"},
        {"W", "W", "", "", "RB", "", "", "", "", "", "", "", "W", "W", "W", "W"},
        {"W", "W", "", "", "", "", "", "W", "W", "", "", "", "W", "W", "W", "W"},
        {"W", "W", "", "", "", "", "", "W", "W", "RR", "", "", "W", "W", "W", "W"},
        {"W", "W", "", "", "", "", "", "", "", "", "", "", "W", "W", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "", "", "", "", "W", "W", "W", "W"},
        {"W", "W", "W", "", "", "", "W", "W", "", "", "", "", "W", "W", "W", "W"},
        {"W", "W", "W", "W", "TR", "", "", "", "", "", "", "", "W", "W", "W", "W"},
        {"W", "W", "W", "W", "W", "", "", "", "", "", "", "", "W", "W", "W", "W"},
        {"", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", ""},
        {"", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "", ""}
    };

    public static String[][] l8 = new String[][]{
        {"", "W", "", "W", "", "W", "", "W", "", "", "W", "W", "", "", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "", "", "", "W", "W", "", "", "W"},
        {"", "", "", "", "", "", "", "W", "W", "W", "", "", "W", "W", "", ""},
        {"", "W", "W", "W", "W", "W", "", "W", "", "W", "", "", "", "W", "W", ""},
        {"", "W", "W", "W", "", "W", "", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        {"", "W", "", "", "", "W", "", "", "", "", "", "", "", "", "", "W"},
        {"", "W", "", "", "", "", "", "", "", "", "", "", "", "", "", "W"},
        {"W", "W", "", "W", "W", "W", "", "W", "W", "W", "", "", "W", "W", "", ""},
        {"", "", "", "", "", "W", "", "W", "W", "W", "", "W", "W", "", "", ""},
        {"W", "TR", "", "", "", "W", "", "", "W", "W", "", "W", "W", "", "", ""},
        {"W", "W", "W", "W", "W", "W", "W", "", "", "", "", "", "", "", "W", "W"},
        {"", "W", "", "", "", "", "W", "", "", "", "", "", "W", "W", "W", ""},
        {"", "W", "", "", "RG", "", "", "", "W", "W", "W", "W", "W", "", "", ""},
        {"", "W", "RR", "", "", "", "", "", "W", "", "", "", "", "", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "", "W", "W", "", "W", "W", ""},
        {"", "W", "", "", "", "", "", "", "W", "", "W", "", "W", "W", "", ""}
    };

    public static String[][] l9 = new String[][]{
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        {"W", "", "W", "", "W", "", "W", "W", "W", "", "W", "W", "", "W", "", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        {"W", "", "W", "", "W", "W", "W", "", "W", "W", "W", "W", "W", "", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "", "W", "W", "TR", "", "W", "W", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "", "", "W", "W", "W", "W"},
        {"W", "W", "", "W", "", "", "", "", "", "", "", "", "", "", "", "W"},
        {"W", "W", "W", "W", "", "", "", "W", "W", "", "", "", "", "", "", "W"},
        {"W", "", "", "", "", "", "", "W", "W", "", "", "", "", "", "", "W"},
        {"W", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "W"},
        {"W", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "W"},
        {"W", "", "", "RR", "", "", "", "RB", "", "", "", "", "", "", "", "W"},
        {"W", "", "", "", "", "", "", "", "", "", "", "", "", "W", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "", "", "", "", "", "", "", "W", "", "W"},
        {"W", "", "W", "W", "", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}
    };

    
}
