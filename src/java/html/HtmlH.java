/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import models.Group;

/**
 *
 * @author luca
 */
public class HtmlH {

    public static final String POST = "POST";
    public static final String GET = "GET";

    public static String addTag(String tag, String intoTag) {
        String ret = "";
        ret += "<" + tag + ">";
        ret += intoTag;
        ret = "</" + tag + ">";
        return "";
    }

    public static String addTag(String tag, String intoTag, Option... opt) {
        String ret = "";
        ret += "<" + tag + " ";
        for (Option o : opt) {
            ret += o.getName() + "=\"" + o.getValue() + "\" ";
        }
        ret += ">";
        ret += intoTag;
        ret = "</" + tag + ">";
        return "";
    }

    public static String getImageAvatarSmall(String avatar) {
        String ret = "<img src=\"img/" + avatar + "\" style='width:50px;heigth:100px;' "
                + "alt=\"This is you? You are so ugly...\" class=\"img-rounded\">";
        return ret;
    }
    
    public static String getImageAvatarSmallTumb(String avatar){
        String ret = "<img src=\"img/" + avatar + "\" style='width:60px;heigth:100px;' "
                + "alt=\"This is you? You are so ugly...\" class=\"img-thumbnail\">";
        return ret;
    }

    public static String getImageAvatar(String avatar) {
        String ret = "<img src=\"img/" + avatar + "\" style='width:150px;heigth:200px;' "
                + "alt=\"This is you? You are so ugly...\" class=\"img-thumbnail\">";
        return ret;
    }

    public static String generateButton(String text, String href, String classBt, String icon) {
        String bt = "";
        bt += "<a href='" + href + "'><button type=\"button\" class=\"" + classBt + "\">";
        bt += "<span class=\"glyphicon glyphicon-" + icon + "\"></span> ";
        bt += text + "</button></a>";
        return bt;
    }

    public static String generateButtonSubmit(String text, String classBt, String icon) {
        String bt = "";
        bt += "<button type=\"submit\" class=\"" + classBt + "\">";
        bt += "<span class=\"glyphicon glyphicon-" + icon + "\"></span> ";
        bt += text + "</button>";
        return bt;
    }

    public static String generateButtonSubmit(String text, String classBt) {
        String bt = "";
        bt += "<button type=\"submit\" class=\"" + classBt + "\">";
        bt += text + "</button>";
        return bt;
    }

    public static String generateButton(String text, String href, String classBt) {
        String bt = "";
        bt += "<a href='" + href + "'><button type=\"button\" class=\"" + classBt + "\"> ";
        bt += text + " </button></a>";
        return bt;
    }

    public static String getDateFromTimestamp(String timestamp) {
        int l = Integer.parseInt(timestamp);
        String dateAsText = new SimpleDateFormat("dd-MM-yyy HH:mm:ss")
                .format(new Date(l * 1000L));
        return dateAsText;
    }

    public static String generateForm(String action, String method, String content) {
        String ret = "";
        ret += "<form method='" + method + "' action='" + action + "'>";
        ret += content;
        ret += "</form>";
        return ret;
    }

    public static String h1String(String text) {
        return "<h1>" + text + "</h1>";
    }

    public static String generateH(int h, String text) {
        return "<h" + h + ">" + text + "</h" + h + ">";
    }

    public static String generateHWithColor(int h, String text, String cClass) {
        return "<h" + h + " class=\"" + cClass + "\">" + text + "</h" + h + ">";
    }

    public static String getNavBar(String user,String avatar) {
        String nav = "";
        nav += "<nav class=\"navbar navbar-inverse\" role=\"navigation\">\n"
                + "  <div class=\"navbar-header\">\n"
                + "    <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#bs-example-navbar-collapse-1\">\n"
                + "    </button>\n"
                + "    <a class=\"navbar-brand\" href=\"./home\">Home</a>\n"
                + "  </div>";
        nav +="<ul class=\"nav navbar-nav\">\n" +
            "      <li><a href=\"newGroup\">Create Group</a></li>\n" +
            "    </ul>";
        nav += "<ul class=\"nav navbar-nav navbar-right\">\n"+
                "<li><a href='./uploadAvatar'></a></li>"+
                "<li class=\"dropdown\">\n" +
                "        <a href=\"#\" style='padding-top:5px; padding-bottom:5px;' class=\"dropdown-toggle\" data-toggle=\"dropdown\">"+
                getImageAvatarSmall(avatar)+" " + user + " <b class=\"caret\"></b></a>\n" +
                "        <ul class=\"dropdown-menu\">\n" +
                "          <li><a href=\"./uploadAvatar\">Change Avatar</a></li>\n" +
                "          <li></li>\n" +
                "           <li class=\"divider\"></li>"+
                "          <li><a href='logout'>Logout</a></li>\n" +
                "        </ul>\n" +
                "      </li>"
                + "</ul>"
                + "</nav>";
        return nav;
    }

    public static String includeHead() {
        String ret = "";

        ret += "<head>";
        ret += "<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n"
                + "<script src=\"js/jquery.js\"></script>\n"
                + "<script src=\"js/bootstrap.min.js\"></script>\n";
        ret += "</head>";

        return ret;

    }

    public static String addHtml(String html, String user, String avatar) {
        String ret = "";
        ret += "<html>";
        ret += HtmlH.includeHead();
        ret += "<body>";
        ret += "<div class=\"row\">"
                + "<div class=\"col-md-2\">&nbsp;</div>"
                + "<div class=\"col-md-8\">";
        ret += HtmlH.getNavBar(user,avatar) + html;
        ret += "<div class=\"col-md-2\">&nbsp</div>"
                + "</div>";
        ret += "</body>";
        ret += "</html>";
        return ret;
    }

    public static String getTableFrom(ArrayList<String> columns, ArrayList<ArrayList> data) {
        String html = "";
        html += "<table>";
        html += "<th>";
        Iterator<String> i = columns.iterator();
        while (i.hasNext()) {
            html += "<td>" + i.next() + "</td>";
        }
        html += "</th>";
        Iterator<ArrayList> j = data.iterator();
        while (j.hasNext()) {
            html += "<tr>";
            ArrayList<String> aux = j.next();
            Iterator<String> y = aux.iterator();
            while (y.hasNext()) {
                html += "<td>" + y.next() + "</td>";
            }
            html += "</tr>";
        }
        return html + "<br>";
    }


}
