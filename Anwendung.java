import java.awt.*;
import javax.swing.*;

public class Anwendung extends JFrame {
    Stadtliste land;

    public Anwendung() {
        land = new Stadtliste();
        setSize(500, 500);
        setTitle("TSP: Nearest Neighbour-Methode");
        setResizable(false);
        setVisible(true);
    }

    public void paint(Graphics g) {
        land.paint(g);
    }

    public static void main(String[] args) {
        new Anwendung();
    }
}