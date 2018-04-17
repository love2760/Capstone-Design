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
    CutImage cutImage;
    String TAG = "resultActivity";
    ExpandableListView listView;
    private ArrayList<String> grplist = new ArrayList<String>(); //상위 리스트
    private HashMap<String, ArrayList<String>> child = new HashMap<String, ArrayList<String>>(  ); //하위 리스트
    //wolfData에 넣은 string 가져옴
    private wolfData wd = new wolfData("여기는 식을 받아올 자리", "여기는 답을 받아올 자리", null, "여기는 풀이를 받아올 자리");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_result );


        setAdaptering();

        setButton();    //저장/취소 버튼

        setValue(); //이미지 뷰
        imgView.setImageBitmap( bitimg );   //이미지 뷰에 사진을 넣음
    }

    private void setValue() {
        imgView = (ImageView) findViewById( R.id.resultimg );
        ctx = this;
    }

    //리스트뷰 구성
    private void setListData() {

        Log.d(TAG, "data 넣기");
        grplist.add("식");
        grplist.add("답");
        grplist.add("그래프");
        grplist.add("풀이");

        ArrayList<String> input = new ArrayList<String>(  );
        input.add(wd.input);
        ArrayList<String> answer = new ArrayList<String>(  );
        answer.add(wd.answer);
        ArrayList<String> graph = new ArrayList<String>(  );
        graph.add(wd.graph);
        ArrayList<String> solution = new ArrayList<String>(  );
        solution.add(wd.solution);

        child.put(grplist.get(0), input);
        Log.d(TAG,"식 ArrayList에 무엇이 들었을까요"+input);
        child.put(grplist.get(1), answer);
        child.put(grplist.get(2), graph);
        child.put(grplist.get(3), solution);
    }

    private void setAdaptering() {
        listView = (ExpandableListView) this.findViewById( R.id.listview ) ;    //리스트뷰 초기화
        setListData();  //expandableListview에 들어갈 data 생성
        listView.setAdapter( new Adapter( this, grplist, child ) );    //adapter 적용

        //시작시 확장된 상태
        int groupCount = grplist.size();
        for(int i = 0 ; i < groupCount ; i++) {
            listView.expandGroup( i );
        }

        //1,2번째 확장 상태로 고정
        listView.setOnGroupCollapseListener( new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                listView.expandGroup(0);
                listView.expandGroup(1);
            }
        } );

        //기본 화살표 아이콘 삭제
        listView.setGroupIndicator( null );
    }

    //버튼 리스너 부여
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
