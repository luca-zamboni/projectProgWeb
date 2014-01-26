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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import servlets.AddPost;
import static servlets.AddPost.getAllFileGroup;
import servlets.Login;

/**
 *
 * @author luca
 */
public class ParsingUtils {

    private static final String D = "$";
    private static final String QR = "QR";
    private static final String FOLDER = "./files/";
    private String text;
    private int groupid;
    private ArrayList<String> qrs = new ArrayList<>();
    private HttpServletRequest mReq;
    private int qrNum = 1;

    public ParsingUtils(String text, int grouid, HttpServletRequest mReq) {
        this.text = "  " + text;
        this.groupid = grouid;
        this.mReq = mReq;
    }

    public ArrayList<String> getQrs() {
        return qrs;
    }

    public String parseQrAndLinkUtils() {
        String ret = "";
        int bstring = 0;
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
            System.err.println("no $");
            ret = text;
        } else {
            // 0:begin 1:$ 2:$$ 3:$$ $ 4:$QR$ 5:$QR$ $
            for (Integer i : is) {
                index = i.intValue();

                ok = false;
                if (state == 5) {
                    if (prev + 1 == index) {
                        qrs.add(text.substring(bstring + 1, index - 1));
                        ret += replaceWithLink(text, bstring + 1, index - 1);
                        prev = index-1;
                        state = 0;
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
                        prev = index-1;
                        state = 0;
                        ok = true;
                    }
                }
                if (state == 2) {
                    if (prev + 2 <= index) {
                        System.err.println("s2");
                        state = 3;
                        prev = index;
                        ok = true;
                    }
                }
                if (state == 1) {
                    if (prev + 1 == index) {
                        System.err.println("s1");
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
                    System.err.println("non ok");
                    System.err.println(bstring +" " + index + " " + text);
                    ret += text.substring(bstring, index );
                    prev = index;
                    bstring = index;
                    state = 1;
                }

            }
        }
        ret = ret.replaceAll("[$]", " ");
        return ret;

    }

    private String replaceWithLink(String text, int start, int end) {
        String into = text.substring(start, end);
        try {
            URL url = new URL(into);
            into = "<a href='" + into + "'>" + into + "</a>";
        } catch (MalformedURLException e) {
            boolean g = false;
            into = into.replace(" ", "");
            for (String f : getAllFileGroup(groupid, mReq)) {
                if (f.equals(into)) {
                    g = true;
                }
            }
            if (g) {
                String path = mReq.getServletContext().getContextPath();
                into = "<a href=\"" + path + "/files/" + groupid + "/" + into + "\">" + into + "</a>";
            }
        }
        return into;
    }

    public void generateQR(int postid) {
        String link, saveLocation;
        String path = mReq.getServletContext().getRealPath("/");
        path = path + "files/" + groupid + "/qr/" + postid + "/";
        for (String h : qrs) {
            link = h;
            saveLocation = path;
            try {
                URL url = new URL(link);
                File a = new File(saveLocation);
                a.mkdirs();
                File aux = new File(saveLocation + "QR" + qrNum + ".png");
                qrNum++;
                if (aux.exists()) {
                    aux.delete();
                }
                aux.createNewFile();
                QRCodeClient client = new QRCodeClient();
                BufferedInputStream ins = client.generate(link);
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

            } catch (Exception e) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
