
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import db.DBManager;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
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
public class GeneratePdf {

    public static void generatePdf(ArrayList<ArrayList<Object>> c, int groupId, DBManager dbm) {

        String path = "/home/forna/git/projectProgWeb/build/web";
        File b = new File(path + "/pdf/" + groupId + "/");
        b.mkdirs();
        
        String outPath = path+"/pdf/" + groupId + "/report.pdf";
        Document doc = new Document();
        PdfWriter pdf;

        try {
            pdf = PdfWriter.getInstance(doc, new FileOutputStream(outPath));
        } catch (Exception ex) {
            pdf = null;
        }

        if (pdf != null) {
            doc.open();
            doc.addTitle("report_group_"+groupId);
            String tit = "Group Report of group ";
            try {
                tit += "\""+dbm.getGroupTitleById(groupId)+"\"";
            } catch (SQLException ex) {
                tit = "";
            }
            Paragraph title = new Paragraph(tit);
            title.setFont(new Font(Font.FontFamily.HELVETICA, 40, Font.ITALIC));
            try {
                doc.add(title);
            } catch (DocumentException ex) {
                Logger.getLogger(GeneratePdf.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (ArrayList<Object> data : c) {
                
                int postNum = (int) data.get(0);
                String username = (String) data.get(1);
                String avatarPath = (String) data.get(2);
                String date = ((String) data.get(3));
                avatarPath = avatarPath == null || avatarPath.equals("")
                        ? "img.jpg" : avatarPath;
                Image avatar;
                try {
                    Paragraph p = new Paragraph();
                    avatar = Image.getInstance(path+"/img/" + avatarPath); 
                    System.err.println(""+avatar.getWidth()+" "+avatar.getHeight());
                    p.add(avatar);
                    p.add(new Paragraph("Username: " + username));
                    p.add(new Paragraph("Last post: " + date));
                    p.add(new Paragraph("Post in group: " + postNum + "\n\n"));
                    doc.add(p);
                } catch (Exception ex) {
                    System.err.println("ano3");
                }

            }
            doc.close();
        }

    }

}
