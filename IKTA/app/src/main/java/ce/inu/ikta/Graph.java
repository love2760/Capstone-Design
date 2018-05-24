package ce.inu.ikta;

import javax.script.ScriptException;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.*;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class Graph{

    LineGraphSeries<DataPoint> series;
    final String TAG = "Graph";



    public void draw(String input, GraphView graphView) {
        PrePlotString p= new PrePlotString();
        double y,x;
        x= -10.0;
        String equ;
        equ = input;
        GraphView graph = graphView;

        series = new LineGraphSeries<DataPoint>();
        equ = equ.replace( " ","" );
        equ = equ.replace( "×","*" );
        equ = p.multiplyX(equ);
        for(int i = 0 ; i<=200; i++) {
            x = x * 100;
            x = Math.round( x );
            x = x / 100;
            try {
                y = (excute( equ, x ));
                series.appendData( new DataPoint( x, y ), true, 201 );
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            x = x + 0.1;
        }
        graph.addSeries( series );
        graph.getViewport().setMinX(-10);
        graph.getViewport().setMaxX(10);
        //graph.getViewport().setMinY(-10);
        //graph.getViewport().setMaxY(10);

        //graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
    }

    private double excute(String equ, double x) throws ScriptException {

        PrePlotString p= new PrePlotString();

        String xequRep = equ.replace("a",Double.toString(x));
        xequRep = p.checkPow(xequRep);
        Context rhino = Context.enter();

        rhino.setOptimizationLevel(-1);


        try {
            Scriptable scope = rhino.initStandardObjects();
            Object obj = rhino.evaluateString(scope, xequRep, "JavaScript", 1, null);
            String result = obj.toString();
            double res = Double.parseDouble(result);
            return res;
        } finally {
            Context.exit();
        }
    }
}
