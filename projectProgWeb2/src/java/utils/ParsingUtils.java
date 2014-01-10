/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.esponce.webservice.QRCodeClient;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author luca
 */
public class ParsingUtils {

    private static final String D = "$";
    private static final String QR = "QR";
    private static final String FOLDER = "./files/gr";

    public static String parseQrAndLinkUtils(String text, int group,HttpServletRequest req) {
        String ret = "";
        boolean aux = true;
        int bstring = -1;
        int prev = 0;
        int state = 0;
        boolean ok = false;

        ArrayList<Integer> is = new ArrayList<>();
        int index = text.indexOf(D);
        while (index >= 0) {
            is.add(index);
            index = text.indexOf(D, index + 1);
        }

        if (is.size() <= 0) {
            return text;
        }
        // 0:begin 1:$ 2:$$ 3:$$ $ 4:$QR$ 5:$QR$ $
        for (Integer i : is) {
            index = i.intValue();

            ok = false;
            if (state == 5) {
                if (prev + 1 == index) {
                    ret += replaceWithQR(text,bstring + 1, index - 1, group,req);
                    state = 0;
                    aux = false;
                    ok = true;
                }
            }
            if (state == 4) {
                if (prev + 2 <= index) {
                    state = 5;
                    prev = index;
                    ok = true;
                }
            }
            if (state == 3) {
                if (prev + 1 == index) {
                    ret += replaceWithLink(text, bstring + 1, index - 1);
                    state = 0;
                    aux = false;
                    ok = true;
                }
            }
            if (state == 2) {
                if (prev + 2 <= index) {
                    state = 3;
                    prev = index;
                    ok = true;
                }
            }
            if (state == 1) {
                if (prev + 1 == index) {
                    state = 2;
                    bstring = index;
                    prev = index;
                    ok = true;
                } else {
                    if (QR.equals(text.substring(prev + 1, index))) {
                        bstring = index;
                        state = 4;
                        prev = index;
                        ok = true;
                    }
                }
            }
            if (!ok) {
                ret += replaceWithLink(text, bstring + 1, index - 1);
                prev = index;
                state = 1;
                aux = true;
                bstring = -1;
            }

        }

        return ret;
    }

    private static String replaceWithLink(String text, int start, int end) {
        String into = text.substring(start, end);
        try {
            URL url = new URL(into);
            into = "<a href='" + into + "'>" + into + "</a>";
        } catch (MalformedURLException e) {
        }
        return into;
    }

    private static String replaceWithQR(String text, int start, int end, int group,HttpServletRequest req) {
        String into = text.substring(start, end);
        try {
            URL url = new URL(into);
            String path = req.getServletContext().getRealPath("/");
            String imgUrl = FOLDER + group + "/" + Support.randomStringSHA1(32) + ".png";
            File aux = new File(path+imgUrl);
            aux.createNewFile();
            QRCodeClient client = new QRCodeClient();
            BufferedInputStream ins = client.generate(into);
            FileOutputStream fos = new FileOutputStream(aux);
            BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

            int length = 0;
            byte[] data = new byte[1024];
            while ((length = ins.read(data, 0, 1024)) > 0) {
                bos.write(data, 0, length);
            }
            bos.close();
            fos.close();
            ins.close();
            
            into = "<img src='"+imgUrl+"' style='width:150px; heigth:150px;' /><a href='" + into + "'>" + into + "</a>";
        } catch (Exception  e) {
            System.err.println(e.getMessage());
        }
        return into;
    }
}
