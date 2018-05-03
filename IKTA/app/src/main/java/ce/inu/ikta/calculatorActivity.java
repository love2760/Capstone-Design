package ce.inu.ikta;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class calculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_calculator );

        //액션바 객체 가져옴
        ActionBar actionBar = getSupportActionBar();
        //액션바에 < 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setHomeButtonEnabled( true );
    }
}
