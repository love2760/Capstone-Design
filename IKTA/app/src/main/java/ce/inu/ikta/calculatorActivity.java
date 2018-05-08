package ce.inu.ikta;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class calculatorActivity extends AppCompatActivity {
    final ArrayList<String> str = new ArrayList<>(  );
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_calculator );
        textView = (TextView) findViewById( R.id.textbox );
        setBtn();

        /*
        //액션바 객체 가져옴
        ActionBar actionBar = getSupportActionBar();
        //액션바에 < 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setHomeButtonEnabled( true );*/
    }

    public void setBtn() {
        Button AllClear = (Button) findViewById( R.id.AllClear );
        Button Clear = (Button) findViewById( R.id.Clear );
        Button Back = (Button) findViewById( R.id.Backspace);
        Button Div = (Button) findViewById( R.id.Division);
        Button Per = (Button) findViewById( R.id.Per );

        Button Seven = (Button) findViewById( R.id.Seven);
        Button Eight = (Button) findViewById( R.id.Eight);
        Button Nine = (Button) findViewById( R.id.Nine);
        Button Mul = (Button) findViewById( R.id.Multiplication );
        Button Index = (Button) findViewById( R.id.Index );

        Button Four = (Button) findViewById( R.id.Four );
        Button Five = (Button) findViewById( R.id.Five);
        Button Six = (Button) findViewById( R.id.Six);
        Button Sub = (Button) findViewById( R.id.Subtraction);
        Button Root = (Button) findViewById( R.id.Root );

        Button One = (Button) findViewById( R.id.One );
        Button Two = (Button) findViewById( R.id.Two);
        Button Three = (Button) findViewById( R.id.Three);
        Button Add = (Button) findViewById( R.id.Addition);
        Button Open = (Button) findViewById( R.id.OpneParenthesis );

        Button Dot = (Button) findViewById( R.id.Dot );
        Button Zero = (Button) findViewById( R.id.Zero);
        Button DoubleZ = (Button) findViewById( R.id.DoubleZero);
        Button Equ = (Button) findViewById( R.id.Equal );
        Button Close = (Button) findViewById( R.id.CloseParenthesis );
    }

    /*
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.AllClear:
                str.clear(  );
                textView.setText( "0" );
                break;
            case R.id.Clear:
                break;
            case R.id.Backspace:
                str = str.remove(  );
                textView.setText( str );
                break;
            case R.id.Division:
                str = str+"/";
                textView.setText( str );
                break;
            case R.id.Per:
                str = str+"%";
                textView.setText( str );
                break;

            case R.id.Seven:
                str = str+"7";
                textView.setText( str );
                break;
            case R.id.Eight:
                str = str+"8";
                textView.setText( str );
                break;
            case R.id.Nine:
                str = str+"9";
                textView.setText( str );
                break;
            case R.id.Multiplication:
                str = str+"*";
                textView.setText( str );
                break;
            case R.id.Index:
                str = str+"^";
                textView.setText( str );
                break;

            case R.id.Four:
                str = str+"4";
                textView.setText( str );
                break;
            case R.id.Five:
                str = str+"5";
                textView.setText( str );
                break;
            case R.id.Six:
                str = str+"6";
                textView.setText( str );
                break;
            case R.id.Subtraction:
                str = str+"-";
                textView.setText( str );
                break;
            case R.id.Root:
                str = str+"sqrt(";
                textView.setText( str );
                break;

            case R.id.One:
                str = str+"1";
                textView.setText( str );
                break;
            case R.id.Two:
                str = str+"2";
                textView.setText( str );
                break;
            case R.id.Three:
                str = str+"3";
                textView.setText( str );
                break;
            case R.id.Addition:
                str = str+"+";
                textView.setText( str );
                break;
            case R.id.OpneParenthesis:
                str = str+"(";
                textView.setText( str );
                break;

            case R.id.Dot:
                str = str+".";
                textView.setText( str );
                break;
            case R.id.Zero:
                str = str+"0";
                textView.setText( str );
                break;
            case R.id.DoubleZero:
                str = str+"00";
                textView.setText( str );
                break;
            case R.id.CloseParenthesis:
                str = str+")";
                textView.setText( str );
                break;
            case R.id.Equal:
                break;
        }

    }*/


}
