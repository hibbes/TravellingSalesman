import java.awt.*;

public class Stadtliste {
    Stadt[] liste;

    public Stadtliste() {
        liste = new Stadt[16];

        liste[0] = new Stadt(20, 50);
        liste[1] = new Stadt(40, 150);
        liste[2] = new Stadt(60, 90);
        liste[3] = new Stadt(80, 350);
        liste[4] = new Stadt(130, 120);
        liste[5] = new Stadt(130, 235);
        liste[6] = new Stadt(200, 420);
        liste[7] = new Stadt(220, 340);
        liste[8] = new Stadt(250, 130);
        liste[9] = new Stadt(280, 60);
        liste[10] = new Stadt(320, 180);
        liste[11] = new Stadt(350, 290);
        liste[12] = new Stadt(410, 250);
        liste[13] = new Stadt(430, 350);
        liste[14] = new Stadt(470, 190);
        liste[15] = new Stadt(460, 420);
    }

    public void paint(Graphics g) {
        for (int i = 0; i < liste.length; i++)
            liste[i].paint(g);
    }

}