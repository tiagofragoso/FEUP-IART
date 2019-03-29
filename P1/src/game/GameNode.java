package game;

import graph.Node;
import javafx.util.Pair;
import utils.Utils;

import java.util.ArrayList;

public class GameNode extends Node {

    enum Direction {UP, DOWN, LEFT, RIGHT}

    private Map map;
    private int moveCount;
    private ArrayList<Element> robots;

    public GameNode(Map map, ArrayList<Element> robots, int moveCount) {
        super("nodename");
        this.map = map;
        this.robots = robots;
        this.moveCount = moveCount;
    }

    private Pair<Integer, Integer> move(Element robot, boolean[][] blockingElements, Direction d) {
        int xOff = 0;
        int yOff = 0;
        int xIni = robot.getX();
        int yIni = robot.getY();
        int x = xIni;
        int y = yIni;

        switch (d) {
            case UP:
                yOff = -1;
                break;
            case DOWN:
                yOff = 1;
                break;
            case LEFT:
                xOff = -1;
                break;
            case RIGHT:
                xOff = 1;
                break;
        }
        do {
            x += xOff;
            y += yOff;

            try {
                if (blockingElements[y][x])
                    break;
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        } while (true);
        x -= xOff;
        y -= yOff;
        if (x != xIni || y != yIni)
            return new Pair<>(x, y);
        else return null;
    }

    public ArrayList<GameNode> getChildren() {
        boolean[][] matrix = this.map.getWalls().clone();
        for (Element r: robots) {
            matrix[r.getY()][r.getX()] = true;
        }
        ArrayList<GameNode> nodes = new ArrayList<>();
        for (int i = 0; i < robots.size(); i++) {
            for (Direction m: Direction.values()) {
                Pair<Integer, Integer> newCoords;
                if ((newCoords = this.move(robots.get(i), matrix, m)) != null) {
                    ArrayList<Element> newRobots = Utils.deepCloneElements(this.robots);
                    newRobots.get(i).setX(newCoords.getKey());
                    newRobots.get(i).setY(newCoords.getValue());
                    nodes.add(new GameNode(this.map, newRobots, this.moveCount + 1));
                }
            }
        }
        return nodes;
    }

    public boolean isSolution() {
        ArrayList<Element> targets = this.map.getTargets();
        for (int i = 0; i < targets.size(); i++) {
            if (!this.robots.get(i).sameLocationAs(targets.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Element e: this.robots) {
            builder.append(e);
        }
        return builder.toString();
    }
}
