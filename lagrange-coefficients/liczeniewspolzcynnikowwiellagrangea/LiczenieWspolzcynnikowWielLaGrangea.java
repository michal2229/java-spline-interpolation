package liczeniewspolzcynnikowwiellagrangea;

import java.util.Random;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import ptolemy.plot.Plot;
import ptolemy.plot.PlotApplication;

/**
 *
 * @author Michał
 */
public class LiczenieWspolzcynnikowWielLaGrangea {
    /**
     * @param args the command line arguments
     */
    static Plot wykres = new Plot();
    static Random generator = new Random();
    
    public static void main(String[] args) throws ScriptException {
        wykres.setTitle("LaGrange");
        wykres.setXLabel("x");
        wykres.setYLabel("y");
        wykres.setMarksStyle("dots", 0);
        
        double x[] = {1, 2, 3, 4, 5, 6, 7};
        
        double y[] = new double[x.length];
        
        for (int i = 0; i<y.length; i++) {
            y[i] = generator.nextDouble()*100;
            wykres.addPoint(0, x[i], y[i], false);
        }
        //double y[] = {0, -2, 5, -1, 4, 7, 3};
        
        
        
        double fi[] = new double[y.length];
        double a[] = new double[y.length];
        
        String wynik = "W(x) = " ;
        
        for (int iy = 0; iy<y.length; iy++) {
            String tempX = "";
            fi[iy] = 1;
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
        
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");        
//        double result = (double) engine.eval("var x = 4; 5+x");
//        System.out.println("engine eval: " + result);
    
        double step = 0.1;
        for (double i = x[0]/step; i<x[x.length-1]/step; i+=step) {
            double result = (double) engine.eval("var x = " + i*step + "; " + wynik.substring(7));
            wykres.addPoint(1, i*step, result, true);
        }
        
        
        
        
        // wykres.addPoint(0, x, y, true);
        
        PlotApplication plotApp = new PlotApplication(wykres);
        plotApp.setVisible(true);
    }
}