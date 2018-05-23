package ce.inu.ikta;

import java.io.Serializable;

/**
 * Created by 김광현 on 2018-05-19.
 */

public class DBDataSet implements Serializable{
    int id;
    String input;
    String plot;
    String answer;
    String date;
    public DBDataSet(int id,String input, String plot, String answer, String date){
        this.id = id;
        this.input = input;
        this.plot = plot;
        this.answer = answer;
        this.date = date;
    }

}
