import javax.swing.*;
import java.awt.*;

public class Anwendung {

    // Stadtlisten für beide Verfahren
    private Stadtliste landNearestNeighbour;
    private Stadtliste landNearestInsertion;

    // Timer für die Animation
    private Timer timer;

    public Anwendung() {
        // ------------------------------------------------------
        // 1) Stadtlisten erzeugen und die Touren berechnen
        // ------------------------------------------------------
        landNearestNeighbour = new Stadtliste();
        landNearestNeighbour.berechneTourNearestNeighbour();

        landNearestInsertion = new Stadtliste();
        landNearestInsertion.berechneTourNearestInsertion();

        // ------------------------------------------------------
        // 2) Erstes Fenster für Nearest Neighbour aufbauen
        // ------------------------------------------------------
        JFrame nnFrame = new JFrame("TSP: Nearest Neighbour-Methode");
        nnFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nnFrame.setSize(1000, 1000);

        // Panel für Nearest Neighbour
        NearestNeighbourPanel nnPanel = new NearestNeighbourPanel(landNearestNeighbour);
        nnFrame.add(nnPanel);

        // ------------------------------------------------------
        // 3) Zweites Fenster für Nearest Insertion aufbauen
        // ------------------------------------------------------
        JFrame niFrame = new JFrame("TSP: Nearest Insertion-Methode");
        niFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        niFrame.setSize(1000, 1000);

        // Panel für Nearest Insertion
        NearestInsertionPanel niPanel = new NearestInsertionPanel(landNearestInsertion);
        niFrame.add(niPanel);

        // ------------------------------------------------------
        // 4) Frames positionieren und sichtbar machen
        // (damit sie sich nicht überlappen)
        // ------------------------------------------------------
        nnFrame.setLocation(100, 100);
        nnFrame.setVisible(true);

        niFrame.setLocation(nnFrame.getX() + nnFrame.getWidth() + 20, 100);
        niFrame.setVisible(true);

        // ------------------------------------------------------
        // 5) Timer für schrittweise Animation
        // ------------------------------------------------------
        timer = new Timer(1000, e -> {
            // Schritt für Nearest Neighbour
            boolean nnUpdated = landNearestNeighbour.nextStepNearestNeighbour();
            if (nnUpdated) {
                nnPanel.repaint();
            }

            // Schritt für Nearest Insertion
            boolean niUpdated = landNearestInsertion.nextStepNearestInsertion();
            if (niUpdated) {
                niPanel.repaint();
            }

            // Wenn beide Touren fertig sind, stoppen wir den Timer
            if (landNearestNeighbour.isTourCompleteNearestNeighbour()
                    && landNearestInsertion.isTourCompleteNearestInsertion()) {
                timer.stop();
            }
        });

        timer.start();
    }

    // -----------------------------------------------
    // Panels als innere Klassen
    // -----------------------------------------------

    // Panel zum Zeichnen der Nearest-Neighbour-Tour
    private static class NearestNeighbourPanel extends JPanel {
        private final Stadtliste stadtliste;

        public NearestNeighbourPanel(Stadtliste stadtliste) {
            this.stadtliste = stadtliste;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Zeichne die bisherige NN-Tour
            stadtliste.paintNearestNeighbour(g);
        }
    }

    // Panel zum Zeichnen der Nearest-Insertion-Tour
    private static class NearestInsertionPanel extends JPanel {
        private final Stadtliste stadtliste;

        public NearestInsertionPanel(Stadtliste stadtliste) {
            this.stadtliste = stadtliste;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Zeichne die bisherige NI-Tour
            stadtliste.paintNearestInsertion(g);
        }
    }

    // -----------------------------------------------
    // main-Methode zum Starten der Anwendung
    // -----------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Anwendung::new);
    }
}
