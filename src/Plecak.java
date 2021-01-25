import java.text.*;
import java.util.*;

public class Plecak {

    private static DecimalFormat waga = new DecimalFormat("0.00kg");

    static final double MAX_CIEZAR = 30;

    static final int MAX_WARTOSC = 190;

    static int liczebnoscObiektow = 5;

    static double[][] wyborObiektow = null;

    static int pojemnosc = 50;

    boolean[] wybor = null;

    Plecak () {
        wybor = new boolean[liczebnoscObiektow];
        for (int i = 0; i < wybor.length; i ++)
            wybor[i] = false;
    }

    Plecak (Plecak r) {
        wybor = new boolean[liczebnoscObiektow];
        for (int i = 0; i < wybor.length; i ++)
            wybor[i] = r.wybor[i];
    }

    double ciezar () {        double g = 0;
        for (int i=0; i < wybor.length; i ++)
            if (wybor[i] == true)
                g = g + wyborObiektow[i][0];
        return g;
    }

    int wartosc () {        int n = 0;
        for (int i=0; i < wybor.length; i ++)
            if (wybor[i] == true)
                n = n + (int) wyborObiektow[i][1];
        return n;
    }


    public String toString() {        String r = "|";
        for (int i=0; i < wybor.length; i++)
            r = r + (wybor[i] ? "1" : "0") + "|";
        r = r + " Ciezar: " + waga.format(ciezar())
                + " Wartosc: " + wartosc();
        return r;
    }

    static double[][] wypelnijObiektami() {
        java.util.Random ra = new java.util.Random();
        double[][] r = new double[liczebnoscObiektow][2];
        for (int i=0; i < r.length; i++) {
            r[i][0]= (ra.nextDouble() * MAX_CIEZAR) + 0.5;            r[i][1]= ra.nextInt(MAX_WARTOSC) + 10;        }
        return r;
    }

    static String obiektyToString(double[][] a) {
        String r = "Wybor obiektow: ";
        for (int i=0; i < a.length; i++)
            r = r + "(" + waga.format(a[i][0]) + "," + a[i][1] +")";
        return r;
    }

    Plecak mutacja() {               java.util.Random ra = new java.util.Random();
        int pos = ra.nextInt(wybor.length);        Plecak r = new Plecak(this);
        r.wybor[pos] = (!wybor[pos]);
        return r;
    }

    Plecak krzyzowanie(Plecak partner) {               java.util.Random ra = new java.util.Random();
        int pos = ra.nextInt(wybor.length);        Plecak r = new Plecak();
        for (int i=0; i < pos; i++)            r.wybor[i] = wybor[i];
        for (int i=pos; i < wybor.length; i++)
            r.wybor[i] = partner.wybor[i];
        return r;
    }

    int dopasowanie() {        if (ciezar() > pojemnosc) return 0;
        else return wartosc();
    }

    static Plecak algorytm(int metoda_sel) {

        int liczba_oso = 10;
        int liczba_osob = liczba_oso + 1;
        int liczba_epok = 200;
        int epoka = 0;

        Plecak[] populacja = new Plecak[liczba_osob];

        for (int i = 0; i < liczba_osob; i++) {
            populacja[i] = new Plecak();
        }

        while(epoka < liczba_epok) {

            if(metoda_sel == 1){
                int totalFitness = 1;
                int ruleta[][] = new int[liczba_osob][2];
                for (int i = 0 ; i < liczba_osob ; i++){
                    ruleta[i][0] = totalFitness + 1;
                    totalFitness += populacja[i].dopasowanie();
                    ruleta[i][1] = totalFitness;
                }
                java.util.Random ra = new java.util.Random();
                for(int i = 0 ; i < liczba_oso ; i++){
                    int rand = ra.nextInt(totalFitness);
                    for(int j = 0 ; j < ruleta.length ; j++){
                        if(ruleta[j][0] <= rand && rand < ruleta[j][1]) { populacja[i]=populacja[j]; break; }
                    }
                }
            } else
            if(metoda_sel == 2){                                              ArrayList[] groups = new ArrayList[liczba_oso];
                for(int i = 0 ; i < liczba_oso ; i++) groups[i] =
                        new ArrayList((int)(liczba_osob / liczba_oso) + 1);
                int c = 0;
                for(int i = 0 ; i < liczba_osob ; i++){
                    int[][] dopasowanie = { {i,} , {populacja[i].dopasowanie(),} };
                    groups[c].add(dopasowanie);
                }
                for(int i = 0 ; i < liczba_oso ; i++){
                    int najlepszyZGrupy = 0;
                    int najlepszyFitness = 0;
                    Iterator it = groups[i].iterator ();
                    while (it.hasNext ()) {
                        int[][] a = (int[][]) it.next();
                        if(a[1][0] > najlepszyFitness ) {najlepszyZGrupy = a[0][0] ; najlepszyFitness = a[1][0];}
                    }
                    populacja[i] = populacja[najlepszyZGrupy];
                }

            }else            {
                for (int i = liczba_oso ; i < liczba_osob ; i ++) {
                    int gorsze_dop = Integer.MAX_VALUE;
                    int pos = -1;
                    for (int j = 0; j < liczba_oso; j ++)
                        if (populacja[i].dopasowanie() > populacja[j].dopasowanie()
                                && populacja[j].dopasowanie() < gorsze_dop) {
                            gorsze_dop = populacja[j].dopasowanie();
                            pos = j;
                        }
                    if (pos >= 0) populacja[pos] = populacja[i];
                }}

            for (int i = liczba_oso; i < liczba_osob; i++) {
                java.util.Random ra = new java.util.Random();
                if (ra.nextFloat() < 0.9) populacja[i] = populacja[i % liczba_oso].mutacja();
                if (ra.nextFloat() < 0.5) populacja[i] = populacja[i % liczba_oso].krzyzowanie(populacja[ra.nextInt(liczba_oso)]);
            }

            epoka++;
        }
        int pos = -1;
        int naj_dop = 0;
        for (int i = 0; i < liczba_osob; i ++)
            if (populacja[i].dopasowanie() > naj_dop) {
                naj_dop = populacja[i].dopasowanie();
                pos = i;
            };
        return populacja[pos];
    }

    public static void main (String args[]) {

        wyborObiektow = wypelnijObiektami();
        System.out.println(obiektyToString(wyborObiektow));
        System.out.println("Pojemnosc plecaka: " + pojemnosc + "kg");
        System.out.println();

        Plecak p1 = algorytm(0);
        System.out.println("Algorytm genetyczny, selekcja ranking liniowy: " + p1);
        System.out.println();

        Plecak p2 = algorytm(1);
        System.out.println("Algorytm genetyczny, selekcja kolo ruletki: " + p2);
        System.out.println();

        Plecak p3 = algorytm(2);
        System.out.println("Algorytm genetyczny, selekcja turniej: " + p3);
        System.out.println();
    }
}

