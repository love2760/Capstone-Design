package ce.inu.ikta;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveResultActivity extends AppCompatActivity {
    ArrayList<String> grplist;
    HashMap<String, ArrayList<String>> child;
    DBDataSet dbDataSet;
    ExpandableListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_save_result );

        Intent intent = getIntent();
        dbDataSet = (DBDataSet)intent.getSerializableExtra( "dbdata" );
        setValue();
        setAdaptering();
        setButton();
    }
    private void setValue() {
        grplist = new ArrayList<>(  );
        child = new HashMap<>(  );
        listView = (ExpandableListView)findViewById( R.id.saveExpandableListView);
    }

    //리스트뷰 구성
    private void setListData() {

        grplist.add( "식" );
        ArrayList<String> input = new ArrayList<String>();
        input.add( dbDataSet.input );
        child.put( grplist.get( grplist.indexOf( "식" ) ), input );

        ArrayList<String> answer = new ArrayList<String>();
        if (dbDataSet.answer.equals( "empty" ) == false) {
            grplist.add( "답" );
            answer.add( dbDataSet.answer );
            child.put( grplist.get( grplist.indexOf( "답" ) ), answer );
        }

        ArrayList<String> graph = new ArrayList<String>();
        if (dbDataSet.plot.indexOf( "y" ) != -1) {
            grplist.add( "그래프" );
            graph.add( dbDataSet.plot );
            child.put( grplist.get( grplist.indexOf( "그래프" ) ), graph );
        }
    }

    private void setAdaptering() {
        setListData();
        listView.setAdapter( new resultAdapter( getApplicationContext(), grplist, child ) );    //adapter 적용

        //시작시 확장된 상태
        int groupCount = grplist.size();
        for(int i = 0 ; i < groupCount ; i++) {
            listView.expandGroup( i );
        }

        //기본 화살표 아이콘 삭제
        listView.setGroupIndicator( null );
    }

    //버튼 리스너 부여
    private void setButton() {
        findViewById( R.id.Delete_Btn ).setOnClickListener( Delete_Btn);   //취소 버튼
        findViewById( R.id.Ok_Btn ).setOnClickListener( Ok_Btn);   //저장 버튼
    }

    //삭제 버튼
    Button.OnClickListener Delete_Btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DataBase dataBase = new DataBase( getApplicationContext() );
            dataBase.delete( dbDataSet.id );
            Intent intent = new Intent( getApplicationContext(), SaveActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity( intent );
            finish();
        }
    };

    //첨으로 버튼
    Button.OnClickListener Ok_Btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent( getApplicationContext(), MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity( intent );
        }
    };
}
