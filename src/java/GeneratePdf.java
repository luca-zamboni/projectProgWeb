
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
    
    public static void generatePdf(Collection<String> c) {   
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
            for (Iterator it = c.iterator(); it.hasNext();) {
                Object object = it.next();
                
                
            }
        }
        
    }
    
}
