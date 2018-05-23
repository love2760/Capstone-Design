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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class SaveActivity extends AppCompatActivity {
    private static final String TAG = "SaveActivity";
    Context ctx;
    ListView savelistView;
    DataBase dataBase;
    SaveAdapter saveAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_save );
        dataBase = new DataBase( this );
        ctx = this;
        /*
        //액션바 객체 가져옴
        ActionBar actionBar = getSupportActionBar();
        //액션바에 < 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setHomeButtonEnabled( true );*/

        setSaveAdaptering();
        setListener();
    }

    private void setListener() {
        savelistView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent( ctx, SaveResultActivity.class );
                intent.putExtra( "dbdata",saveAdapter.getItem( position ) );
                ctx.startActivity( intent );
            }
        } );
    }

    private void setSaveAdaptering() {
        saveAdapter = new SaveAdapter();
        DBDataSet[] mDBDataSet = dataBase.getAll( DataBase.SORT.ASC_TIME );
        if(mDBDataSet != null) {
            for (int i = 0; i < mDBDataSet.length; i++) {
                saveAdapter.addItem( mDBDataSet[i] );
            }
        }
        savelistView = (ListView) findViewById( R.id.savelist );
        savelistView.setAdapter( saveAdapter );
    }

}
