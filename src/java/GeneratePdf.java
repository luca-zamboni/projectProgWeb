
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import db.DBManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author forna
 */
public class GeneratePdf  {
    
    public static void generatePdf(Collection<Collection<Object>> c) {   
        String outPath = "";
        Document doc = new Document();
        PdfWriter pdf;
        
        try {
            pdf = PdfWriter.getInstance(doc, new FileOutputStream(outPath));
        } catch (Exception ex) {
            pdf = null;
        }
        
        if (pdf!=null) {
            doc.open();
            doc.addTitle("report_group");
            for (Iterator<Collection<Object>> it = c.iterator(); it.hasNext();) {
                Object object = it.next();
                
                ArrayList<Object> data = (ArrayList<Object>) it.next();
                
                int postNum = (int) data.get(0);
                String username = (String) data.get(1);
                String avatarPath = (String) data.get(2);
                String date = ((String) data.get(3));
                avatarPath = avatarPath==null || avatarPath.equals("") ? 
                        "img.jpg" : avatarPath;
                Image avatar;
                
                try {
                    avatar = Image.getInstance("img/"+avatarPath);
                } catch (BadElementException ex) {
                    Logger.getLogger(GeneratePdf.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    
                }
                
            }
        }
        
    }
    
}
