package ce.inu.ikta;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
    //wolfData에 넣은 string 가져옴
    private wolfData wd = new wolfData("여기는 식을 받아올 자리", "여기는 답을 받아올 자리", null, "여기는 풀이를 받아올 자리");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_result );
        Log.d(TAG,"두 번째 액티비티 시작");
        listView = (ExpandableListView) this.findViewById( R.id.listview ) ;    //리스트뷰 초기화
        setListData();  //expandableListview에 들어갈 data 생성
        Log.d(TAG,"adapter 시작");
        listView.setAdapter( new Adapter( this, list, child ) );    //adapter 적용

        setButton();    //저장/취소 버튼 구현

        setValue(); //이미지 뷰 구현
        imgView.setImageBitmap( bitimg );   //이미지 뷰에 사진을 넣음
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

        ArrayList<String> input = new ArrayList<String>(  );
        input.add(wd.input);
        ArrayList<String> answer = new ArrayList<String>(  );
        answer.add(wd.answer);
        ArrayList<String> graph = new ArrayList<String>(  );
        graph.add(wd.graph);
        ArrayList<String> solution = new ArrayList<String>(  );
        solution.add(wd.solution);

        child.put(list.get(0), input);
        Log.d(TAG,"식 ArrayList에 무엇이 들었을까요"+input);
        child.put(list.get(1), answer);
        child.put(list.get(2), graph);
        child.put(list.get(3), solution);
    }

    //버튼 구현
    private void setButton() {
        findViewById( R.id.Cancel_Btn ).setOnClickListener( Cancel_Btn );   //취소 버튼
        findViewById( R.id.Save_Btn ).setOnClickListener( Save_Btn );   //저장 버튼
    }

    //취소 버튼
    Button.OnClickListener Cancel_Btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    //저장 버튼(미구현)
    Button.OnClickListener Save_Btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


}
