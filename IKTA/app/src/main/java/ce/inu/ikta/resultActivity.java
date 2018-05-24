package ce.inu.ikta;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.HashMap;

import static ce.inu.ikta.globalValue.bitimg;
import static ce.inu.ikta.globalValue.postImg;

public class resultActivity extends AppCompatActivity {
    Context ctx;
    //ImageView imgView;
    String TAG = "resultActivity";
    ExpandableListView listView;
    DataBase dataBase;
    private ArrayList<String> grplist = new ArrayList<String>(); //상위 리스트
    private HashMap<String, ArrayList<String>> child = new HashMap<String, ArrayList<String>>(  ); //하위 리스트
    String data;
    wolfData wolfdata;
    ce.inu.ikta.wolframalpha wolframalpha;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_result );
        Intent intent = getIntent();
        data = intent.getStringExtra( "ocrString" );
        //data = "1+5";
        wolframalpha = new wolframalpha();

        setAdaptering();

        setButton();    //저장/취소 버튼

        setValue(); //이미지 뷰
        //imgView.setImageBitmap( postImg );   //이미지 뷰에 사진을 넣음
    }

    private void setValue() {
        //imgView = (ImageView) findViewById( R.id.resultimg );
        ctx = this;
    }

    //리스트뷰 구성
    private void setListData() {

        wolfdata = wolframalpha.Wolfoutput( data );

        Log.d(TAG, "식 넣기");
        grplist.add("식");
        ArrayList<String> input = new ArrayList<String>(  );
        input.add(wolfdata.input);
        child.put(grplist.get(grplist.indexOf( "식" )), input);

        Log.d(TAG, "답 넣기");
        ArrayList<String> answer = new ArrayList<String>(  );
        if(wolfdata.answer.equals( "empty" ) == false) {
            grplist.add( "답" );
            answer.add( wolfdata.answer );
            child.put( grplist.get( grplist.indexOf( "답" ) ), answer );
        }
        Log.d(TAG, "그래프 넣기");
        ArrayList<String> graph = new ArrayList<String>(  );
        if(wolfdata.input.indexOf( "y" ) != -1 ) {
            grplist.add("그래프");
            graph.add(wolfdata.input);
            child.put(grplist.get(grplist.indexOf( "그래프" )), graph);
        }
    }

    private void setAdaptering() {
        listView = (ExpandableListView) this.findViewById( R.id.listview ) ;    //리스트뷰 초기화
        setListData();  //expandableListview에 들어갈 data 생성
        listView.setAdapter( new resultAdapter( this, grplist, child ) );    //adapter 적용

        //시작시 확장된 상태
        int groupCount = grplist.size();
        for(int i = 0 ; i < groupCount ; i++) {
            listView.expandGroup( i );
        }

        /*
        //1,2번째 확장 상태로 고정
        listView.setOnGroupCollapseListener( new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                listView.expandGroup(0);
                listView.expandGroup(1);
            }
        } );*/

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

    //저장 버튼
    Button.OnClickListener Save_Btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dataBase = new DataBase( ctx );
            dataBase.insert( wolfdata.input, wolfdata.answer, wolfdata.input );

            Intent intent = new Intent( ctx, SaveActivity.class );
            ctx.startActivity( intent );
            finish();
        }
    };

}
