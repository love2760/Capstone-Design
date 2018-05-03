package ce.inu.ikta;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SaveActivity extends AppCompatActivity {
    private static final String TAG = "SaveActivity";
    Context ctx;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_save );
        listView = (ListView) findViewById( R.id.savelist );
        //액션바 객체 가져옴
        ActionBar actionBar = getSupportActionBar();
        //액션바에 < 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setHomeButtonEnabled( true );
        setListener();
    }

    private void setListener() {
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent( ctx, SaveResultActivity.class );
                ctx.startActivity( intent );
            }
        } );
    }

}
