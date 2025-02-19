import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Stadtliste {
    Stadt[] liste;

    int[] tourNearestNeighbour; // Reihenfolge der NN-Tour (plus letztes Element = Start wiederholt)
    int[] tourNearestInsertion; // Reihenfolge der NI-Tour (plus letztes Element = Start wiederholt)

    int currentStepNearestNeighbour; // Anzahl gezeichneter Kanten für NN
    int currentStepNearestInsertion; // Anzahl gezeichneter Kanten für NI

    // ----------------------------------------------------------------
    // Konstruktor: Beispielhaft 16 Städte
    // ----------------------------------------------------------------
    public Stadtliste() {
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

    // ----------------------------------------------------------------
    // Nearest-Neighbour: Tour berechnen
    // ----------------------------------------------------------------
    public void berechneTourNearestNeighbour() {
        // Alle Städte zunächst auf "nicht in Tour"
        for (Stadt stadt : liste) {
            stadt.inTour = false;
        }
        int anzahl = liste.length;
        tourNearestNeighbour = new int[anzahl + 1];

        // Starte bei Stadt 0
        int currentIndex = 0;
        liste[currentIndex].inTour = true;
        tourNearestNeighbour[0] = currentIndex;

        // Füge wiederholt den nächsten Nachbarn hinzu
        for (int i = 1; i < anzahl; i++) {
            int nextIndex = findNearestNeighbour(currentIndex);
            if (nextIndex == -1)
                break;
            tourNearestNeighbour[i] = nextIndex;
            currentIndex = nextIndex;
            liste[currentIndex].inTour = true;
        }
        // Tour schließen
        tourNearestNeighbour[anzahl] = tourNearestNeighbour[0];
    }

    // Sucht die nächste noch nicht inTour befindliche Stadt, die am nächsten dran
    // ist
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

    // ----------------------------------------------------------------
    // Nearest-Insertion: Tour berechnen
    // ----------------------------------------------------------------
    public void berechneTourNearestInsertion() {
        // Alle Städte zunächst auf "nicht in Tour"
        for (Stadt stadt : liste) {
            stadt.inTour = false;
        }

        // Wir verwalten die Tour zunächst in einer ArrayList<Integer>
        List<Integer> tour = new ArrayList<>();
        // Starte mit Stadt 0
        tour.add(0);
        liste[0].inTour = true;

        // Solange nicht alle Städte in Tour sind
        while (tour.size() < liste.length) {
            int nearestCity = -1;
            int insertPosition = -1;
            double minCost = Double.MAX_VALUE;

            // Prüfe jede Stadt, die noch nicht in Tour ist:
            for (int i = 0; i < liste.length; i++) {
                if (liste[i].inTour)
                    continue; // schon drin -> überspringen

                // Teste alle möglichen Einfügepositionen in "tour"
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

        // Array für die Tour (+1 für den Rückweg zur Startstadt)
        tourNearestInsertion = new int[tour.size() + 1];
        for (int i = 0; i < tour.size(); i++) {
            tourNearestInsertion[i] = tour.get(i);
        }
        tourNearestInsertion[tour.size()] = tour.get(0); // zurück zum Start
    }

    // ----------------------------------------------------------------
    // Schrittweises Weiterschalten (Animation)
    // ----------------------------------------------------------------
    public boolean nextStepNearestNeighbour() {
        if (currentStepNearestNeighbour < tourNearestNeighbour.length - 1) {
            currentStepNearestNeighbour++;
            return true;
        }
        return false;
    }

    public boolean nextStepNearestInsertion() {
        if (currentStepNearestInsertion < tourNearestInsertion.length - 1) {
            currentStepNearestInsertion++;
            return true;
        }
        return false;
    }

    // ----------------------------------------------------------------
    // Zeichnen: Nearest-Neighbour
    // ----------------------------------------------------------------
    public void paintNearestNeighbour(Graphics g) {
        if (tourNearestNeighbour != null && tourNearestNeighbour.length > 1) {
            g.setColor(Color.BLUE);

            // Zeichne alle aktiven Segmente bis currentStepNearestNeighbour
            for (int i = 0; i < currentStepNearestNeighbour; i++) {
                Stadt current = liste[tourNearestNeighbour[i]];
                Stadt next = liste[tourNearestNeighbour[i + 1]];
                g.drawLine(current.x, current.y, next.x, next.y);
            }
        }

        // Zeichne alle Städte (rote Kreise)
        for (Stadt stadt : liste) {
            stadt.paint(g);
        }
    }

    // ----------------------------------------------------------------
    // Zeichnen: Nearest-Insertion (mit geschlossenem Teilzyklus)
    // ----------------------------------------------------------------
    public void paintNearestInsertion(Graphics g) {
        if (tourNearestInsertion != null && tourNearestInsertion.length > 1) {
            g.setColor(Color.GREEN);

            // Zeichne alle aktiven Segmente bis currentStepNearestInsertion
            int stepsToDraw = currentStepNearestInsertion;
            for (int i = 0; i < stepsToDraw; i++) {
                Stadt current = liste[tourNearestInsertion[i]];
                Stadt next = liste[tourNearestInsertion[i + 1]];
                g.drawLine(current.x, current.y, next.x, next.y);
            }

            // Schließe den Teilzyklus,
            // indem wir die letzte aktiv gezeichnete Stadt wieder mit der ersten verbinden
            if (stepsToDraw > 0) {
                Stadt last = liste[tourNearestInsertion[stepsToDraw]];
                Stadt first = liste[tourNearestInsertion[0]];
                g.drawLine(last.x, last.y, first.x, first.y);
            }
        }

        // Zeichne alle Städte (rote Kreise)
        for (Stadt stadt : liste) {
            stadt.paint(g);
        }
    }

    // ----------------------------------------------------------------
    // Aktueller Zustand: Ist die Tour vollständig?
    // ----------------------------------------------------------------
    public boolean isTourCompleteNearestNeighbour() {
        return currentStepNearestNeighbour == tourNearestNeighbour.length - 1;
    }

    public boolean isTourCompleteNearestInsertion() {
        return currentStepNearestInsertion == tourNearestInsertion.length - 1;
    }

    // ----------------------------------------------------------------
    // Längenberechnung (aktuelle / gesamte Länge)
    // ----------------------------------------------------------------

    // ============ NEAREST NEIGHBOUR ============

    // Aktuelle Länge (nur die schon gezeichneten Kanten)
    // + Rückverbindung für einen Teilzyklus, wenn gewünscht.
    public double getCurrentLengthNN() {
        double sum = 0.0;
        // summiere alle bereits gezeichneten Segmente
        for (int i = 0; i < currentStepNearestNeighbour; i++) {
            Stadt a = liste[tourNearestNeighbour[i]];
            Stadt b = liste[tourNearestNeighbour[i + 1]];
            sum += distance(a, b);
        }
        return sum;
    }

    // Gesamtlänge der fertigen Tour (wenn alles gezeichnet wäre)
    public double getTotalLengthNN() {
        double sum = 0.0;
        if (tourNearestNeighbour == null)
            return 0.0;

        // bis length-2, weil am Ende "tourNearestNeighbour[length-1]" == Startstadt
        for (int i = 0; i < tourNearestNeighbour.length - 1; i++) {
            Stadt a = liste[tourNearestNeighbour[i]];
            Stadt b = liste[tourNearestNeighbour[i + 1]];
            sum += distance(a, b);
        }
        return sum;
    }

    // ============ NEAREST INSERTION ============

    public double getCurrentLengthNI() {
        double sum = 0.0;
        // "stepsToDraw" = currentStepNearestInsertion
        for (int i = 0; i < currentStepNearestInsertion; i++) {
            Stadt a = liste[tourNearestInsertion[i]];
            Stadt b = liste[tourNearestInsertion[i + 1]];
            sum += distance(a, b);
        }
        // Falls wir schon mindestens 1 Kante haben, ist der Teil-Zyklus geschlossen
        if (currentStepNearestInsertion > 0) {
            int lastIdx = tourNearestInsertion[currentStepNearestInsertion];
            int firstIdx = tourNearestInsertion[0];
            sum += distance(liste[lastIdx], liste[firstIdx]);
        }
        return sum;
    }

    public double getTotalLengthNI() {
        double sum = 0.0;
        if (tourNearestInsertion == null)
            return 0.0;
        // tourNearestInsertion hat length = n+1, letztes Element = Startstadt
        for (int i = 0; i < tourNearestInsertion.length - 1; i++) {
            Stadt a = liste[tourNearestInsertion[i]];
            Stadt b = liste[tourNearestInsertion[i + 1]];
            sum += distance(a, b);
        }
        return sum;
    }

    // ----------------------------------------------------------------
    // Hilfsfunktion: Distanz zweier Städte
    // ----------------------------------------------------------------
    private double distance(Stadt a, Stadt b) {
        int dx = a.x - b.x;
        int dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
