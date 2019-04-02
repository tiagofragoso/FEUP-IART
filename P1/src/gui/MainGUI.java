package gui;

import algorithms.Node;
import game.GameNode;
import game.Map;
import game.Solution;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;

public class MainGUI extends JPanel {

    private JFrame menuWindow;
    private JButton btnExitGUI;
    private JButton btnChooseFile;
    private JComboBox<String> algoComboBox;
    private GamePanel gameBox;
    private JButton btnNextMove;
    private JButton btnSkip;
    private JButton btnRunAlgo;
    private Map map;
    private Map originalMap;
    private Solution solution;
    private int currNode;
    private JTextArea infoLabel;
    private JScrollPane scroll;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainGUI();
            }
        });
    }

    private MainGUI() {

        initializeFrame();
        initializeElements();
    }

    private void initializeFrame() {
        //frame
        menuWindow = new JFrame("Labyrinth Robots");
        menuWindow.setBounds(100, 100, 1000, 800);
        menuWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        menuWindow.setVisible(true);
        menuWindow.setResizable(false);
    }

    private void initializeElements() {

        String[] algorithms = new String[] {"A* #1", "A* #2", "A* #3", "BFS" , "IDDFS", "Greedy #1", "Greedy #2", "Greedy #3"};
        algoComboBox = new JComboBox<>(algorithms);
        algoComboBox.setBounds(700, 50, 200, 50);
        algoComboBox.setBackground(Color.white);
        menuWindow.getContentPane().setLayout(null);
        menuWindow.getContentPane().add(algoComboBox);

        btnChooseFile = new JButton("Choose Level");
        btnChooseFile.addActionListener(new chooseLevelEvent());
        btnChooseFile.setBounds(700, 200, 200, 50);
        btnChooseFile.setBackground(Color.white);
        menuWindow.getContentPane().add(btnChooseFile);

        btnExitGUI = new JButton("Exit Game");
        btnExitGUI.addActionListener(new exitGameEvent());
        btnExitGUI.setBounds(700, 600, 200, 50);
        btnExitGUI.setBackground(Color.white);
        menuWindow.getContentPane().add(btnExitGUI);

        btnNextMove = new JButton("Next Move");
        btnNextMove.setBounds(100, 50, 100, 25);
        btnNextMove.addActionListener(new nextMoveEvent());
        btnNextMove.setBackground(Color.white);
        btnNextMove.setEnabled(false);
        menuWindow.getContentPane().add(btnNextMove);

        btnSkip = new JButton("Skip");
        btnSkip.addActionListener(new skipEvent());
        btnSkip.setBounds(250, 50, 100, 25);
        btnSkip.setEnabled(false);
        btnSkip.setBackground(Color.white);
        menuWindow.getContentPane().add(btnSkip);

        btnRunAlgo = new JButton("Run Algorithm");
        btnRunAlgo.addActionListener(new runAlgoEvent());
        btnRunAlgo.setBounds(700, 300, 200, 50);
        btnRunAlgo.setBackground(Color.white);
        menuWindow.getContentPane().add(btnRunAlgo);

        infoLabel = new JTextArea();
        infoLabel.setBounds(700, 400, 200, 100);
        infoLabel.setEditable(false);
        //scroll = new JScrollPane(infoLabel);
        //scroll.setBounds(700, 400, 200, 100);

        infoLabel.setLineWrap(true);
       //scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        menuWindow.getContentPane().add(infoLabel);
        //menuWindow.getContentPane().add(scroll);

    }

    private class skipEvent implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            for(int j = currNode ; j < solution.getNodes().size(); j++) {
                String[] parts = solution.getNodes().get(currNode).toString().split(":");
                for (int i=0; i < map.getRobots().size(); i++){
                    if (map.getRobots().get(i).getColor().name().equals(parts[0])){
                        String letter = parts[1];
                        String number = parts[1].substring(1);
                        map.getRobots().get(i).setX((int)letter.charAt(0) - (int)'A');
                        map.getRobots().get(i).setY(Integer.parseInt(number) - 1);
                        gameBox.setMap(map);
                        gameBox.repaint();
                        gameBox.setFocusable(true);
                        gameBox.requestFocusInWindow();
                    }
                }
                currNode++;
            }

        }
    }

    private class runAlgoEvent implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            solution = map.runAlgo(algoComboBox.getSelectedItem().toString());
            currNode = 0;
            btnNextMove.setEnabled(true);
            btnSkip.setEnabled(true);

            infoLabel.setText(solution.getTextAreaInfo());

            gameBox.setRobots(((GameNode) solution.getNodes().get(currNode)).getRobots());

            gameBox.repaint();
            gameBox.setFocusable(true);
            gameBox.requestFocusInWindow();

        }
    }

    private class nextMoveEvent implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            currNode++;
            /*String[] parts = solution.getNodes().get(currNode).toString().split(":");
            for (int i=0; i < map.getRobots().size(); i++){
                if (map.getRobots().get(i).getColor().name().equals(parts[0])){
                        String letter = parts[1];
                        String number = parts[1].substring(1);
                        map.getRobots().get(i).setX((int)letter.charAt(0) - (int)'A');
                        map.getRobots().get(i).setY(Integer.parseInt(number) - 1);
                        */
                        //gameBox.setMap(map);
            gameBox.setRobots(((GameNode) solution.getNodes().get(currNode)).getRobots());
                        gameBox.repaint();
                        gameBox.setFocusable(true);
                        gameBox.requestFocusInWindow();

                    //}
                //}
            //}

            if(solution.getNodes().size() == currNode+1){
                btnNextMove.setEnabled(false);
                btnSkip.setEnabled(false);
            }
        }
    }


    private class exitGameEvent implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {

            menuWindow.dispatchEvent(new WindowEvent(menuWindow, WindowEvent.WINDOW_CLOSING));
        }
    }

    private class chooseLevelEvent implements ActionListener {
        public void actionPerformed(ActionEvent arg) {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    ".txt files", "txt");

            jfc.setFileFilter(filter);
            jfc.setCurrentDirectory(new File("."));

            int returnValue = jfc.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                map = null;
                originalMap = null;
                try {
                    map = Map.fromFile(selectedFile.getAbsolutePath());
                    originalMap = map;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                //game box
                gameBox = new GamePanel(map, map.getRobots());
                gameBox.setBounds(50, 100, 512, 512);
                gameBox.setVisible(true);
                gameBox.setFocusable(false);
                gameBox.setBackground(new Color(32,32,32));
                menuWindow.add(gameBox);
                gameBox.repaint();
                gameBox.setFocusable(true);
                gameBox.requestFocusInWindow();
            }
        }
    }
}