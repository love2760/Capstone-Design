package ce.inu.ikta;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveActivity extends AppCompatActivity {
    private static final String TAG = "SaveActivity";
    Context ctx;
    ListView listView;
    DataBase dataBase;
    SQLiteDatabase SQLdatabase;
    Cursor cursor;
    ArrayList<String> savelist = new ArrayList<>(  );
    HashMap<String, ArrayList<String>> savechild = new HashMap<>(  );
    SaveAdapter saveAdapter;
    final static String querySelectAll = String.format( "SELECT * FROM %s", "IKTA.SAVEDATA" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_save );
        listView = (ListView) findViewById( R.id.savelist );
        /*
        //액션바 객체 가져옴
        ActionBar actionBar = getSupportActionBar();
        //액션바에 < 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setHomeButtonEnabled( true );*/
        setListener();

        dataBase = new DataBase( this );
        SQLdatabase = dataBase.getWritableDatabase();

        cursor = SQLdatabase.rawQuery( querySelectAll, null );
        saveAdapter = new SaveAdapter(this, cursor);

        listView.setAdapter( saveAdapter );

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
