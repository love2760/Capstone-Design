package ce.inu.ikta;

import android.content.Context;
import android.content.Intent;
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
