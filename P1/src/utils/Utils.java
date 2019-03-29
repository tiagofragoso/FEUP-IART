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


}
