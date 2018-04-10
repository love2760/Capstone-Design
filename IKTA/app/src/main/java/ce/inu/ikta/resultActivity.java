package ce.inu.ikta;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.HashMap;

import static ce.inu.ikta.globalValue.bitimg;

public class resultActivity extends AppCompatActivity {
    Context ctx;
    ImageView imgView;
    String TAG = "resultActivity";
    ExpandableListView listView;
    private ArrayList<String> list = new ArrayList<String>(); //상위 리스트
    private HashMap<String, ArrayList<String>> child = new HashMap<String, ArrayList<String>>(  ); //하위 리스트

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_result );
        Log.d(TAG,"두 번째 액티비티 시작");
        listView = (ExpandableListView) this.findViewById( R.id.listview ) ;    //리스트뷰 초기화
        setListData();
        Log.d(TAG,"adapter 시작");
        listView.setAdapter( new Adapter( this, list, child ) );


        setValue();
        imgView.setImageBitmap( bitimg );
    }
    private void setValue() {
        imgView = (ImageView) findViewById( R.id.resultimg );
        ctx = this;
    }

    //리스트뷰 구성

    private void setListData() {

        Log.d(TAG, "data 넣기");
        list.add("식");
        list.add("답");
        list.add("그래프");
        list.add("풀이");

        ArrayList<String> modify = new ArrayList<String>(  );
        modify.add("df");
        ArrayList<String> answer = new ArrayList<String>(  );
        answer.add("gf");
        ArrayList<String> graph = new ArrayList<String>(  );
        graph.add("hh");
        ArrayList<String> solution = new ArrayList<String>(  );
        solution.add("jj");

        child.put(list.get(0),modify);
        child.put(list.get(1),answer);
        child.put(list.get(2),graph);
        child.put(list.get(3),solution);
    }


}
