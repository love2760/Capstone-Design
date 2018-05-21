package ce.inu.ikta;

/**
 * Created by 김광현 on 2018-05-19.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;


public class DataBase extends SQLiteOpenHelper {
    private static final String dbname = "IKTA.SAVEDATA";
    private static final int DB_VERSION = 1;
    SQLiteDatabase mdatabase;

    public DataBase(Context context) {
        super(context, dbname, null,  DB_VERSION);
        mdatabase = this.getWritableDatabase();
    }

    /***********************
     * 성공시 0 반환
     * 실패시 1 반환
     ***********************/
    public int delete(int id) {
        try {
            mdatabase.execSQL("DELETE FROM SAVEDATA WHERE _id =" + Integer.toString(id) + ";");
            return 0;
        }catch (Exception e) {
            Log.d(getClass().getName(), "삭제 실패");
            return 1;
        }
    }

    /**************************************
     * 정상적으로 종료시 0 반환
     * 실패시 1 반환
     *************************************/
    public int insert(String input, String answer, String plot, String solution) {
        try {
            long currentTime = System.currentTimeMillis();
            mdatabase.execSQL("INSERT INTO SAVEDATA VALUES (null," + input + "," + answer + "," + plot + "," + solution + "," + currentTime + " );");
            return 0;
        }
        catch(Exception e) {
            Log.d(getClass().getName(), "삽입 실패");
            return 1;
        }
    }
    public Cursor sort(SORT order) {
        Cursor c;
        switch (order) {
            case ASC_TIME:
                c = mdatabase.rawQuery("select * from SAVEDATA order by _date asc;",null);
                break;
            case DESC_TIME:
                c = mdatabase.rawQuery("select * from SAVEDATA order by _date asc;",null);
                break;
            default:
                c = mdatabase.rawQuery("select * from SAVEDATA;",null);
        }
        return c;
    }
    public enum SORT {
        ASC_TIME ,DESC_TIME , DEFAULT
    }
    public DBDataSet[] getAll(SORT order) {
        Cursor c = sort(order);

        c.moveToFirst();
        int count = c.getCount();
        if(count == 0) {
            return null;
        }
        DBDataSet[] data = new DBDataSet[count];
        for(int i = 0 ; i < count ; i++) {
            data[i] = new DBDataSet(c.getInt(0),c.getString(1),
                    c.getString(2),c.getString(3),
                    c.getString(4),timeLongToString(c.getLong(5)));
            c.moveToNext();
        }
        return data;
    }
    public String timeLongToString(long milTime) {
        Time time = new Time();
        time.set(milTime);
        String timeString = Integer.toString(time.year) +"년 " + Integer.toString(time.month) +"월 " + Integer.toString(time.monthDay) +"일 "
                + Integer.toString(time.hour) +"시 "+ Integer.toString(time.minute) +"분 "+ Integer.toString(time.second) +"초 ";
        return timeString;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(getClass().getName(), "onCreate 실행");
        createTable(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //database 버전이 올라갈 시에 테이블을 지우고 재생성
        droptable(db);
        createTable(db);
    }

    public void createTable(SQLiteDatabase db) {
        /*********************
         * Table 생성
         * _id INTEGER
         * _input TEXT
         * _plot TEXT
         * _answer TEXT
         * _solution TEXT
         * _date REAL
         * *******************/
        db.execSQL("CREATE TABLE SAVEDATA(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "_input TEXT NOT NULL,"+
                "_plot TEXT NOT NULL," +
                "_answer TEXT NOT NULL," +
                "_solution TEXT NOT NULL," +
                "_date REAL);");
        Log.v(getClass().getName(), "SAVEDATA 테이블 작성 완료");
    }

    public void droptable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE SAVEDATA;");
        Log.v(getClass().getName(), "테이블 삭제 완료");
    }
}