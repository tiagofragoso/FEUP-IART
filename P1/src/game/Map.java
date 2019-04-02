package game;

import algorithms.*;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.*;

public class Map {
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

    private Map(String[][] matrix) {
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
        this.preomputeHeuristics();
        this.startNode = new GameNode(this, this.robots, 0);
    }

    public static Map fromFile(String fileName) throws FileNotFoundException {
        try {
            String[][] matrix = new String[16][16];
            FileReader reader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                matrix[i++] = parseLine(line);
            }
            bufferedReader.close();
            return new Map(matrix);
        } catch (IOException ex) {
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

    public ArrayList<int[][]> getHeuristicMatrix(int index) {
        if (index == 1) {
            return h1Matrix;
        } else if (index == 2) {
            return h2Matrix;
        }
        return null;
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
            System.out.print(" " + (y + 1));
            if (y + 1 > 9) {
                System.out.print(" ");
            } else {
                System.out.print("  ");
            }

            for (int x = 0; x < this.walls[y].length; x++) {
                if (this.walls[y][x])
                    System.out.print(" X  ");
                else if (!(printRobots(x, y, robots) || printTargets(x, y))) {
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
            if (target != null && target.getX() == x && target.getY() == y) {
                System.out.print(" T" + target.getColorInitial() + " ");
                return true;
            }
        }

        return false;
    }

    public Solution runAlgo(String algo) {
        final Duration timeout = Duration.ofMinutes(1);
        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });

        Algorithm algorithm;
        switch (algo) {
            case "BFS":
                System.out.println("BFS");
                algorithm = new BFS(this.startNode);
                break;
            case "IDDFS":
                System.out.println("IDDFS");
                algorithm = new IDDFS(this.startNode);
                break;
            case "A* #1":
                System.out.println("A* #1");
                algorithm = new AStar(this.startNode, 1);
                break;
            case "A* #2":
                System.out.println("A* #2");
                algorithm = new AStar(this.startNode, 2);
                break;
            case "Greedy #1":
                System.out.println("Greedy #1");
                algorithm = new Greedy(this.startNode, 1);
                break;
            case "Greedy #2":
                System.out.println("Greedy #2");
                algorithm = new Greedy(this.startNode, 2);
                break;
            default:
                System.out.println("Invalid algorithm");
                return null;
        }
        final Future handler = executor.submit(algorithm);
        try {
            handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            algorithm.printSolution();
            return algorithm.getSolution();
        } catch (TimeoutException e) {
            handler.cancel(true);
            System.out.println("Timeout");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdownNow();
        return null;
    }

    public void runAllAlgos() {
        this.runAlgo("BFS");
        this.runAlgo("IDDFS");
        this.runAlgo("A* #1");
        this.runAlgo("A* #2");
        this.runAlgo("Greedy #1");
        this.runAlgo("Greedy #2");
    }

    private void preomputeHeuristics() {
        this.precomputeH1();
        this.precomputeH2();
    }

    private void precomputeH1() {
        int[][][] moves = new int[5][][];
        for (Element t : this.targets) {
            if (t != null)
                moves[this.colorMap.get(t.getColor())] = precomputeH1ForTarget(new Node(t.getX(), t.getY(), 0));
        }
        this.h1Matrix = new ArrayList<>(Arrays.asList(moves));
        ;
    }

    private int[][] precomputeH1ForTarget(Node target) {
        int[][] moves = new int[16][16];
        for (int[] row : moves) {
            Arrays.fill(row, 2);
        }
        moves[target.getY()][target.getX()] = 0;
        for (Direction d : Direction.values()) {
            int newX = target.getX();
            int newY = target.getY();
            Pair<Integer, Integer> offset = directionOffset.get(d);
            int xOff = offset.getKey();
            int yOff = offset.getValue();
            newX += xOff;
            newY += yOff;
            try {
                while (!this.walls[newY][newX]) {
                    moves[newY][newX] = 1;
                    newX += xOff;
                    newY += yOff;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
        return moves;
    }

    private void precomputeH2() {
        int[][][] moves = new int[5][][];
        for (Element t : this.targets) {
            if (t != null)
                moves[this.colorMap.get(t.getColor())] = precomputeH2ForTarget(new Node(t.getX(), t.getY(), 0));
        }
        this.h2Matrix = new ArrayList<>(Arrays.asList(moves));
    }

    private int[][] precomputeH2ForTarget(Node target) {
        boolean[][] status = new boolean[16][16];
        for (boolean[] row : status) {
            Arrays.fill(row, false);
        }
        int[][] moves = new int[16][16];
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
                        int yOff = offset.getValue();
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
                        } catch (ArrayIndexOutOfBoundsException ignored) {
                        }
                    }
                }
            }
        }
        return moves;
    }

    public enum Direction {UP, DOWN, LEFT, RIGHT}
}
