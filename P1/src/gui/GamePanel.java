package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.HashMap;

public class GamePanel extends JPanel{

    private HashMap<String, BufferedImage> loadedImages;

    GamePanel(){
        loadedImages = new HashMap<>();
        try {
            loadedImages.put("empty",ImageIO.read(new File("images/white.png")));
            loadedImages.put("wall",ImageIO.read(new File("images/gray.png")));
            loadedImages.put("redRobot",ImageIO.read(new File ("images/red.png")));
            loadedImages.put("blueRobot",ImageIO.read(new File ("images/blue.png")));
            loadedImages.put("yellowRobot",ImageIO.read(new File ("images/orange.png")));
            loadedImages.put("greenRobot",ImageIO.read(new File ("images/green.png")));
            loadedImages.put("redTarget",ImageIO.read(new File ("images/target_red.png")));
            loadedImages.put("blueTarget",ImageIO.read(new File ("images/target_blue.png")));
            loadedImages.put("yellowTarget",ImageIO.read(new File ("images/target_orange.png")));
            loadedImages.put("greenTarget",ImageIO.read(new File ("images/target_green.png")));

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Character[][] map = game.getLevel().getPlayMap();

        //drawImagePanel(g, map);
    }

    private void drawBackground(Graphics g, Character[][] map) {

        for (int i = 0; i < map.length; ++i)
            for (int j = 0; j < map[i].length; ++j) {
                g.drawImage(loadedImages.get("background"), j * 32, i * 32, null);
            }
    }

    private void drawImagePanel(Graphics g, Character[][] map) {
        drawBackground(g,map);
        for (int i = 0; i < map.length; ++i)
            for (int j = 0; j < map[i].length; ++j){
                chooseImage(g, map[i][j], i, j);
            }
    }

    private void chooseImage(Graphics g, Character character, int i, int j) {
        chooseImageMovable(g, character, i, j);
    }

    private void chooseImageMovable(Graphics g, Character character, int i, int j) {
        switch (character) {
            case ' ':
                g.drawImage(loadedImages.get("empty"), j * 32, i * 32, null);
                break;
            case 'W':
                g.drawImage(loadedImages.get("wall"), j * 32, i * 32, null);
                break;
            case 'K':
                g.drawImage(loadedImages.get("heroArmedWithKey"), j * 32, i * 32, null);
                break;
            case 'G':
                g.drawImage(loadedImages.get("guard"), j * 32, i * 32, null);
                break;
            case 'g':
                g.drawImage(loadedImages.get("guardSleeping"), j * 32, i * 32, null);
                break;
            case '0':
                g.drawImage(loadedImages.get("ogre"), j * 32, i * 32, null);
                break;
            case '8':
                g.drawImage(loadedImages.get("ogreStunned"), j * 32, i * 32, null);
                break;
            case '$':
                g.drawImage(loadedImages.get("ogreKey"), j * 32, i * 32, null);
                break;
            case '*':
                g.drawImage(loadedImages.get("club"), j * 32, i * 32, null);
                break;
        }
    }

}


