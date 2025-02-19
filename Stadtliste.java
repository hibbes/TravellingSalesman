import java.awt.*;

public class Stadtliste {
    // Array der Städte
    Stadt[] liste;
    // Array, das die Reihenfolge der Tour speichert
    int[] tour;
    // Aktueller Schritt im Linienaufbau
    int currentStep;

    // Konstruktor für die Stadtliste
    public Stadtliste() {
        // Initialisiere das Array der Städte mit doppelten Koordinaten
        liste = new Stadt[16];
        liste[0] = new Stadt(40, 100);
        liste[1] = new Stadt(80, 300);
        liste[2] = new Stadt(120, 180);
        liste[3] = new Stadt(160, 700);
        liste[4] = new Stadt(260, 240);
        liste[5] = new Stadt(260, 470);
        liste[6] = new Stadt(400, 840);
        liste[7] = new Stadt(440, 680);
        liste[8] = new Stadt(500, 260);
        liste[9] = new Stadt(560, 120);
        liste[10] = new Stadt(640, 360);
        liste[11] = new Stadt(700, 580);
        liste[12] = new Stadt(820, 500);
        liste[13] = new Stadt(860, 700);
        liste[14] = new Stadt(940, 380);
        liste[15] = new Stadt(920, 840);

        currentStep = 0; // Starte bei Schritt 0
    }

    // Methode zur Berechnung der Tour mit dem Nearest-Neighbour-Algorithmus
    public void berechneTour() {
        // Setze alle Städte als nicht in der Tour enthalten
        for (Stadt stadt : liste) {
            stadt.inTour = false;
        }
        int anzahl = liste.length;
        tour = new int[anzahl + 1]; // +1 für die Rückkehr zur Startstadt

        int currentIndex = 0; // Starte bei der ersten Stadt
        liste[currentIndex].inTour = true; // Markiere die Startstadt als in der Tour enthalten
        tour[0] = currentIndex; // Füge die Startstadt zur Tour hinzu

        // Berechne die Tour Schritt für Schritt
        for (int i = 1; i < anzahl; i++) {
            int nextIndex = findNearestNeighbour(currentIndex); // Finde den nächsten Nachbarn
            if (nextIndex == -1)
                break; // Fallback, falls keine Stadt gefunden wird
            tour[i] = nextIndex; // Füge den nächsten Nachbarn zur Tour hinzu
            currentIndex = nextIndex; // Setze den aktuellen Index auf den nächsten Nachbarn
            liste[currentIndex].inTour = true; // Markiere die Stadt als in der Tour enthalten
        }
        tour[anzahl] = tour[0]; // Rückkehr zur Startstadt
    }

    // Methode zum Finden des nächsten Nachbarn
    private int findNearestNeighbour(int currentIndex) {
        Stadt current = liste[currentIndex];
        double minDist = Double.MAX_VALUE; // Initialisiere die minimale Distanz
        int nearestIndex = -1; // Initialisiere den Index des nächsten Nachbarn

        // Durchlaufe alle Städte, um den nächsten Nachbarn zu finden
        for (int i = 0; i < liste.length; i++) {
            if (i == currentIndex || liste[i].inTour)
                continue; // Überspringe die aktuelle Stadt und bereits besuchte Städte
            double dist = distance(current, liste[i]); // Berechne die Distanz
            if (dist < minDist) {
                minDist = dist; // Aktualisiere die minimale Distanz
                nearestIndex = i; // Aktualisiere den Index des nächsten Nachbarn
            }
        }
        return nearestIndex; // Gib den Index des nächsten Nachbarn zurück
    }

    // Methode zur Berechnung der Distanz zwischen zwei Städten
    private double distance(Stadt a, Stadt b) {
        int dx = a.x - b.x; // Differenz in der x-Koordinate
        int dy = a.y - b.y; // Differenz in der y-Koordinate
        return Math.sqrt(dx * dx + dy * dy); // Euklidische Distanz
    }

    // Methode zum Fortschreiten zum nächsten Schritt der Tour
    public void nextStep() {
        if (currentStep < tour.length - 1) {
            currentStep++; // Erhöhe den aktuellen Schritt
        }
    }

    // Methode zum Zeichnen der Städte und der Tour
    public void paint(Graphics g) {
        // Zeichne die Linien der Tour bis zum aktuellen Schritt
        if (tour != null && tour.length > 1) {
            g.setColor(Color.BLUE); // Farbe für die Linien
            for (int i = 0; i < currentStep; i++) {
                Stadt current = liste[tour[i]]; // Aktuelle Stadt
                Stadt next = liste[tour[i + 1]]; // Nächste Stadt
                g.drawLine(current.x, current.y, next.x, next.y); // Zeichne eine Linie
            }
        }
        // Zeichne alle Städte
        for (Stadt stadt : liste) {
            stadt.paint(g);
        }
    }
}