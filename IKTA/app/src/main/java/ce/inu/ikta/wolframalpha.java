package ce.inu.ikta;

public class wolframalpha {

    // 울프램알파 app id 입력
    private static String appid = "E5AKEG-Q8EA9VLPE2";
    final String TAG = "wolfram";
    wolfData output;
    /*
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
*/
}