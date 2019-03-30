package utils;

import game.Element;

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

}
