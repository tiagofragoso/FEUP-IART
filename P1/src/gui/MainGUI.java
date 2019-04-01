package gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;

public class MainGUI extends JPanel {

    private JFrame menuWindow;
    private JButton btnExitGUI;
    private JButton btnChooseFile;
    private JComboBox<String> algoComboBox;
    private GamePanel gameBox;
    private JButton btnNextMove;
    private JButton btnSkip;

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
        initializePanel();
    }

    private void initializePanel() {
        //game box
        gameBox = new GamePanel();
        gameBox.setBounds(50, 100, 360, 400);
        gameBox.setVisible(false);
        gameBox.setFocusable(false);
    }


    private void initializeFrame() {
        //frame
        menuWindow = new JFrame("Labyrinth Robots");
        menuWindow.setBounds(100, 100, 640, 480);
        menuWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        menuWindow.setVisible(true);
        menuWindow.setResizable(false);
    }

    private void initializeElements() {

        String[] algorithms = new String[] {"A*", "BFS",
                "IDDFS", "Greedy"};
        algoComboBox = new JComboBox<>(algorithms);
        algoComboBox.setFont(new Font("Impact", Font.PLAIN, 12));
        algoComboBox.setBounds(460, 50, 100, 25);
        algoComboBox.setBackground(Color.white);
        menuWindow.getContentPane().setLayout(null);
        menuWindow.getContentPane().add(algoComboBox);

        btnChooseFile = new JButton("Choose Level");
        btnChooseFile.setFont(new Font("Impact", Font.PLAIN, 12));
        btnChooseFile.addActionListener(new chooseLevelEvent());
        btnChooseFile.setBounds(460, 100, 100, 25);
        btnChooseFile.setBackground(Color.white);
        menuWindow.getContentPane().add(btnChooseFile);

        btnExitGUI = new JButton("Exit Game");
        btnExitGUI.setFont(new Font("Impact", Font.PLAIN, 12));
        btnExitGUI.addActionListener(new exitGameEvent());
        btnExitGUI.setBounds(460, 340, 100, 25);
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
                System.out.println(selectedFile.getAbsolutePath());
            }
        }
    }
}