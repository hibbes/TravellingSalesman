import java.awt.*;
import javax.swing.*;

public class Anwendung extends JFrame {
    // Liste der Städte
    Stadtliste land;

    // Konstruktor für das Hauptfenster
    public Anwendung() {
        land = new Stadtliste(); // Initialisiere die Stadtliste
        land.berechneTour(); // Berechne die Tour mit dem Nearest-Neighbour-Algorithmus
        setSize(1000, 1000); // Setze die Fenstergröße auf 1000x1000 Pixel
        setTitle("TSP: Nearest Neighbour-Methode"); // Titel des Fensters
        setResizable(false); // Verhindere, dass das Fenster in der Größe geändert wird
        setVisible(true); // Mache das Fenster sichtbar

        // Timer für den animierten Aufbau der Linien
        Timer timer = new Timer(1000, e -> {
            land.nextStep(); // Gehe zum nächsten Schritt der Tour
            repaint(); // Aktualisiere die Darstellung
        });
        timer.start(); // Starte den Timer
    }

    // Methode zum Zeichnen des Fensterinhalts
    public void paint(Graphics g) {
        super.paint(g); // Zeichne den Hintergrund und andere Komponenten des Fensters
        land.paint(g); // Zeichne die Städte und die Tour
    }

    // Hauptmethode zum Starten der Anwendung
    public static void main(String[] args) {
        new Anwendung(); // Erstelle eine Instanz der Anwendung
    }
}