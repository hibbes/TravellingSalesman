import java.awt.*;

public class Stadt {
    // Koordinaten der Stadt
    public int x, y;
    // Flag, ob die Stadt bereits in der Tour enthalten ist
    public boolean inTour;

    // Konstruktor für die Stadt
    public Stadt(int pX, int pY) {
        x = pX; // Setze die x-Koordinate
        y = pY; // Setze die y-Koordinate
        inTour = false; // Standardmäßig ist die Stadt nicht in der Tour
    }

    // Methode zum Zeichnen der Stadt
    public void paint(Graphics g) {
        g.setColor(Color.RED); // Farbe für den inneren Kreis
        g.fillOval(x - 7, y - 7, 14, 14); // Zeichne einen gefüllten Kreis
        g.setColor(Color.BLACK); // Farbe für den Rand
        g.drawOval(x - 7, y - 7, 14, 14); // Zeichne den Rand des Kreises
    }
}
