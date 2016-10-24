/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optymalizacjasumyliczbzmiennoprzecinkowych;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Studwcy
 */
public class OptymalizacjaSumyLiczbZmiennoprzecinkowych {
    static Random generator = new Random();
    static int n = 1000;
    static int qn = 1000;
    static Float x[] = new Float[n];
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double[] sumaBledow = new double[4];
        
        for (int q = 0; q<qn; q++) {
            Number[] wynik = liczenieWartosci(x);
            
            for (int w = 0; w<4; w++) {
                //System.out.println(w);
                sumaBledow[w] +=  Math.abs(wynik[4].doubleValue() - wynik[w].doubleValue());
                //System.out.println(Math.abs(wynik[4].doubleValue() - wynik[w].doubleValue()));
            }
            //System.out.println("\n");
            
        }
        
        System.out.println(sumaBledow[0]/(n*qn) + " - sredni blad sumy wektora nieposortowanego, liczonej od poczatku.");
        System.out.println(sumaBledow[1]/(n*qn) + " - sredni blad sumy wektora nieposortowanego, liczonej od konca.");
        System.out.println(sumaBledow[2]/(n*qn) + " - sredni blad sumy wektora sortowanego wg modulu, liczonej od poczatku.");
        System.out.println(sumaBledow[3]/(n*qn) + " - sredni blad sumy wektora sortowanego wg modulu, liczonej od konca.");
        
        
        /*Float sumaP = zwrocSumeP(x);
        Float sumaK = zwrocSumeK(x);
        
        
        Float sumaSortowanaP = zwrocSumeSortP(x);
        Float sumaSortowanaK = zwrocSumeSortK(x);
        
        
        Double sumaSortowanaP_double = zwrocSumeSortP_double(x);*/
        
        /*System.out.println((Float)wynik[0] + " : + " + Math.abs((Double)wynik[4] - (Float)wynik[0]) + " - suma liczona od poczatku.");
        System.out.println((Float)wynik[1] + " : + " + Math.abs((Double)wynik[4] - (Float)wynik[1]) + " - suma liczona od konca.");
        
        
        System.out.println((Float)wynik[2] + " : + " + Math.abs((Double)wynik[4] - (Float)wynik[2]) + " - suma posortowanej tablicy (najmniejszy blad).");
        System.out.println((Float)wynik[3] + " : + " + Math.abs((Double)wynik[4] - (Float)wynik[3]) + " - suma posortowanej tablicy liczona od konca.");
        System.out.println((Double)wynik[4] + " - suma posortowanej tablicy liczb double.");*/
        
        
        
        
    }
    
    
    static Float zwrocSumeP(Float wektor[]) {
        Float s = 0.0f;
        int j = wektor.length;
        
        for(int i=0; i<j; i++) {
            s += wektor[i];
        }
        return s;
    }
    
    static Float zwrocSumeK(Float wektor[]) {
        Float s = 0.0f;
        int j = wektor.length;
        
        for(int i=0; i<j; i++) {
            s += wektor[j-i-1];
        }
        return s;
    }
        
    
    static Float zwrocSumeSortP(Float wektor[]) {
        Float s = 0.0f;
        int j = wektor.length;
        //Float wektorSort[] = wektor.clone();
        List<Float> wektorSort_ = Arrays.asList(wektor.clone());
                
        Collections.sort(wektorSort_, (Float p1, Float p2) -> {
            Float a = p1*p1; Float b = p2*p2;
            return a.compareTo(b);
        });
        
        Float wektorSort[] = wektorSort_.toArray(new Float[j]);
        
        for(int i=0; i<j; i++) {
            s += wektorSort[i];
            //System.out.println(wektorSort[i] + "\n");
        }
        return s;
    }
        
    static Float zwrocSumeSortK(Float wektor[]) {
        Float s = 0.0f;
        int j = wektor.length;
        //Float wektorSort[] = wektor.clone();
        List<Float> wektorSort_ = Arrays.asList(wektor.clone());
                
        Collections.sort(wektorSort_, (Float p1, Float p2) -> {
            Float a = p1*p1; Float b = p2*p2;
            return a.compareTo(b);
        });
        
        Float wektorSort[] = wektorSort_.toArray(new Float[j]);
        
        for(int i=0; i<j; i++) {
            s += wektorSort[j-i-1];
        }
        return s;
    }
    
    
    
    
    
    static Double zwrocSumeSortP_double(Float wektor[]) {
        Double s = 0.0;
        int j = wektor.length;
        //Float wektorSort[] = wektor.clone();
        
        /*Double[] wektor_ = new Double[wektor.length];
        for (int i = 0; i < wektor.length; i++)
        {
            wektor_[i] = wektor[i].doubleValue();
        }*/
        
        List<Float> wektorSort_ = Arrays.asList(wektor);
                
        Collections.sort(wektorSort_, (Float p1, Float p2) -> {
            Float a = p1*p1; Float b = p2*p2;
            return a.compareTo(b);
        });
        
        Float wektorSort[] = wektorSort_.toArray(new Float[j]);
        
        for(int i=0; i<j; i++) {
            s += wektorSort[i];
            //System.out.println(wektorSort[i] + "\n");
        }
        return s;
    }
    
    static Number[] liczenieWartosci(Float[] x) {
        for(int i=0; i<n; i++) {
            x[i] = (generator.nextFloat() /*- 0.5f*/) * (generator.nextFloat() /*- 0.5f*/)*10000;
            //System.out.println(x[i]);
        }
        
        Float sumaP = zwrocSumeP(x);
        Float sumaK = zwrocSumeK(x);
        
        
        Float sumaSortowanaP = zwrocSumeSortP(x);
        Float sumaSortowanaK = zwrocSumeSortK(x);
        
        
        Double sumaSortowanaP_double = zwrocSumeSortP_double(x);
        
        Number wynik[] = {sumaP, sumaK, sumaSortowanaP, sumaSortowanaK, sumaSortowanaP_double};
        return wynik;
    }
}


