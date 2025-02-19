import java.awt.*;

public class Stadt {
    public int x, y;
    public boolean inTour;

    public Stadt(int pX, int pY) {
        x = pX;
        y = pY;
        inTour = false;
    }

    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x - 7, y - 7, 14, 14);
        g.setColor(Color.BLACK);
        g.drawOval(x - 7, y - 7, 14, 14);
    }
}