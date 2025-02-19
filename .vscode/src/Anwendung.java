import javax.swing.*;
import java.awt.*;

public class Anwendung {

    // Stadtlisten für beide Verfahren
    private final Stadtliste landNearestNeighbour;
    private final Stadtliste landNearestInsertion;

    // Timer für die Animation
    private Timer timer;

    public Anwendung() {
        // 1) Erzeuge beide Stadtlisten und berechne die Touren
        landNearestNeighbour = new Stadtliste();
        landNearestNeighbour.berechneTourNearestNeighbour();

        landNearestInsertion = new Stadtliste();
        landNearestInsertion.berechneTourNearestInsertion();

        // 2) Erstelle erstes Fenster (Nearest Neighbour)
        JFrame nnFrame = new JFrame("TSP: Nearest Neighbour-Methode");
        nnFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nnFrame.setSize(1000, 1000);

        // Panel für NN
        NearestNeighbourPanel nnPanel = new NearestNeighbourPanel(landNearestNeighbour);
        nnFrame.add(nnPanel);

        // 3) Erstelle zweites Fenster (Nearest Insertion)
        JFrame niFrame = new JFrame("TSP: Nearest Insertion-Methode");
        niFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        niFrame.setSize(1000, 1000);

        // Panel für NI
        NearestInsertionPanel niPanel = new NearestInsertionPanel(landNearestInsertion);
        niFrame.add(niPanel);

        // 4) Platziere die Fenster nebeneinander (einfaches Beispiel)
        nnFrame.setLocation(100, 100);
        nnFrame.setVisible(true);

        niFrame.setLocation(nnFrame.getX() + nnFrame.getWidth() + 20, 100);
        niFrame.setVisible(true);

        // 5) Timer, der jede Sekunde einen Schritt macht
        timer = new Timer(1000, e -> {
            // NN-Schritt
            boolean nnUpdated = landNearestNeighbour.nextStepNearestNeighbour();
            if (nnUpdated) {
                nnPanel.repaint(); // NN neu zeichnen
            }

            // NI-Schritt
            boolean niUpdated = landNearestInsertion.nextStepNearestInsertion();
            if (niUpdated) {
                niPanel.repaint(); // NI neu zeichnen
            }

            // Wenn beide Touren fertig sind, Timer stoppen
            if (landNearestNeighbour.isTourCompleteNearestNeighbour()
                    && landNearestInsertion.isTourCompleteNearestInsertion()) {
                timer.stop();
            }
        });
        timer.start();
    }

    // ----------------------------------------------------------------
    // Panel-Klasse für Nearest-Neighbour-Fenster
    // ----------------------------------------------------------------
    private static class NearestNeighbourPanel extends JPanel {
        private final Stadtliste stadtliste;

        public NearestNeighbourPanel(Stadtliste stadtliste) {
            this.stadtliste = stadtliste;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 1) NN-Tour zeichnen
            stadtliste.paintNearestNeighbour(g);

            // 2) Textausgabe der aktuellen bzw. Gesamtstrecke
            g.setColor(Color.BLACK);
            if (!stadtliste.isTourCompleteNearestNeighbour()) {
                // Noch nicht fertig --> aktuelle Länge
                double currentLen = stadtliste.getCurrentLengthNN();
                g.drawString(String.format("Aktuelle Länge: %.2f", currentLen), 10, 20);
            } else {
                // Tour vollständig --> Gesamtlänge
                double totalLen = stadtliste.getTotalLengthNN();
                g.drawString(String.format("Gesamtlänge: %.2f", totalLen), 10, 20);
            }
        }
    }

    // ----------------------------------------------------------------
    // Panel-Klasse für Nearest-Insertion-Fenster
    // ----------------------------------------------------------------
    private static class NearestInsertionPanel extends JPanel {
        private final Stadtliste stadtliste;

        public NearestInsertionPanel(Stadtliste stadtliste) {
            this.stadtliste = stadtliste;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 1) NI-Tour zeichnen
            stadtliste.paintNearestInsertion(g);

            // 2) Textausgabe der aktuellen bzw. Gesamtstrecke
            g.setColor(Color.BLACK);
            if (!stadtliste.isTourCompleteNearestInsertion()) {
                // Noch nicht fertig --> aktuelle Länge
                double currentLen = stadtliste.getCurrentLengthNI();
                g.drawString(String.format("Aktuelle Länge: %.2f", currentLen), 10, 20);
            } else {
                // Tour vollständig --> Gesamtlänge
                double totalLen = stadtliste.getTotalLengthNI();
                g.drawString(String.format("Gesamtlänge: %.2f", totalLen), 10, 20);
            }
        }
    }

    // ----------------------------------------------------------------
    // main-Methode zum Starten
    // ----------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Anwendung::new);
    }
}
