/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aproksymacjasredniokwadratowa;

import Jama.Matrix;
import Jama.QRDecomposition;
import java.awt.BorderLayout;

/**
 *
 * @author Michał Bokiniec
 */
public class AproksymacjaSredniokwadratowa {
    public static void main(String[] args) {
        // postac wielomianu aproksymujacego (kolejne potegi)
        int[] potegiWielomianu = {0, 2, 3, 4, 5};
        //int[] potegiWielomianu = {0, 2, 3};
        
        // punkty do aproksymacji
        double punkty[][] = {{1, 2}, {2, 3}, {3, 5}, {4, 3}, {5, 2}, {6,8}};
        //double punkty[][] = {{2,3}, {3,5}, {4,7}, {5,8}, {6,9}};
        
        // generowanie N (liczba poteg) i k (liczba punktow)
        int N = potegiWielomianu.length; // ilosc wspolczynnikow wielomianu
        int k = punkty.length; // ilosc punktow
        
        // deklarowanie tablic jako macierzy
        double[][] macierzGlowna = new double[k][N]; //X
        double[][] wspolczynnikiWielomianu = new double[N][1]; //A
        double[][] wspolrzedneY = new double[k][1]; //Y
 
        //wypelnianie macierzy glownej X
        for (int y = 0; y<k; y++) {
            for(int x = 0; x<N; x++) {
                macierzGlowna[y][x] = Math.pow(punkty[y][0], potegiWielomianu[x]);
                System.out.print("macierzGlowna["+x+"]["+y+"] = "+punkty[y][0]+"^"+potegiWielomianu[x]+"   ");
            }
            System.out.println("\n");
        }
        
        // wypelnianie macierzy wspolczynnikow Y
        for (int y = 0; y<k; y++) {
            wspolrzedneY[y][0] = punkty[y][1];
        }
        
        // generowanie macierzy JAMA
        // postac ukl. rownan: X*A = Y
        Matrix X = new Matrix(macierzGlowna);
        Matrix A = new Matrix(wspolczynnikiWielomianu);
        Matrix Y = new Matrix(wspolrzedneY);
        
        System.out.println("Macierz X"); X.print(2, 2);
        System.out.println("Macierz A"); A.print(2, 2);
        System.out.println("Macierz Y"); Y.print(2, 2);
        
        // dekompozycja QR
        QRDecomposition qr = X.qr();
        Matrix Q = qr.getQ(); Matrix R = qr.getR();
        
        System.out.println("Macierz Q"); Q.print(5, 5);
        System.out.println("Macierz R"); R.print(5, 5);
        
        //A = R.inverse().times(Q.transpose().times(Y));
        Matrix A_zSolwera = X.solve(Y); // obliczanie wyniku solverem - rownowazne z komentarzem wyzej
        
        // uzyskiwanie tablic z macierzy JAMA
        // rownanie ma postac: R * A = Q^T * Y     Q^T - macierz Q transponowana        
        double [][] QTY_arr = (Q.transpose().times(Y)).getArrayCopy(); //Q^T * Y
        double [][] R_arr = R.getArrayCopy();
        double [][] A_arr = A.getArrayCopy();
                
        // kod, ktory na podstawie powyzszych macierzy oblicza wspolczynniki macierzy A (podstawianie od dolu)
        for (int i = N-1; i>=0; i--) {
            System.out.println("i = " + i);
            double ai = QTY_arr[i][0];      System.out.println("QTY_arr["+i+"][0] = " + ai);
            for (int j = N-1; j>=0; j--) {
                if (i!=j) {
                    ai -= R_arr[i][j]*A_arr[j][0];      
                    System.out.println("ai -= "+R_arr[j][i]+"*"+A_arr[j][0]);
                }
            }
            A_arr[i][0] = ai / R_arr[i][i];
        }
        A = new Matrix(A_arr);
                
        //wyniki
        System.out.println("\nobliczona z algorytmu macierz A:"); A.print(5, 5);
        System.out.println("\nMacierz A z solvera:");         A_zSolwera.print(5, 5);
        
        System.out.println("Rownanie wielomianu aproksymujacego:"); System.out.print("f(x) = ");
        for (int i = 0; i<N; i++) {
            System.out.print( A.get(i,0) + "*x^" + potegiWielomianu[i]);
            if (i<N-1) {
                if (A.get(i+1,0)>=0) System.out.print(" + "); 
                else System.out.print(" ");
            }
            else 
                System.out.print("\n");
        }
    }
}