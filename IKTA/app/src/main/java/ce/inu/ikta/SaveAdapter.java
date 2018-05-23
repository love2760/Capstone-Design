package ce.inu.ikta;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by NyuNyu on 2018-05-23.
 */

public class SaveAdapter extends CursorAdapter {

    @SuppressWarnings( "deprecation" )
    public SaveAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView)view.findViewById( R.id.childtxt );
        String id = cursor.getString( cursor.getColumnIndex( "_input" ) );

        textView.setText( id );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from( context );
        View view = inflater.inflate( R.layout.result_group, parent, false);
        return view;
    }
}
