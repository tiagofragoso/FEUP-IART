package game;

public class Element {
    enum Color {RED, BLUE, YELLOW, GREEN, SILVER}

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

    public void setColor(Color color) {
        this.color = color;
    }
}
