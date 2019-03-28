package game;

import graph.Node;

import java.util.ArrayList;

public class GameNode extends Node {
    private Map map;
    private int moveCount = 0;
    private ArrayList<Element> robots = new ArrayList<>();

    public GameNode(String name) {
        super(name);
    }


}
