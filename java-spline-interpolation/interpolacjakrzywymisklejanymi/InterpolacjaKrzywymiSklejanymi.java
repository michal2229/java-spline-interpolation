package interpolacjakrzywymisklejanymi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import ptolemy.plot.Plot;
import ptolemy.plot.PlotApplication;
import Jama.Matrix;
import Jama.QRDecomposition;

/**
 *
 * @author Michal Bokiniec
 */
public class InterpolacjaKrzywymiSklejanymi {
    static Plot wykres = new Plot();
    static Random generator = new Random();
    
    static final int iloscZmiennych = 10;
    static final int rozdzielczosc = 100;
    
//    static Double x[] = new Double[iloscZmiennych];
//    static Double y[] = new Double[x.length];
    
    static Double x[] = {1.0, 2.0, 4.0, 7.0};
    static Double y[] = {1.0, 5.0, 2.0, 1.0};
    
    static ScriptEngineManager manager = new ScriptEngineManager();
    static ScriptEngine engine = manager.getEngineByName("js");
    
    public static void main(String[] args) throws ScriptException {
        wykres.setTitle("Krzywe Sklejane");
        wykres.setXLabel("x");
        wykres.setYLabel("y");
        wykres.setMarksStyle("dots", 0);
        wykres.setMarksStyle("dots", 1);
        wykres.setMarksStyle("dots", 2);
        wykres.setMarksStyle("dots", 3);
        
        
        Matrix A = new Matrix(new double[x.length][x.length]);
        Matrix Z = new Matrix(new double[x.length][1]);
        Matrix V = new Matrix(new double[x.length][1]);
        //set(int i, int j, double s);
        
        
        
        // wypelnianie macierzy glownej
        for (int i = 0; i < x.length; i++)
            if (i >= 1 && i <= x.length - 2) {
                double tp = x[i - 1];       // t_i-1
                double tc = x[i];           // t_i
                double tn = x[i + 1];       // t_i+1
                double hp = tc - tp;        // h_i-1
                double hc = tn - tc;        // h_i
                double uc = 2*(hp + hc);    // u_i
                A.set(i, i - 1, hp);
                A.set(i, i, uc);
                A.set(i, i + 1, hc);
                
                double yp = y[i-1];         // y_i-1
                double yc = y[i];           // y_i
                double yn = y[i+1];         // y_i+1
                double bp = 6.0*(yc - yp)/hp;   // b_i-1
                double bc = 6.0*(yn - yc)/hc;   // b_i

                double vc = bc - bp;        // v_i
                V.set(i, 0, vc);
            }
            else {
                A.set(i, i, 1.0);
                V.set(i, 0, 0.0);
            }
        
        Z = A.solve(V);
        
        System.out.println("A: " ); A.print(5,6);
        System.out.println("Z: " ); Z.print(5,6);
        System.out.println("V: " ); V.print(5,6);
            
        
        // wyswietlanie wykresow
        for (int i = 0; i < x.length - 1 ;i++) {
            double tc = x[i];        // t_i
            double tn = x[i + 1];    // t_i+1
            double hc = tn - tc;     // h_i
                
            double zc = Z.get(i, 0);    // z_i
            double zn = Z.get(i+1, 0);  // z_i+1
            
            double yc = y[i];           // y_i
            double yn = y[i+1];         // y_i+1
            
            for (double x_ = x[i]; x_ < x[i+1]; x_ += (x[i+1] - x[i])/10.0 ) {
                
                
                Double result = (zc/6*hc)*Math.pow(tn - x_, 3)
                        + (zn/6*hc)*Math.pow(x_ - tc, 3)
                        + (yn/hc + zn*hc/6)*(x_ - tc)
                        + (yc/hc + zc*hc/6)*(tn - x_);
                wykres.addPoint(3, x_, result, false);
            }
            
        }
        
        //splineIrzedu();
        //laGrange();
      
        
        PlotApplication plotApp = new PlotApplication(wykres);
        plotApp.setVisible(true);
    }
    
    static void splineIrzedu() throws ScriptException {
        List<Double> listaX = new ArrayList<>();
        
        
        
        for (int i = 0; i<y.length; i++) {
            listaX.add(generator.nextDouble()*100);
            y[i] = generator.nextDouble()*100;
            
        }
        
        Collections.sort(listaX);
        
        x = listaX.toArray(x);
        
        for (int i = 0; i<y.length; i++) {
            wykres.addPoint(0, x[i], y[i], false);  
        }
        
        int _a = 0; int _b = 1;
        Double wspolczynniki[][] = new Double[x.length - 1][2]; //[N][a/b]
        for (int i = 0; i < x.length - 1; i++) {
            wspolczynniki[i][_a] = (y[i+1] - y[i])/(x[i+1] - x[i]);
            wspolczynniki[i][_b] = y[i] - wspolczynniki[i][_a]*x[i];
            
            System.out.println("odcinek " + i + " - " + (i+1) + " : a = " + wspolczynniki[i][_a] + ", b = " + wspolczynniki[i][_b]);
            double step = 1.0/rozdzielczosc ;
            for (Double j = 0.0; j<1.0; j+=step) {
                Double x_ = x[i]*j + x[i+1]*(1.0-j);
                Double result = (Double) engine.eval("var x = " + x_ + "; var a = " + wspolczynniki[i][_a] + ";var b = " + wspolczynniki[i][_b] + "; a*x+b" );
                wykres.addPoint(2, x_, result, false);
            }
            
        }
    }
    static void laGrange() throws ScriptException {
        Double fi[] = new Double[y.length];
        Double a[] = new Double[y.length];
        
        String wynik = "W(x) = " ;
        
        for (int iy = 0; iy<y.length; iy++) {
            String tempX = "";
            fi[iy] = 1.0;
            for (int ix = 0; ix<x.length; ix++) {
                if(ix != iy) {
                    fi[iy] *= x[iy] - x[ix];
                    tempX += "*(x - "+x[ix]+")";
                    System.out.println("mnozenie fi["+iy+"] *= x["+iy+"] - x["+ix+"]  :  "+fi[iy]+" *= "+x[iy]+" - "+x[ix]);
                } 
            }
            System.out.println("fi[" + iy + "] = " + fi[iy]);
            a[iy] = y[iy] / fi[iy];
            System.out.println("a[iy] = y[iy] / fi[iy] : " + a[iy] + " = " + y[iy] + " / " + fi[iy]);
            wynik += a[iy] + tempX; if (iy < y.length -1) wynik += "  +  ";
        }
        System.out.println(wynik);
        
                
//        Double result = (Double) engine.eval("var x = 4; 5+x");
//        System.out.println("engine eval: " + result);
        double step = Math.abs(x[0] -x[x.length-1])/(iloscZmiennych * rozdzielczosc);
        for (double i = x[0]; i<x[x.length-1]; i+= step)  {
            Double result = (Double) engine.eval("var x = " + i + "; " + wynik.substring(7));
            wykres.addPoint(1, i, result, false);
        }
    }
}
