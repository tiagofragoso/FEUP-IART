package game;

import algorithms.Node;
import javafx.util.Pair;
import utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.abs;

public class GameNode extends Node  {

    public enum Direction {UP, DOWN, LEFT, RIGHT}

    private Map map;
    private Element move;
    private ArrayList<Element> robots;

    GameNode(Map map, ArrayList<Element> robots, int moveCount) {
        super(moveCount);
        this.map = map;
        this.robots = robots;
    }

    GameNode(Map map, ArrayList<Element> robots, int moveCount, Element move) {
        super(moveCount);
        this.map = map;
        this.robots = robots;
        this.move = move;
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

    public Element getMove() {
        return move;
    }

    public Map getMap() {
        return map;
    }

    public ArrayList<Element> getRobots() {
        return robots;
    }

    public ArrayList<GameNode> getChildren() {
        boolean[][] matrix = Utils.deepCloneBlocking(this.map.getWalls());
        for (Element r: robots) {
            matrix[r.getY()][r.getX()] = true;
        }
        ArrayList<GameNode> nodes = new ArrayList<>();
        for (int i = 0; i < robots.size(); i++) {
            for (Direction m: Direction.values()) {
                Pair<Integer, Integer> newCoords;
                final Element robot = robots.get(i);
                if ((newCoords = this.move(robot, matrix, m)) != null) {
                    ArrayList<Element> newRobots = Utils.deepCloneElements(this.robots);
                    newRobots.get(i).setX(newCoords.getKey());
                    newRobots.get(i).setY(newCoords.getValue());
                    nodes.add(new GameNode(this.map, newRobots, this.getDepth() + 1, new Element(newCoords.getKey(), newCoords.getValue(), robot.getColor())));
                }
            }
        }
        return nodes;
    }

    public boolean isSolution() {
        ArrayList<Element> targets = this.map.getTargets();
        for (int i = 0; i < targets.size(); i++) {
            if (targets.get(i) == null) continue;
            if (!this.robots.get(i).sameLocationAs(targets.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (this.move != null)
            return this.move.toString();
        else return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameNode gameNode = (GameNode) o;
        return Objects.equals(robots, gameNode.robots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(robots);
    }


    private int distanceToTarget() {
        ArrayList<Element> targets = this.map.getTargets();
        for (Element target : targets) {
            if (target.getColor().equals(this.move.getColor())) {
                return abs(target.getX() - this.move.getX()) + abs(target.getY() - this.move.getY());
            }
        }

        return 0;
    }
}
