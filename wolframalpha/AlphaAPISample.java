
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;
public class AlphaAPISample {

    // 울프램알파 app id 입력
    private static String appid = "E5AKEG-Q8EA9VLPE2";

    public static void main(String[] args) {

        // 수식, 질문 입력받음
        String input = "(x+1)^2=100";
        if (args.length > 0)
            input = args[0];
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

}
