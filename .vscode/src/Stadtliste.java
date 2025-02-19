import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Stadtliste {
    Stadt[] liste;
    int[] tourNearestNeighbour; // Tour für Nearest Neighbour
    int[] tourNearestInsertion; // Tour für Nearest Insertion
    int currentStepNearestNeighbour; // Aktueller Schritt für Nearest Neighbour
    int currentStepNearestInsertion; // Aktueller Schritt für Nearest Insertion

    public Stadtliste() {
        // Initialisiere das Array der Städte
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

        currentStepNearestNeighbour = 0;
        currentStepNearestInsertion = 0;
    }

    // Methode zur Berechnung der Tour mit Nearest Neighbour
    public void berechneTourNearestNeighbour() {
        // Setze alle Städte auf "nicht in Tour"
        for (Stadt stadt : liste) {
            stadt.inTour = false;
        }
        int anzahl = liste.length;
        tourNearestNeighbour = new int[anzahl + 1];

        int currentIndex = 0;
        liste[currentIndex].inTour = true;
        tourNearestNeighbour[0] = currentIndex;

        for (int i = 1; i < anzahl; i++) {
            int nextIndex = findNearestNeighbour(currentIndex);
            if (nextIndex == -1)
                break;
            tourNearestNeighbour[i] = nextIndex;
            currentIndex = nextIndex;
            liste[currentIndex].inTour = true;
        }
        // Schließe die Tour, indem wir zum Start zurückkehren
        tourNearestNeighbour[anzahl] = tourNearestNeighbour[0];
    }

    // Methode zur Berechnung der Tour mit Nearest Insertion
    public void berechneTourNearestInsertion() {
        for (Stadt stadt : liste) {
            stadt.inTour = false;
        }
        List<Integer> tour = new ArrayList<>();

        // Starte mit einer Stadt, z.B. Index 0
        tour.add(0);
        liste[0].inTour = true;

        // Solange nicht alle Städte in der Tour sind
        while (tour.size() < liste.length) {
            int nearestCity = -1;
            int insertPosition = -1;
            double minCost = Double.MAX_VALUE;

            // Finde die Stadt mit den geringsten Einfügekosten in die bestehende Tour
            for (int i = 0; i < liste.length; i++) {
                if (liste[i].inTour)
                    continue; // schon in Tour, überspringen

                // Probiere alle möglichen Einfügepositionen in "tour" durch
                for (int j = 0; j < tour.size(); j++) {
                    int cityA = tour.get(j);
                    int cityB = tour.get((j + 1) % tour.size());
                    double cost = distance(liste[cityA], liste[i])
                            + distance(liste[i], liste[cityB])
                            - distance(liste[cityA], liste[cityB]);
                    if (cost < minCost) {
                        minCost = cost;
                        nearestCity = i;
                        insertPosition = j + 1;
                    }
                }
            }

            // Füge die gefundene Stadt an der besten Position ein
            if (nearestCity != -1) {
                tour.add(insertPosition, nearestCity);
                liste[nearestCity].inTour = true;
            }
        }

        // Konvertiere die Tour (List<Integer>) in ein Array
        tourNearestInsertion = new int[tour.size() + 1];
        for (int i = 0; i < tour.size(); i++) {
            tourNearestInsertion[i] = tour.get(i);
        }
        tourNearestInsertion[tour.size()] = tour.get(0); // Zurück zum Start
    }

    // Methode zum Fortschreiten zum nächsten Schritt (Nearest Neighbour)
    public boolean nextStepNearestNeighbour() {
        if (currentStepNearestNeighbour < tourNearestNeighbour.length - 1) {
            currentStepNearestNeighbour++;
            return true;
        }
        return false;
    }

    // Methode zum Fortschreiten zum nächsten Schritt (Nearest Insertion)
    public boolean nextStepNearestInsertion() {
        if (currentStepNearestInsertion < tourNearestInsertion.length - 1) {
            currentStepNearestInsertion++;
            return true;
        }
        return false;
    }

    // Methode zum Zeichnen der Tour (Nearest Neighbour)
    public void paintNearestNeighbour(Graphics g) {
        if (tourNearestNeighbour != null && tourNearestNeighbour.length > 1) {
            g.setColor(Color.BLUE);
            for (int i = 0; i < currentStepNearestNeighbour; i++) {
                Stadt current = liste[tourNearestNeighbour[i]];
                Stadt next = liste[tourNearestNeighbour[i + 1]];
                g.drawLine(current.x, current.y, next.x, next.y);
            }
        }
        // Zeichne alle Städte
        for (Stadt stadt : liste) {
            stadt.paint(g);
        }
    }

    // Methode zum Zeichnen der Tour (Nearest Insertion)
    public void paintNearestInsertion(Graphics g) {
        if (tourNearestInsertion != null && tourNearestInsertion.length > 1) {
            g.setColor(Color.GREEN);
            for (int i = 0; i < currentStepNearestInsertion; i++) {
                Stadt current = liste[tourNearestInsertion[i]];
                Stadt next = liste[tourNearestInsertion[i + 1]];
                g.drawLine(current.x, current.y, next.x, next.y);
            }
        }
        // Zeichne alle Städte
        for (Stadt stadt : liste) {
            stadt.paint(g);
        }
    }

    // Methode zur Überprüfung, ob die Tour vollständig ist (Nearest Neighbour)
    public boolean isTourCompleteNearestNeighbour() {
        return currentStepNearestNeighbour == tourNearestNeighbour.length - 1;
    }

    // Methode zur Überprüfung, ob die Tour vollständig ist (Nearest Insertion)
    public boolean isTourCompleteNearestInsertion() {
        return currentStepNearestInsertion == tourNearestInsertion.length - 1;
    }

    // Methode zum Finden des nächsten Nachbarn (Nearest Neighbour)
    private int findNearestNeighbour(int currentIndex) {
        Stadt current = liste[currentIndex];
        double minDist = Double.MAX_VALUE;
        int nearestIndex = -1;

        for (int i = 0; i < liste.length; i++) {
            if (i == currentIndex || liste[i].inTour)
                continue;
            double dist = distance(current, liste[i]);
            if (dist < minDist) {
                minDist = dist;
                nearestIndex = i;
            }
        }
        return nearestIndex;
    }

    // Methode zur Berechnung der Distanz zwischen zwei Städten
    private double distance(Stadt a, Stadt b) {
        int dx = a.x - b.x;
        int dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
