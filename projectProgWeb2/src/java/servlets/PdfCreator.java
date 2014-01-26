/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBManager;
import utils.Support;

/**
 *
 * @author forna
 */
public class PdfCreator extends HttpServlet {

    private DBManager dbm;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int gp = Integer.parseInt(req.getParameter("g"));
            connectToDatabase(req);
            String path = req.getServletContext().getRealPath("/");
            ArrayList<ArrayList<Object>> alalo = dbm.getDataForReport(gp);
            generatePdf(path, alalo, gp);
            resp.sendRedirect("pdf/" + gp + "/report.pdf");
        } catch (Exception e) {
        }
    }

    private void generatePdf(String path, ArrayList<ArrayList<Object>> c, int groupId) {

        File b = new File(path + "/pdf/" + groupId + "/");
        b.mkdirs();

        String outPath = path + "/pdf/" + groupId + "/report.pdf";
        Document doc = new Document();
        PdfWriter pdf;

        try {
            pdf = PdfWriter.getInstance(doc, new FileOutputStream(outPath));
        } catch (Exception ex) {
            pdf = null;
        }

        if (pdf != null) {
            doc.open();
            doc.addTitle("report_group_" + groupId);
            String tit = "Group Report of group ";
            try {
                tit += "\"" + dbm.getGroupTitleById(groupId) + "\"";
            } catch (SQLException ex) {
                Logger.getLogger(PdfCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
            tit = "";
            Font font = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLDITALIC);
            Paragraph title = new Paragraph(tit, font);
            title.setSpacingAfter(5f);
            try {
                doc.add(title);
            } catch (DocumentException ex) {
                Logger.getLogger(PdfCreator.class.getName()).log(Level.SEVERE, null, ex);
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
                    PdfPTable tbl = new PdfPTable(2);
                    Paragraph p = new Paragraph();

                    float[] widths = {1f, 3f};
                    tbl.setWidths(widths);

                    avatar = Image.getInstance(path + "/img/" + avatarPath);
                    System.err.println("" + avatar.getWidth() + " " + avatar.getHeight());
                    PdfPCell pc1 = new PdfPCell(avatar);
                    p.add(new Paragraph("Username: " + username));
                    p.add(new Paragraph("Last post: " + Support.getDateFromTimestampLong(date)));
                    p.add(new Paragraph("Post in group: " + postNum + "\n\n"));
                    PdfPCell pc2 = new PdfPCell();
                    pc2.addElement(p);

                    pc1.setBorder(PdfPCell.NO_BORDER);
                    pc2.setBorder(PdfPCell.NO_BORDER);

                    tbl.addCell(pc1);
                    tbl.addCell(pc2);
                    tbl.setSpacingBefore(10f);

                    doc.add(tbl);
                } catch (Exception ex) {
                    System.err.println("ano3");
                }

            }
            doc.close();
        }

    }

    private void connectToDatabase(HttpServletRequest request) {
        try {
            dbm = new DBManager(request);
        } catch (SQLException ex) {
        }
    }

}
