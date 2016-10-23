package interpolacjakrzywymisklejanymi;

//import javax.script.ScriptException;

import ptolemy.plot.Plot;
import ptolemy.plot.PlotApplication;
import Jama.Matrix;

/**
 *
 * @author Michal Bokiniec
 */
public class InterpolacjaKrzywymiSklejanymi {

    static Plot wykres = new Plot();

    static Double x[] = {1.0, 2.0, 4.0, 7.0};
    static Double y[] = {1.0, 5.0, 2.0, 1.0};
    
    
    public static void main(String[] args) throws ScriptException {
    	// przygotowywanie wykresow
        wykres.setTitle("Krzywe Sklejane");
        wykres.setXLabel("x");
        wykres.setYLabel("y");
        wykres.setMarksStyle("dots", 0);
        wykres.setMarksStyle("dots", 1);
        wykres.setMarksStyle("dots", 2);
        wykres.setMarksStyle("dots", 3);
        
        
        // deklaracja macierzy
        Matrix A = new Matrix(new double[x.length][x.length]);
        Matrix Z = new Matrix(new double[x.length][1]);
        Matrix V = new Matrix(new double[x.length][1]);        
        
        
        // wypelnianie macierzy glownej
        for (int i = 0; i < x.length; i++) {
        	wykres.addPoint(0, x[i], y[i], false); // wyswietlanie punktow na czerwono
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
        }
        
        // wyznaczanie wartosci drugich pochodnych
        Z = A.solve(V);
        
        System.out.println("A: " ); A.print(5,6);
        System.out.println("Z: " ); Z.print(5,6);
        System.out.println("V: " ); V.print(5,6);
            
        
        // rysowanie wykresow
        for (int i = 0; i < x.length - 1 ;i++) {
            double tc = x[i];        // t_i
            double tn = x[i + 1];    // t_i+1
            double hc = tn - tc;     // h_i
                
            double zc = Z.get(i, 0);    // z_i
            double zn = Z.get(i+1, 0);  // z_i+1
            
            double yc = y[i];           // y_i
            double yn = y[i+1];         // y_i+1
            
            for (double x_ = x[i]; x_ < x[i+1]; x_ += (x[i+1] - x[i])/10.0 ) {
                Double result = (zc/(6*hc))*Math.pow(tn - x_, 3)
                        + (zn/(6*hc))*Math.pow(x_ - tc, 3)
                        + (yn/hc - zn*hc/6)*(x_ - tc)
                        + (yc/hc - zc*hc/6)*(tn - x_);
                wykres.addPoint(3, x_, result, false);
                System.out.println(x_ + " : " + result);
            }
            
        }
        
        PlotApplication plotApp = new PlotApplication(wykres);
        plotApp.setVisible(true);
    }
}
