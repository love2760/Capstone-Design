package ce.inu.ikta;

import android.util.Log;

/**
 * Created by NyuNyu on 2018-05-23.
 */

public class PrePlotString {
    public static String checkPow(String equ) {
        if(equ.contains("^") == false) return equ;

        int powSign = equ.lastIndexOf("^");
        int fidx = powSign-1;
        // 앞부분 찾기
        for(; fidx >= 0 ; fidx--) {
            if(equ.charAt(fidx) >=0x30 && equ.charAt(fidx) <= 0x39 || equ.charAt(fidx) == 0x2E || equ.charAt( fidx ) == 0x2D) {
                if(equ.charAt( fidx ) == 0x2D && fidx == 0) { // 마이너스 숫자에 포함
                    fidx = fidx-1;
                    break;
                }else if(equ.charAt( fidx ) == 0x2D && fidx != 0 && (equ.charAt(fidx-1)== 0x28))
                {
                    fidx = fidx-1;
                    break;
                }

            }
            else{break;}
        }
        String front = equ.substring(fidx+1, powSign);

        int bidx = powSign+1;
        for(;bidx<equ.length();bidx++) {
            if(equ.charAt(bidx) >=0x30 && equ.charAt(bidx) <= 0x39 || equ.charAt(bidx) == 0x2E) {}
            else{break;}
        }
        String back = equ.substring(powSign+1,bidx);

            float fn = Float.parseFloat( front );
            float bn = Float.parseFloat( back );
        if(fn != 0) {
            float tmp = (float)Math.pow( fn, bn );
            Log.d( "PrePlotString", "지수 계산 결과 : " + tmp + "" );
            return equ.replace( equ.substring( fidx + 1, bidx ), Float.toString( tmp ) );
        }
        else {
            return equ.replace( equ.substring( fidx + 1, bidx ), Float.toString( 0 ) );
        }
    }
    public String deleteEqual(String equ)
    {
        if(equ.contains("=") == false) return equ;

        int powSign = equ.lastIndexOf("=");
        String front =equ.substring(powSign+1,equ.length());
        return front;
    }
    public String multiplyX (String equ) {
        String xequ = deleteEqual(equ);
        if (xequ.contains( "x" ) == false) {

            return xequ;
        }

        int powSign = xequ.indexOf( "x" );
        int fidx = powSign - 1;
        //조건식 x 앞이 숫자거나 ')'이다.
        if(powSign != 0) {
            if (xequ.charAt( fidx ) >= 0x30 && xequ.charAt( fidx ) <= 0x39 || xequ.charAt( fidx ) == 0x29) {
                xequ = xequ.replaceFirst( "x", "*a" );

            } else {
                xequ = xequ.replaceFirst( "x", "a" );
            }
        }
        else
        {
            xequ = xequ.replaceFirst( "x", "a" );
        }
        Log.d("ㅇㅇ",xequ+"얘에요");
        equ = multiplyX( xequ );
        return equ;
    }
}

