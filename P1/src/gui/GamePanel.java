package gui;

import game.Element;
import game.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.HashMap;

public class GamePanel extends JPanel {

    private HashMap<String, BufferedImage> loadedImages;
    private Map map;

    GamePanel(Map map) {
        loadedImages = new HashMap<>();
        try {
            loadedImages.put("empty", ImageIO.read(new File("images/white.png")));
            loadedImages.put("wall", ImageIO.read(new File("images/gray.png")));
            loadedImages.put("redRobot", ImageIO.read(new File("images/red.png")));
            loadedImages.put("blueRobot", ImageIO.read(new File("images/blue.png")));
            loadedImages.put("yellowRobot", ImageIO.read(new File("images/orange.png")));
            loadedImages.put("greenRobot", ImageIO.read(new File("images/green.png")));
            loadedImages.put("redTarget", ImageIO.read(new File("images/target_red.png")));
            loadedImages.put("blueTarget", ImageIO.read(new File("images/target_blue.png")));
            loadedImages.put("yellowTarget", ImageIO.read(new File("images/target_orange.png")));
            loadedImages.put("greenTarget", ImageIO.read(new File("images/target_green.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.map = map;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Character[][] map = game.getLevel().getPlayMap();

        drawImagePanel(g);
    }

    private void drawImagePanel(Graphics g) {
        chooseImage(g);
    }

    private void chooseImage(Graphics g) {
        chooseImageMovable(g);
    }

    private void chooseImageMovable(Graphics g) {
        for (int y = 0; y < map.getWalls().length; y++) {
            for (int x = 0; x < map.getWalls()[y].length; x++) {
                if (map.getWalls()[y][x])
                    g.drawImage(loadedImages.get("wall"), x * 32, y * 32, null);
                else if (!(checkRobots(g,x,y) || checkTargets(g,x, y))) {
                    g.drawImage(loadedImages.get("empty"), x * 32, y * 32, null);
                }
            }
        }
    }

    private boolean checkRobots(Graphics g,int x,int y) {

        for (Element robot : map.getRobots()) {
            if (robot != null && robot.getX() == x && robot.getY() == y) {
                switch(robot.getColorInitial()) {
                    case('R'):
                        g.drawImage(loadedImages.get("redRobot"), x * 32, y * 32, null);
                        break;
                    case('B'):
                        g.drawImage(loadedImages.get("blueRobot"), x * 32, y * 32, null);
                        break;
                    case('Y'):
                        g.drawImage(loadedImages.get("yellowRobot"), x * 32, y * 32, null);
                        break;
                    case('G'):
                        g.drawImage(loadedImages.get("greenRobot"), x * 32, y * 32, null);
                        break;
                    default:
                        break;
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkTargets(Graphics g,int x,int y) {

        for (Element target : map.getTargets()) {
            if (target != null && target.getX() == x && target.getY() == y) {
                switch(target.getColorInitial()) {
                    case('R'):
                        g.drawImage(loadedImages.get("redTarget"), x * 32, y * 32, null);
                        break;
                    case('B'):
                        g.drawImage(loadedImages.get("blueTarget"), x * 32, y * 32, null);
                        break;
                    case('Y'):
                        g.drawImage(loadedImages.get("yellowTarget"), x * 32, y * 32, null);
                        break;
                    case('G'):
                        g.drawImage(loadedImages.get("greenTarget"), x * 32, y * 32, null);
                        break;
                    default:
                        break;
                }
                return true;
            }
        }
        return false;
    }
}


