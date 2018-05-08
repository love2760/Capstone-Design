package ce.inu.ikta;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NyuNyu on 2018-05-08.
 */

public class DataBasesOpen extends SQLiteOpenHelper{

    public DataBasesOpen(Context context) {
        super(context, "SaveList_TABLE", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL( "CREATE TABLE SaveList_TABLE (" + "_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "input TEXT, answer TEXT, graph TEXT, soulution TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL( "DROP TABLE IF EXISTS SaveList" ); // 기존 db 삭제
        onCreate( database );   // 새로운 db 삭제
    }

    public void DB() {
        SQLiteDatabase database = getWritableDatabase();
    }

}
