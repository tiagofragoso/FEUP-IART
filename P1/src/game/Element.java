package game;

import java.util.Objects;

public class Element implements Cloneable {
    public enum Color {RED, BLUE, YELLOW, GREEN, SILVER}

    private int x;
    private int y;
    private Color color;

    Element(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    Element(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean sameLocationAs(Element e) {
        return (this.x == e.getX() && this.y == e.y);
    }

    @Override
    public Object clone() {
        Element clone;
        try {
            clone = (Element) super.clone();
            clone.setX(this.x);
            clone.setY(this.y);
            clone.setColor(this.color);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    @Override
    public String toString() {
        return this.color.toString() + ":" + (char) (this.x + 'A') + (this.y + 1);
    }

    char getColorInitial() {
        return this.color.toString().charAt(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return x == element.x &&
                y == element.y &&
                color == element.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, color);
    }
}
