import java.awt.*;
import javax.swing.*;

public class Anwendung extends JFrame {
    Stadtliste landNearestNeighbour; // Stadtliste für Nearest Neighbour
    Stadtliste landNearestInsertion; // Stadtliste für Nearest Insertion
    Timer timer; // Timer für die Animation

    public Anwendung() {
        // Initialisiere beide Stadtlisten
        landNearestNeighbour = new Stadtliste();
        landNearestInsertion = new Stadtliste();

        // Berechne die Touren
        landNearestNeighbour.berechneTourNearestNeighbour();
        landNearestInsertion.berechneTourNearestInsertion();

        // Erstelle das erste Fenster für Nearest Neighbour
        setSize(1000, 1000);
        setTitle("TSP: Nearest Neighbour-Methode");
        setResizable(false);
        setVisible(true);

        // Erstelle das zweite Fenster für Nearest Insertion
        JFrame secondFrame = new JFrame();
        secondFrame.setSize(1000, 1000);
        secondFrame.setTitle("TSP: Nearest Insertion-Methode");
        secondFrame.setResizable(false);
        secondFrame.setVisible(true);

        // Timer für die Animation
        timer = new Timer(1000, e -> {
            if (landNearestNeighbour.nextStep()) { // Nächster Schritt für Nearest Neighbour
                repaint(); // Aktualisiere das erste Fenster
            }
            if (landNearestInsertion.nextStep()) { // Nächster Schritt für Nearest Insertion
                secondFrame.repaint(); // Aktualisiere das zweite Fenster
            }
            // Stoppe den Timer, wenn beide Touren vollständig sind
            if (landNearestNeighbour.isTourComplete() && landNearestInsertion.isTourComplete()) {
                timer.stop();
            }
        });
        timer.start();
    }

    // Methode zum Zeichnen des Fensterinhalts (Nearest Neighbour)
    public void paint(Graphics g) {
        super.paint(g);
        landNearestNeighbour.paint(g);
    }

    // Hauptmethode zum Starten der Anwendung
    public static void main(String[] args) {
        new Anwendung();
    }
}