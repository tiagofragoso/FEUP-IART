package game;

import algorithms.*;
import javafx.util.Pair;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Map {

    public enum Direction {UP, DOWN, LEFT, RIGHT}

    static HashMap<Direction, Pair<Integer, Integer>> directionOffset = new HashMap<Direction, Pair<Integer, Integer>>() {
        {
            put(Direction.UP, new Pair<>(0, -1));
            put(Direction.DOWN, new Pair<>(0, 1));
            put(Direction.LEFT, new Pair<>(-1, 0));
            put(Direction.RIGHT, new Pair<>(1, 0));
        }
    };

    private boolean[][] walls = new boolean[16][16];
    private ArrayList<Element> targets;
    private ArrayList<Element> robots;
    private GameNode startNode;
    private HashMap<Element.Color, Integer> colorMap = new HashMap<>();
    private ArrayList<int[][]> h1Matrix;
    private ArrayList<int[][]> h2Matrix;
    private ArrayList<int[][]> h3Matrix;

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
                else if (!el.equals("B")) {
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
        this.preComputeHeuristics();
        this.startNode = new GameNode(this, this.robots, 0);
    }

    public ArrayList<int[][]> getHeuristicMatrix(int index) {
        if (index == 1) {
            return h1Matrix;
        } else if (index == 2) {
            return h2Matrix;
        } else if (index == 3) {
            return h3Matrix;
        }
        return null;
    }

    boolean[][] getWalls() {
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
    }

    private boolean printRobots(int x, int y, ArrayList<Element> robots) {
        for (Element robot : robots) {
            if (robot != null && robot.getX() == x && robot.getY() == y) {
                System.out.print(" R" + robot.getColorInitial() + " ");
                return true;
            }
        }
        return false;
    }

    private boolean printTargets(int x, int y) {
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
            case "Greedy":
                System.out.println("Using Greedy:");
                algorithm = new Greedy(this.startNode);
                ((Greedy) algorithm).run();
                break;
            default:
                System.out.println("Invalid algorithm");
                return;
        }

        algorithm.printSolution();

    }

    private void preComputeHeuristics() {
        this.precomputeH1();
        this.precomputeH3();
    }

    private void precomputeH1() {
        int[][][] moves = new int[5][][];
        for (Element t: this.targets) {
            if (t != null)
                moves[this.colorMap.get(t.getColor())] = precomputeH1ForTarget(new Node(t.getX(), t.getY(), 0));
        }
        this.h1Matrix = new ArrayList<>(Arrays.asList(moves));;
    }

    private int[][] precomputeH1ForTarget(Node target) {
        int [][] moves = new int[16][16];
        for (int[] row : moves) {
            Arrays.fill(row, 2);
        }
        moves[target.getY()][target.getX()] = 0;
        for (Direction d : Direction.values()) {
            int newX = target.getX();
            int newY = target.getY();
            Pair<Integer, Integer> offset = directionOffset.get(d);
            int xOff = offset.getKey();
            int yOff= offset.getValue();
            newX += xOff;
            newY += yOff;
            try {
                while (!this.walls[newY][newX]) {
                    moves[newY][newX] = 1;
                    newX += xOff;
                    newY += yOff;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }
        return moves;
    }

    private void precomputeH3() {
        int[][][] moves = new int[5][][];
        for (Element t: this.targets) {
            if (t != null)
                moves[this.colorMap.get(t.getColor())] = precomputeH3ForTarget(new Node(t.getX(), t.getY(), 0));
        }
        this.h3Matrix = new ArrayList<>(Arrays.asList(moves));
    }

    private int[][] precomputeH3ForTarget(Node target) {
        boolean [][] status = new boolean[16][16];
        for (boolean[] row : status) {
            Arrays.fill(row, false);
        }
        int [][] moves = new int[16][16];
        for (int[] row : moves) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        moves[target.getY()][target.getX()] = 0;
        status[target.getY()][target.getX()] = true;
        boolean done = false;
        while (!done) {
            done = true;
            for (int y = 0; y < status.length; y++) {
                for (int x = 0; x < status[y].length; x++) {
                    if (!status[y][x])
                        continue;
                    status[y][x] = false;
                    int depth = moves[y][x] + 1;
                    for (Direction d : Direction.values()) {
                        int newX = x;
                        int newY = y;
                        Pair<Integer, Integer> offset = directionOffset.get(d);
                        int xOff = offset.getKey();
                        int yOff= offset.getValue();
                        newX += xOff;
                        newY += yOff;
                        try {
                            while (!this.walls[newY][newX]) {
                                if (moves[newY][newX] > depth) {
                                    moves[newY][newX] = depth;
                                    status[newY][newX] = true;
                                    done = false;
                                }
                                newX += xOff;
                                newY += yOff;
                            }
                        } catch (ArrayIndexOutOfBoundsException ignored) {}
                    }
                }
            }
        }
        return moves;
    }

    public static Map fromFile(String fileName) throws FileNotFoundException {
        try {
            String[][] matrix = new String[16][16];
            FileReader reader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            int i = 0;
            while((line = bufferedReader.readLine()) != null) {
                matrix[i++] = parseLine(line);
            }
            bufferedReader.close();
            System.out.println(Arrays.deepToString(matrix));
            return new Map(matrix);
        } catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        return null;
    }

    private static String[] parseLine(String line) {
        String[] tokens = line.split(",");
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }
        return tokens;
    }

    public static void writeLevelToFile(String[][] level, String name) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/tiagofragoso/FEUP/3ANO/2SEM/iart/P1/maps/" + name, true));
            for (int i = 0; i < level.length; i++) {
                for (int j = 0; j < level[i].length; j++) {
                    writer.append(level[i][j]);
                    if (j != level[i].length - 1)
                        writer.append(',');
                    else if (i != level.length - 1)
                        writer.append('\n');
                }
            }
            writer.close();
        } catch (IOException ignored) {}
    }


    public static String[][] l1 = new String[][]{
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "TR", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "B", "B", "W", "W", "W", "W", "B", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "B", "B", "W", "W", "W", "B", "B", "B", "B", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"B", "B", "RR", "W", "W", "B", "B", "B", "B", "B", "W", "B", "W", "B", "W", "W"},
            {"B", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "W", "B", "W", "W"},
            {"B", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "B", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}
    };

    public static String[][] l2 = new String[][]{
            {"W", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"B", "B", "W", "B", "W", "B", "B", "B", "B", "B", "B", "W", "B", "W", "B", "B"},
            {"W", "W", "W", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "W", "W", "W"},
            {"B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"B", "B", "W", "W", "W", "W", "B", "W", "W", "W", "W", "B", "B", "B", "B", "W"},
            {"B", "B", "W", "TB", "B", "W", "B", "W", "W", "W", "W", "W", "B", "B", "B", "W"},
            {"W", "B", "W", "B", "B", "W", "B", "B", "B", "W", "B", "W", "W", "B", "B", "W"},
            {"W", "B", "B", "B", "B", "W", "B", "B", "RB", "W", "B", "B", "W", "B", "B", "W"},
            {"W", "W", "W", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"B", "B", "W", "B", "B", "B", "W", "W", "W", "W", "W", "B", "B", "B", "B", "W"},
            {"B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "RR"}
    };

    public static String[][] l3 = new String[][]{
            {"W", "W", "TR", "B", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"B", "B", "B", "B", "B", "B", "W", "B", "B", "B", "RR", "B", "B", "W", "B", "W"},
            {"B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "B", "B", "B", "W", "B", "W"},
            {"B", "B", "W", "W", "W", "W", "B", "B", "B", "B", "B", "W", "W", "W", "B", "W"},
            {"B", "B", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B", "W"},
            {"B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "W"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B", "W"},
            {"B", "B", "B", "B", "B", "B", "B", "W", "W", "B", "W", "W", "W", "W", "B", "W"},
            {"B", "W", "B", "B", "B", "B", "B", "W", "W", "B", "W", "W", "W", "W", "B", "W"},
            {"B", "W", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W", "B", "W"},
            {"B", "W", "B", "B", "W", "B", "B", "W", "W", "B", "B", "B", "B", "W", "B", "W"},
            {"B", "W", "B", "B", "W", "B", "B", "W", "W", "B", "B", "B", "B", "W", "B", "W"},
            {"B", "W", "W", "W", "W", "B", "B", "W", "W", "W", "W", "B", "B", "W", "B", "W"},
            {"B", "B", "W", "B", "B", "B", "B", "B", "B", "B", "W", "B", "B", "W", "W", "W"},
            {"B", "B", "B", "B", "W", "W", "W", "B", "B", "W", "W", "B", "B", "B", "B", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W"}
    };

    public static String[][] l4 = new String[][]{
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "B", "W", "W", "W", "B", "W", "W", "W", "W", "B", "W", "W", "W", "B", "W"},
            {"W", "B", "W", "W", "W", "B", "B", "W", "W", "B", "B", "W", "W", "W", "B", "W"},
            {"W", "B", "B", "B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "W"},
            {"W", "W", "W", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "W", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "W", "W", "W", "W", "B", "B", "B", "W", "W", "W"},
            {"W", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "B", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "B", "W"},
            {"W", "W", "W", "B", "B", "B", "W", "W", "W", "W", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "B", "B", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "B", "B", "W", "W", "W", "B", "B", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "RB", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "RR", "B", "W", "W", "W", "W", "W", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W", "B", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "B", "B", "TR", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}
    };

    public static String[][] l5 = new String[][]{
            {"B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B"},
            {"B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B"},
            {"W", "B", "B", "B", "B", "B", "W", "W", "W", "W", "B", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "B", "B", "W", "W", "W", "W", "B", "B", "B", "B", "B", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "TB", "W", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"B", "B", "W", "W", "W", "RR", "B", "W", "W", "B", "B", "W", "W", "W", "B", "B"},
            {"B", "B", "B", "B", "W", "B", "B", "W", "W", "B", "W", "W", "B", "B", "B", "B"},
            {"B", "B", "B", "B", "W", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B"},
            {"W", "W", "B", "B", "W", "B", "B", "B", "B", "B", "B", "W", "B", "B", "W", "W"},
            {"W", "W", "B", "B", "W", "B", "B", "W", "W", "B", "B", "W", "B", "B", "W", "W"},
            {"W", "W", "B", "B", "W", "RB", "B", "B", "B", "B", "TR", "W", "B", "B", "W", "W"},
            {"W", "W", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W", "W"},
            {"W", "W", "B", "B", "W", "W", "W", "B", "B", "W", "W", "W", "B", "B", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W"}
    };

    public static String[][] l6 = new String[][]{
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "W"},
            {"W", "W", "W", "TR", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W", "W", "W"},
            {"W", "B", "W", "B", "W", "B", "W", "B", "W", "B", "W", "B", "B", "W", "B", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W", "W", "W"},
            {"W", "B", "W", "B", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "B", "W", "W", "W", "W", "W", "B", "B", "W", "W", "W"},
            {"W", "B", "B", "B", "W", "W", "W", "B", "W", "B", "W", "B", "B", "W", "W", "W"},
            {"W", "B", "B", "B", "B", "B", "W", "B", "W", "W", "W", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "B", "B", "B", "RB", "B", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "B", "W", "W", "RR", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "B", "W", "W", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "B", "B", "B", "B", "B", "W", "W", "B", "W", "B", "W", "B", "W", "B", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}
    };

    public static String[][] l7 = new String[][]{
            {"B", "B", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "B", "B"},
            {"B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "B"},
            {"B", "B", "W", "W", "W", "W", "B", "B", "B", "B", "W", "W", "W", "W", "B", "B"},
            {"W", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B"},
            {"W", "W", "W", "TB", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "B", "B", "RB", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "W", "W", "RR", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "W", "W", "B", "B", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "TR", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W"},
            {"B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B"},
            {"B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B"}
    };

    public static String[][] l8 = new String[][]{
            {"B", "W", "B", "W", "B", "W", "B", "W", "B", "B", "W", "W", "B", "B", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "B", "W", "W", "B", "B", "W"},
            {"B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "B", "B", "W", "W", "B", "B"},
            {"B", "W", "W", "W", "W", "W", "B", "W", "B", "W", "B", "B", "B", "W", "W", "B"},
            {"B", "W", "W", "W", "B", "W", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"B", "W", "B", "B", "B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "W", "B", "W", "W", "W", "B", "W", "W", "W", "B", "B", "W", "W", "B", "B"},
            {"B", "B", "B", "B", "B", "W", "B", "W", "W", "W", "B", "W", "W", "B", "B", "B"},
            {"W", "TR", "B", "B", "B", "W", "B", "B", "W", "W", "B", "W", "W", "B", "B", "B"},
            {"W", "W", "W", "W", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"B", "W", "B", "B", "B", "B", "W", "B", "B", "B", "B", "B", "W", "W", "W", "B"},
            {"B", "W", "B", "B", "RG", "B", "B", "B", "W", "W", "W", "W", "W", "B", "B", "B"},
            {"B", "W", "RR", "B", "B", "B", "B", "B", "W", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "W", "W", "B", "W", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "W", "B", "W", "B", "W", "W", "B", "B"}
    };

    public static String[][] l9 = new String[][]{
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "B", "W", "B", "W", "B", "W", "W", "W", "B", "W", "W", "B", "W", "B", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "B", "W", "B", "W", "W", "W", "B", "W", "W", "W", "W", "W", "B", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "B", "W", "W", "TR", "B", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "W", "W", "W", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "RR", "B", "B", "B", "RB", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "W", "B", "W"},
            {"W", "B", "W", "W", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}
    };

    public static String[][] l10 = new String[][]{
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "B", "W", "B", "B", "B", "B", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "W", "W", "B", "B", "W", "W", "W", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "W", "W", "B", "B", "W", "W", "W", "B", "B", "B", "B", "W", "W", "W"},
            {"B", "B", "B", "B", "B", "B", "W", "W", "W", "W", "B", "B", "W", "B", "W", "W"},
            {"B", "B", "B", "B", "B", "B", "B", "W", "B", "B", "B", "B", "B", "TR", "B", "W"},
            {"B", "B", "B", "W", "B", "B", "B", "W", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"B", "RR", "B", "B", "B", "W", "B", "W", "W", "W", "W", "B", "B", "W", "W", "W"},
            {"W", "B", "B", "W", "B", "B", "B", "W", "W", "W", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "W", "W", "B", "B", "B", "W", "W", "W", "B", "B", "B", "B", "W", "W"},
            {"W", "W", "W", "W", "B", "B", "B", "W", "W", "W", "W", "RB", "B", "B", "W", "W"},
            {"W", "W", "W", "W", "B", "B", "B", "W", "W", "W", "W", "B", "B", "W", "W", "W"}
    };

    public static String[][] l11 = new String[][]{
            {"W", "W", "W", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "W", "W", "W"},
            {"W", "W", "B", "W", "B", "W", "B", "W", "W", "B", "W", "B", "W", "B", "W", "W"},
            {"W", "B", "W", "B", "W", "B", "W", "B", "B", "W", "B", "W", "B", "W", "B", "W"},
            {"B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "TR", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "B", "B", "W", "W", "B", "B", "B", "B", "B", "W", "B", "B", "W", "W"},
            {"B", "W", "B", "B", "B", "B", "RR", "B", "B", "B", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "W", "B", "W", "B", "B", "W", "W", "B", "W", "W", "B", "B", "W", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "W", "B", "B", "W", "W", "B"},
            {"B", "W", "W", "W", "W", "B", "B", "B", "B", "RG", "W", "B", "B", "B", "W", "B"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"B", "W", "TG", "B", "B", "B", "B", "B", "B", "W", "B", "B", "B", "W", "W", "B"},
            {"W", "B", "W", "W", "B", "B", "B", "B", "B", "W", "B", "B", "W", "W", "B", "W"},
            {"W", "W", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "W", "W"},
            {"W", "W", "W", "B", "W", "W", "B", "W", "W", "B", "W", "W", "B", "W", "W", "W"}
    };

    public static String[][] l12 = new String[][]{
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "B", "B", "B", "B", "W", "W", "W", "W", "W", "W", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "W"},
            {"W", "B", "B", "W", "W", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B", "W"},
            {"W", "B", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "RR", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B", "W"},
            {"W", "B", "W", "W", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B", "W"},
            {"W", "W", "B", "W", "W", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "W"},
            {"W", "B", "W", "B", "W", "W", "B", "B", "TR", "W", "W", "B", "B", "B", "B", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}
    };

    public static String[][] l13 = new String[][]{
            {"B", "B", "B", "B", "B", "W", "W", "W", "W", "W", "B", "B", "B", "W", "W", "B"},
            {"W", "B", "W", "W", "W", "W", "B", "B", "B", "W", "W", "B", "B", "B", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "W"},
            {"B", "B", "W", "B", "B", "RB", "B", "B", "B", "W", "W", "W", "B", "B", "B", "B"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "B", "B", "B"},
            {"W", "B", "B", "B", "B", "W", "B", "B", "B", "B", "B", "B", "W", "B", "B", "B"},
            {"W", "B", "B", "B", "W", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B"},
            {"W", "W", "B", "B", "B", "B", "B", "W", "W", "RR", "B", "B", "B", "W", "W", "W"},
            {"B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "W", "W"},
            {"B", "B", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "TR", "B", "W", "W", "B"},
            {"B", "W", "B", "B", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "W", "W", "B", "B", "W", "W", "B", "B", "B", "B", "W", "B", "W", "W"},
            {"B", "B", "B", "B", "B", "B", "W", "B", "B", "W", "B", "B", "B", "W", "W", "W"},
            {"B", "W", "W", "W", "W", "B", "W", "W", "B", "B", "B", "B", "B", "W", "B", "B"},
            {"B", "B", "B", "B", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B"}
    };

    public static String[][] l14 = new String[][]{
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "W", "B", "W", "B", "B", "B", "B", "W", "W", "W", "W", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "W", "W", "W", "B", "B", "W", "W", "W", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "W", "W", "B", "B", "W", "W", "W", "B", "B", "B", "B", "W", "W", "W"},
            {"B", "B", "B", "B", "B", "B", "W", "W", "W", "W", "B", "B", "W", "B", "W", "W"},
            {"B", "B", "B", "W", "B", "B", "B", "W", "B", "B", "B", "B", "B", "TR", "B", "W"},
            {"B", "RR", "B", "B", "B", "W", "B", "W", "W", "W", "W", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "W", "W", "B", "B", "B", "W", "W", "W", "B", "B", "B", "B", "B", "W"},
            {"W", "W", "W", "W", "B", "B", "B", "W", "W", "W", "B", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "W", "B", "B", "B", "B", "B", "W", "W", "RB", "B", "B", "B", "W"},
            {"TB", "B", "B", "B", "B", "B", "B", "W", "B", "B", "B", "B", "B", "W", "W", "W"}
    };

    public static String[][] l15 = new String[][]{
            {"W", "W", "W", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "W", "W", "W"},
            {"W", "W", "B", "W", "B", "W", "B", "W", "W", "B", "W", "B", "W", "B", "W", "W"},
            {"W", "B", "W", "B", "W", "B", "W", "B", "B", "W", "B", "W", "B", "W", "B", "W"},
            {"B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "RB", "B", "TG", "B", "W", "W", "W"},
            {"W", "W", "B", "B", "W", "W", "B", "B", "B", "B", "B", "W", "B", "B", "W", "W"},
            {"B", "W", "TR", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "W", "B", "W", "B", "B", "W", "W", "B", "W", "W", "B", "B", "W", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "W", "B", "B", "W", "W", "B"},
            {"B", "W", "W", "W", "W", "B", "B", "B", "B", "RG", "W", "B", "B", "B", "W", "B"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "W", "W", "B", "RR", "B", "B", "B", "W", "W", "W"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "W", "B", "B", "B", "W", "W", "B"},
            {"W", "B", "W", "W", "B", "B", "B", "B", "TB", "W", "B", "B", "W", "W", "B", "W"},
            {"W", "W", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "W", "W"},
            {"W", "W", "W", "B", "W", "W", "B", "W", "W", "B", "W", "W", "B", "W", "W", "W"}
    };

    public static String[][] l16 = new String[][]{
            {"B", "B", "W", "B", "B", "B", "B", "W", "W", "W", "B", "B", "B", "B", "B", "B"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B"},
            {"B", "B", "W", "B", "B", "B", "W", "B", "W", "W", "W", "W", "W", "B", "B", "W"},
            {"B", "B", "W", "B", "RB", "W", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"B", "W", "W", "W", "B", "B", "B", "W", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"B", "W", "B", "W", "B", "B", "B", "RR", "B", "B", "W", "B", "B", "B", "B", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "W", "W", "B", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "B", "B", "W", "B"},
            {"B", "W", "TB", "B", "B", "W", "B", "W", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "W", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"B", "B", "W", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "W", "W", "B", "B", "B", "W", "B", "B", "TR", "B", "W", "W"},
            {"W", "W", "B", "B", "B", "W", "W", "W", "W", "B", "B", "B", "W", "W", "W", "B"},
            {"W", "W", "W", "B", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B"}
    };

    public static String[][] l17 = new String[][]{
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B"},
            {"B", "W", "B", "W", "B", "B", "B", "W", "B", "B", "B", "RY", "W", "B", "W", "B"},
            {"B", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "B"},
            {"B", "W", "B", "RR", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "TB", "W", "TG", "B", "B", "B", "W", "W"},
            {"B", "W", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "W", "W", "W", "B"},
            {"B", "W", "W", "W", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "W", "W"},
            {"B", "W", "B", "B", "B", "B", "W", "TY", "B", "B", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "B", "B", "W", "W"},
            {"B", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "RB", "W", "W", "W", "B"},
            {"B", "W", "B", "W", "RG", "B", "B", "B", "W", "B", "B", "B", "W", "B", "W", "B"},
            {"B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B"},
            {"W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"}
    };

    public static String[][] l18 = new String[][]{
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B"},
            {"B", "W", "B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W", "B", "W", "B"},
            {"B", "W", "W", "W", "B", "W", "B", "B", "B", "TB", "W", "B", "W", "W", "W", "B"},
            {"B", "W", "B", "B", "B", "TR", "B", "B", "B", "B", "B", "B", "B", "RY", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"B", "W", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "W", "W"},
            {"B", "W", "B", "B", "B", "B", "RB", "B", "B", "RR", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"B", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "B"},
            {"B", "W", "B", "W", "RG", "B", "B", "B", "B", "B", "B", "B", "W", "B", "W", "B"},
            {"B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B"},
            {"W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"}
    };

    public static String[][] l19 = new String[][]{
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B"},
            {"B", "W", "B", "W", "B", "B", "B", "W", "B", "B", "B", "RY", "W", "B", "W", "B"},
            {"B", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "B"},
            {"B", "W", "B", "B", "B", "RR", "B", "B", "B", "B", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "TB", "W", "TG", "B", "B", "B", "W", "W"},
            {"B", "W", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "W", "W", "W", "B"},
            {"B", "W", "W", "W", "B", "B", "TY", "W", "W", "B", "B", "B", "B", "B", "W", "W"},
            {"B", "W", "B", "B", "B", "B", "W", "B", "B", "B", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "RB", "B", "B", "B", "W", "W"},
            {"B", "W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "B"},
            {"B", "W", "B", "W", "RG", "B", "B", "B", "W", "B", "B", "B", "W", "B", "W", "B"},
            {"B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B"},
            {"W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"}
    };

    public static String[][] l20 = new String[][]{
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
            {"W", "B", "B", "B", "B", "W", "W", "W", "W", "W", "W", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "W"},
            {"W", "B", "B", "W", "W", "TY", "B", "B", "B", "B", "B", "W", "W", "B", "B", "W"},
            {"W", "B", "W", "W", "TB", "B", "B", "W", "B", "B", "B", "B", "W", "W", "B", "W"},
            {"W", "W", "W", "TR", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W"},
            {"W", "B", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B", "W"},
            {"W", "B", "B", "W", "W", "RR", "B", "B", "B", "B", "RB", "W", "W", "B", "B", "W"},
            {"W", "B", "B", "B", "W", "W", "B", "B", "B", "RY", "W", "W", "B", "B", "B", "W"},
            {"W", "B", "B", "B", "B", "W", "W", "W", "W", "W", "W", "B", "B", "B", "B", "W"},
            {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}
    };

    public static String[][] l21 = new String[][]{
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W"},
            {"B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B"},
            {"B", "W", "W", "TB", "B", "B", "B", "B", "B", "B", "B", "B", "TY", "W", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "W", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "W", "B", "B", "W", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "RB", "B", "B", "RY", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "W", "W", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "W", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "RR", "B", "B", "B", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "W", "B", "B", "W", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "W", "TR", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B"},
            {"B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B"},
            {"W", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"}
    };

    public static String[][] l22 = new String[][]{
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W"},
            {"B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "W", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "W", "B", "B", "W", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "RB", "B", "B", "RY", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "W", "W", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "W", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "RR", "B", "B", "RG", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "W", "B", "B", "W", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "TR", "W", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "TY", "TB", "W", "W", "B"},
            {"B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B"},
            {"W", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"}
    };

    public static String[][] l23 = new String[][]{
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"},
            {"W", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W"},
            {"B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "TR", "B", "B", "W", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "W", "B", "B", "W", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "RB", "B", "B", "RY", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "W", "W", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "W", "W", "B", "B", "B", "TY", "W", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "RR", "B", "B", "B", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "W", "W", "B", "B", "W", "W", "B", "B", "B", "W", "B"},
            {"B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "B"},
            {"B", "W", "W", "B", "B", "B", "B", "TB", "B", "B", "B", "B", "B", "W", "W", "B"},
            {"B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B", "B", "W", "W", "B", "B"},
            {"W", "B", "B", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "B", "B", "W"},
            {"W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "W"}
    };

    public static String[][] l24 = new String[][]{
            {"W", "W", "B", "B", "W", "B", "B", "B", "B", "W", "B", "B", "B", "W", "W", "W"},
            {"W", "B", "B", "B", "W", "B", "TR", "B", "B", "B", "B", "B", "B", "B", "TB", "W"},
            {"B", "B", "B", "B", "B", "B", "W", "W", "B", "W", "B", "W", "W", "B", "W", "W"},
            {"B", "RB", "B", "B", "B", "B", "B", "W", "B", "B", "B", "B", "RY", "B", "W", "W"},
            {"W", "W", "B", "B", "B", "B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B"},
            {"B", "B", "B", "W", "B", "B", "B", "B", "B", "B", "W", "B", "B", "B", "B", "B"},
            {"B", "B", "B", "B", "B", "B", "W", "W", "B", "B", "W", "W", "B", "B", "B", "B"},
            {"W", "B", "B", "B", "B", "B", "W", "W", "W", "B", "W", "W", "W", "B", "B", "B"},
            {"B", "B", "B", "B", "B", "B", "B", "W", "W", "B", "B", "B", "B", "B", "B", "B"},
            {"B", "RG", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "W", "B", "W", "W"},
            {"W", "W", "B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "TG", "B", "W", "B"},
            {"W", "B", "B", "W", "W", "B", "B", "B", "W", "B", "B", "W", "W", "B", "B", "W"},
            {"B", "B", "B", "B", "B", "B", "B", "B", "W", "W", "W", "W", "B", "B", "B", "W"},
            {"B", "B", "B", "B", "B", "B", "W", "W", "W", "W", "W", "W", "B", "B", "B", "B"},
            {"W", "B", "B", "B", "B", "B", "W", "B", "B", "B", "B", "B", "B", "B", "B", "W"},
            {"W", "W", "W", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "RR", "W", "W"}
    };
}
