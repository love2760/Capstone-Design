package ce.inu.ikta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.*;

public class graphtest extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;
    final String TAG = "graph";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_graphtest );

        double y,x;
        x= -5.0;
        String equ;
        equ = "y=x+1";
        GraphView graph = (GraphView) findViewById( R.id.GraphView);
        series = new LineGraphSeries<DataPoint>();

        for(int i = 0 ; i<500; i++)
        {
            x= x+0.1;
            try {
                y= (excute(equ,i));
                series.appendData( new DataPoint( x,y ),true,500 );
            } catch (ScriptException e) {
                e.printStackTrace();
            }

        }
        graph.addSeries(series);
    }

    protected Double excute(String equ, int i) throws ScriptException {
        PrePlotString p= new PrePlotString();
        String xequ = p.deleteEqual(equ);
        Log.d( TAG, xequ);
        String xequRep = xequ.replace("x",Integer.toString( i ));
        Log.d( TAG, xequRep);

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");

        final Double a = (Double) engine.eval( xequRep );
        return a ;
    }
}
