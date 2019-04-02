package gui;

import game.Map;

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

        String[] algorithms = new String[] {"A* #1", "A* #3", "BFS" , "IDDFS", "Greedy #1", "Greedy #3"};
        algoComboBox = new JComboBox<>(algorithms);
        algoComboBox.setFont(new Font("Impact", Font.PLAIN, 12));
        algoComboBox.setBounds(700, 50, 200, 50);
        algoComboBox.setBackground(Color.white);
        menuWindow.getContentPane().setLayout(null);
        menuWindow.getContentPane().add(algoComboBox);

        btnChooseFile = new JButton("Choose Level");
        btnChooseFile.setFont(new Font("Impact", Font.PLAIN, 12));
        btnChooseFile.addActionListener(new chooseLevelEvent());
        btnChooseFile.setBounds(700, 200, 200, 50);
        btnChooseFile.setBackground(Color.white);
        menuWindow.getContentPane().add(btnChooseFile);

        btnExitGUI = new JButton("Exit Game");
        btnExitGUI.setFont(new Font("Impact", Font.PLAIN, 12));
        btnExitGUI.addActionListener(new exitGameEvent());
        btnExitGUI.setBounds(700, 600, 200, 50);
        btnExitGUI.setBackground(Color.white);
        menuWindow.getContentPane().add(btnExitGUI);

        btnNextMove = new JButton("Next Move");
        btnNextMove.setFont(new Font("Impact", Font.PLAIN, 12));
        btnNextMove.setBounds(100, 50, 100, 25);
        btnNextMove.setBackground(Color.white);
        menuWindow.getContentPane().add(btnNextMove);

        btnSkip = new JButton("Skip");
        btnSkip.setFont(new Font("Impact", Font.PLAIN, 12));
        btnSkip.setBounds(250, 50, 100, 25);
        btnSkip.setBackground(Color.white);
        menuWindow.getContentPane().add(btnSkip);

        btnRunAlgo = new JButton("Run Algorithm");
        btnRunAlgo.setFont(new Font("Impact", Font.PLAIN, 12));
        btnExitGUI.addActionListener(new runAlgoEvent());
        btnRunAlgo.setBounds(700, 200, 100, 25);
        btnRunAlgo.setBackground(Color.white);
        menuWindow.getContentPane().add(btnRunAlgo);

    }

    private class runAlgoEvent implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            map.runAlgo(algoComboBox.getSelectedItem().toString());
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

            int returnValue = jfc.showOpenDialog(null);
            // int returnValue = jfc.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                map = null;
                try {
                    map = Map.fromFile(selectedFile.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                //game box
                gameBox = new GamePanel(map);
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