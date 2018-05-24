package ce.inu.ikta;

import android.util.Log;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAPodState;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

public class wolframalpha {

    // 울프램알파 app id 입력
    private static String appid = "E5AKEG-TPQ2KK5A72";
    final String TAG = "wolfram";
    wolfData output;
    public wolfData requestWolf(String input) {
        WAEngine engine = new WAEngine();
        engine.setAppID( appid );
        engine.addFormat( "plaintext" );
        WAQuery query = engine.createQuery();
        query.setInput( input );
        String sinput = "empty";
        String answer = "empty";
        String graph = "empty";
        Log.d( TAG, "시작" );

        try {
            Log.d( TAG, "try 진입" );
            WAQueryResult queryResult = engine.performQuery( query );
            if (queryResult.isError()) {
                Log.d( TAG, "  error message: " + queryResult.getErrorMessage() );
            } else if (!queryResult.isSuccess()) {
                Log.d( TAG, "Query was not understood; no results available." );
            } else {
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        String podtitle = pod.getTitle();
                        if (podtitle.contains( "Input" ) == true) {
                            for (WASubpod subpod : pod.getSubpods()) {
                                for (Object element : subpod.getContents()) {
                                    if (element instanceof WAPlainText) {
                                        sinput = ((WAPlainText) element).getText();
                                    }
                                }
                            }
                            System.out.println( "" );
                        }
                        else if (podtitle.contains( "Decimal approximation" ) == true) {
                            for (WASubpod subpod : pod.getSubpods()) {
                                for (Object element : subpod.getContents()) {
                                    if (element instanceof WAPlainText) {
                                        answer = ((WAPlainText) element).getText();
                                    }
                                }
                            }
                            System.out.println( "" );
                        }
                        else if (podtitle.contains( "Solution" ) == true || podtitle.contains( "Result" ) == true || podtitle.contains( "solution" ) == true) {
                            for (WASubpod subpod : pod.getSubpods()) {
                                for (Object element : subpod.getContents()) {
                                    if (element instanceof WAPlainText) {
                                        if (answer != "empty") {
                                            answer = answer + '\n' + ((WAPlainText) element).getText();
                                        }
                                        else
                                            answer =((WAPlainText) element).getText();
                                    }
                                }
                            }
                            System.out.println( "" );
                        }
                            else
                            System.out.println( "" );
                        answer = answer.replace( "sqrt","√" );
                    }
                }
            }

            Log.d( TAG, "try종료" );
        } catch (WAException e) {
            e.printStackTrace();
        }


        output = new wolfData( sinput, answer, graph);
        return output;
    }

    public wolfData Wolfoutput(final String input) {
        final wolfData[] dataout = new wolfData[1];

        Thread a = new Thread() {
            public void run() {
                String in1 = input;
                Log.d( TAG, in1 );
                dataout[0] = requestWolf(in1);
                Log.d(TAG, dataout[0].answer);
                Log.d(TAG, dataout[0].input);
            }
        };
        a.start();
        try {
            a.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d( TAG, "스레드종료" );
        output =dataout[0];
        return output;
    }
}
