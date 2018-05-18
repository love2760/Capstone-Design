package ce.inu.ikta;

/**
 * Created by 김광현 on 2018-05-19.
 */

public class DBDataSet {
    int id;
    String input;
    String plot;
    String answer;
    String solution;
    String date;
    public DBDataSet(int id,String input, String plot, String answer, String solution, String date){
        this.id = id;
        this.input = input;
        this.plot = plot;
        this.answer = answer;
        this.solution = solution;
        this.date = date;
    }

}
