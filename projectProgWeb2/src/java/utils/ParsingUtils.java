/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.lang.Integer;

/**
 *
 * @author luca
 */
public class ParsingUtils {
    
    private static String D = "$";
    private static String QR = "QR";
    private static int isQR = 0;
    private static int isLink = 1;
    
    public static String parseQrAndLinkUtils(String text){
        String ret = "";
        boolean aux = true;
        int bstring = -1;
        int prev = -2;
        int state = 0;
        boolean ok = false;

        ArrayList<Integer> is = new ArrayList<>(); 
        int index = text.indexOf(D);
        while(index >= 0) {
           is.add(index);
           index = text.indexOf(D, index+1);
        }
        
        if(is.size() <= 0){
            return text;
        }
        // 0:begin 1:$ 2:$$ 3:$$ $ 4:$QR$ 5:$QR$ $
        for(Integer i : is){
            index = i.intValue();
            
            ok = false;
            if(state == 5){
                if(prev+1 == index){
                    //ret += bstring +" QR " + (index-1) +"\n";
                    ret += " QR " +text.substring(bstring+1,index-1)+"\n";
                    state = 0;
                    aux = false;
                    ok = true;
                }
            }
            if(state == 4){
                if(prev+2 <= index){
                    state = 5;
                    prev = index;
                    ok = true;
                }
            }
            if(state == 3){
                if(prev+1 == index){
                    //ret += bstring +" " + (index-1) +"\n";
                    ret += text.substring(bstring+1,index-1)+"\n";;
                    state = 0;
                    aux = false;
                    ok = true;
                }
            }
            if(state == 2){
                if(prev+2 <= index){
                    state = 3;
                    prev = index;
                    ok = true;
                }
            }
            if(state == 1){
                if(prev+1 == index){
                    state = 2;
                    bstring = index;
                    prev = index;
                    ok = true;
                }else{
                    if(QR.equals(text.substring(prev+1, index))){
                        bstring = index;
                        state = 4;
                        prev = index;
                        ok = true;
                    }
                }
            }
            if(!ok){
                prev = index;
                state = 1;
                aux = true;
                bstring = -1;
            }
                
        }
        
        return ret;
    }
    
    
    
}
