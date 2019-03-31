package game;

import algorithms.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Map {
    private boolean[][] walls = new boolean[16][16];
    private ArrayList<Element> targets;
    private ArrayList<Element> robots;
    private GameNode startNode;
    HashMap<Element.Color, Integer> colorMap = new HashMap<>();
    private ArrayList<int[][]> precomputedMoves;

    public Map(String[][] matrix) {
        for (boolean[] row : walls)
            Arrays.fill(row, false);

        Element[] robots = new Element[5];
        Element[] targets = new Element[5];


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
        int[][][] moves = new int[5][][];
        for (Element t: this.targets) {
            if (t != null)
                moves[this.colorMap.get(t.getColor())] = BFS.precomputeMoves(this, new Node(t.getX(), t.getY(), 0));
        }
        precomputedMoves = new ArrayList<>(Arrays.asList(moves));
        this.startNode = new GameNode(this, this.robots, 0);
    }

    public ArrayList<int[][]> getPrecomputedMoves() {
        return precomputedMoves;
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

    public HashMap<Element.Color, Integer> getColorMap() {
        return colorMap;
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
                else if (!(printRobots(x, y, robots) || printTargets(x, y))){
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
        Algorithm algorithm;
        switch (algo) {
            case "BFS":
                System.out.println("Using BFS:");
                algorithm = new BFS(this.startNode);
                ((BFS) algorithm).run();
                break;
            case "IDDFS":
                System.out.println("Using IDDFS:");
                algorithm = new IDDFS(this.startNode);
                ((IDDFS) algorithm).run(25);
                break;
            case "A*":
                System.out.println("Using A*:");
                algorithm = new AStar(this.startNode);
                ((AStar) algorithm).run();
                break;
            default:
                System.out.println("Invalid algorithm");
                return;
        }

        algorithm.printSolution();

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
