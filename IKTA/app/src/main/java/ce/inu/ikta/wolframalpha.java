package ce.inu.ikta;
import android.util.Log;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;
public class wolframalpha {

    // 울프램알파 app id 입력
    private static String appid = "E5AKEG-Q8EA9VLPE2";
    final String TAG = "wolfram";
    wolfData output;
    public wolfData requestWolf(String input) {
        WAEngine engine = new WAEngine();
        engine.setAppID(appid);
        engine.addFormat("plaintext");
        WAQuery query = engine.createQuery();
        query.setInput(input);
        String sinput ="empty";
        String answer ="empty";
        Log.d( TAG, "시작" );
        try {
            Log.d( TAG, "try 진입" );

            WAQueryResult queryResult = engine.performQuery(query);
            Log.d(TAG, "쿼리 결과 받앗어요");
            if (queryResult.isError()) {
                Log.d( TAG, "  error message: " + queryResult.getErrorMessage() );
            }
            else if (!queryResult.isSuccess()) {
                Log.d( TAG, "Query was not understood; no results available." );
            } else {
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        String podtitle = pod.getTitle();
                        if(podtitle.contains("Input")==true)
                        {
                            for (WASubpod subpod : pod.getSubpods()) {
                                for (Object element : subpod.getContents()) {
                                    if (element instanceof WAPlainText) {
                                        sinput = ((WAPlainText) element).getText();
                                    }
                                }
                            }
                            System.out.println("");
                        }
                        if(podtitle.contains("Solution")==true)
                        {
                            for (WASubpod subpod : pod.getSubpods()) {
                                for (Object element : subpod.getContents()) {
                                    if (element instanceof WAPlainText) {
                                        answer= ((WAPlainText) element).getText();
                                    }
                                }
                            }
                            System.out.println("");
                        }
                        else
                            System.out.println("");
                    }
                }
            }

            Log.d( TAG, "try종료" );
        }
        catch (WAException e) {
            e.printStackTrace();
        }


        output = new wolfData(sinput,answer,"empty","empty");
        return output;
    }

    public wolframalpha() {

    }
}
  /*   public static void main(String[] args) {

        // 수식, 질문 입력받음
        String input = "(x+1)^2=100";

        WAEngine engine = new WAEngine();

        // These properties will be set in all the WAQuery objects created from this WAEngine.
        engine.setAppID(appid);
        engine.addFormat("plaintext");

        // Create the query.
        WAQuery query = engine.createQuery();

        // 입력받은 input을 울프램알파 쿼리로 변환
        query.setInput(input);

        try {
            // For educational purposes, print out the URL we are about to send:
            System.out.println("Query URL:");
            System.out.println(engine.toURL(query));
            System.out.println("");

            //쿼리에서 URL을 울프램알파 서버에 보내고, XML로 결과를 받음
            //WAQueryResult객체로 결과를 파싱함
            WAQueryResult queryResult = engine.performQuery(query);

            if (queryResult.isError()) {
                System.out.println("Query error");
                System.out.println("  error code: " + queryResult.getErrorCode());
                System.out.println("  error message: " + queryResult.getErrorMessage());
            } else if (!queryResult.isSuccess()) {
                System.out.println("Query was not understood; no results available.");
            } else {
                // 결과 출력
                System.out.println("Successful query. Pods follow:\n");
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        String podtitle = pod.getTitle();
                        if(podtitle.contains("Input")==true || podtitle.contains("Solution")==true)
                        {
                            System.out.println(podtitle);
                            System.out.println("------------");

                            for (WASubpod subpod : pod.getSubpods()) {
                                for (Object element : subpod.getContents()) {
                                    if (element instanceof WAPlainText) {
                                        String podcontents = ((WAPlainText) element).getText();
                                        System.out.println(podcontents);
                                        System.out.println("");
                                    }
                                }
                            }
                            System.out.println("");
                        }
                        else
                            System.out.println("");
                    }
                }
            }
        } catch (WAException e) {
            e.printStackTrace();
        }
    }

}*/
