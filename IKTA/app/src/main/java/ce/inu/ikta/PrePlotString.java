package ce.inu.ikta;

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
            if(equ.charAt(fidx) >=0x30 && equ.charAt(fidx) <= 0x39 || equ.charAt(fidx) == 0x2E) {}
            else{break;}
        }
        String front = equ.substring(fidx+1, powSign);

        int bidx = powSign+1;
        for(;bidx<equ.length();bidx++) {
            if(equ.charAt(bidx) >=0x30 && equ.charAt(bidx) <= 0x39 || equ.charAt(bidx) == 0x2E) {}
            else{break;}
        }
        String back = equ.substring(powSign+1,bidx);

        double fn = Double.parseDouble(front);
        double bn = Double.parseDouble(back);

        return equ.replace(equ.substring(fidx+1,bidx), Double.toString(Math.pow(fn, bn)));
    }
    public String deleteEqual(String equ)
    {
        if(equ.contains("=") == false) return equ;

        int powSign = equ.lastIndexOf("=");
        String front =equ.substring(powSign+1,equ.length());
        return front;
    }
}

