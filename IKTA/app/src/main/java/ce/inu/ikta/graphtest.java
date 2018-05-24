package ce.inu.ikta;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import javax.script.ScriptException;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.*;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class graphtest extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;
    final String TAG = "graph";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_graphtest );

        PrePlotString p= new PrePlotString();
        float y,x;
        x= -10.0f;
        String equ;
        equ = "y=x^0.5";
        GraphView graph = (GraphView) findViewById( R.id.GraphView);

        series = new LineGraphSeries<DataPoint>();
        equ = p.multiplyX(equ);
        for(int i = 0 ; i<=200; i++) {
            x = x * 10;
            x = Math.round( x );
            x = x / 10;
            try {
                Log.d( TAG, i + "번째" + x );
                y = (excute( equ, x ));
                series.appendData( new DataPoint( x, y ), true, 201 );
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            x = x + 0.1f;
        }
        graph.addSeries( series );
        graph.getViewport().setMinX(-10);
        graph.getViewport().setMaxX(10);
        graph.getViewport().setMinY(-10);
        graph.getViewport().setMaxY(10);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
    }

    protected float excute(String equ, float x) throws ScriptException {

        PrePlotString p= new PrePlotString();
        Log.d(TAG ,equ);
        String xequRep = equ.replace("a",Float.toString(x));
        xequRep = p.checkPow(xequRep);
        Log.d( TAG,"그래프 전이에요" + xequRep);
        Context rhino = Context.enter();

        rhino.setOptimizationLevel(-1);


        try {
            Scriptable scope = rhino.initStandardObjects();
            Object obj = rhino.evaluateString(scope, xequRep, "JavaScript", 1, null);
            String result = obj.toString();
            float res = Float.parseFloat(result);
            return res;
        } finally {
            Context.exit();
        }
    }
}
