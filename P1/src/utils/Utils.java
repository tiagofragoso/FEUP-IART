package utils;

import game.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {
    public static ArrayList<Element> deepCloneElements(ArrayList<Element> arr) {
        ArrayList<Element> clone = new ArrayList<>();
        for (Element element : arr) {
            clone.add((Element) element.clone());
        }
        return clone;
    }

    public static boolean[][] deepCloneBlocking(boolean[][] arr) {
        boolean[][] clone = new boolean[16][16];
        for (int i = 0; i < clone.length; i++) {
            clone[i] = arr[i].clone();
        }
        return clone;
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

}
