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
public class Html {
    
    public static final String POST = "POST";
    public static final String GET = "GET";   
    
    public static String addTag(String tag,String intoTag){
        String ret = "";
        ret += "<" + tag + ">";
        ret += intoTag;
        ret = "</" + tag + ">";
        return "";
    }
    
    public static String addTag(String tag,String intoTag, Option... opt){
        String ret = "";
        ret += "<" + tag + " ";
        for(Option o : opt){
            ret += o.getName() + "=\"" + o.getValue() + "\" ";
        }
        ret += ">";
        ret += intoTag;
        ret = "</" + tag + ">";
        return "";
    }
    
    public static String generateButton(String text, String href,String classBt,String icon){
        String bt = "";
        bt += "<a href='" + href + "'><button type=\"button\" class=\""+classBt +"\">";
        bt += "<span class=\"glyphicon glyphicon-"+icon+"\"></span> ";
        bt += text + "</button></a>";
        return bt;
    }
    
    public static String generateButton(String text, String href,String classBt){
        String bt = "";
        bt += "<a href='" + href + "'><button type=\"button\" class=\""+classBt+"\"> ";
        bt += text + " </button></a>";
        return bt;
    }
    
    public static String getDateFromTimestamp(String timestamp){
        int l = Integer.parseInt(timestamp);
                String dateAsText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                          .format(new Date(l * 1000L));
                return dateAsText;
    }
    
    public static String generateForm(String action,String method,String content){
        String ret ="";
        ret += "<form method='" +method+"' action='"+action+"'>";
        ret += content;
        ret += "</form>";
        return ret;
    }
    
    public static String h1String(String text){
        return "<h1>" + text + "</h1>";
    }
    
    public static String generateH(int h,String text){
         return "<h"+h+">" + text + "</h"+h+">";
    }
    
    public static String generateHWithColor(int h,String text,String cClass){
         return "<h"+h+" class=\""+cClass+"\">" + text + "</h"+h+">";
    }
    
    public static String includeHead(){
        String ret = "";
        
        ret += "<head>";
        ret+= "<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n"+
        "<script src=\"https://code.jquery.com/jquery.js\"></script>\n"+
        "<script src=\"js/bootstrap.min.js\"></script>\n";
        ret += "</head>";
        return ret;
        
    }
    
    public static String centerInPage(String html){
        String ret = "";
        ret += "<div class=\"row\">"
            +  "<div class=\"col-md-2\">&nbsp.</div>"
            +  "<div class=\"col-md-8\">";
        ret += html;
        ret += "</div>";
        return ret;
    }
    
    public static String getTableFrom(ArrayList<String> columns, ArrayList<ArrayList> data){
        String html = "";
        html += "<table>";
        html += "<th>";
        Iterator<String> i = columns.iterator();
        while(i.hasNext()){
            html += "<td>"+i.next()+"</td>";
        }
        html += "</th>";
        Iterator<ArrayList> j = data.iterator();
        while(j.hasNext()){
            html += "<tr>";
            ArrayList<String> aux = j.next();
            Iterator<String> y = aux.iterator();
            while(y.hasNext()){
                html += "<td>" + y.next() + "</td>";
            }
            html += "</tr>";
        }
        return html+"<br>";
    }
    
    public static String getAllGroups(ArrayList<Group> groups, String user){
        String html = "";
        html += "<style> .table td {\n" +
                "   text-align: center;   \n" +
                "}"
                + "</style>";
        html += "<br>" + generateH(3, "Your Groups");
        html += "<table class=\"table table-condensed table-hover\">";
        html += "<tr>";
        html += "<td><b>Owner</b></td>";
        html += "<td><b>Group Name</b></td>";
        html += "<td><b>Creation Date</b></td>";
        html += "<td><b>Admin</b></td>";
        html += "</tr>";
        Iterator<Group> i = groups.iterator();
        while(i.hasNext()){
            Group aux = i.next();
            if(aux.getOwnerName().equals(user)){
                html += "<tr class=\"success\">";
                html += "<td>"+aux.getOwnerName()+"</td>";
                html += "<td><a href=\"GroupHome?g="+aux.getId()+"\">"+aux.getGroupName()+"</a></td>";
                html += "<td>"+getDateFromTimestamp(aux.getCreationDate())+"</td>";
                html += "<td><a href=\"newGroup?g="+ aux.getId() +"\"><button type=\"button\" class=\"btn btn-default btn-xs\">\n" +
                        "  <span class=\"glyphicon glyphicon-th\"></span> Manage\n" +
                        "</button></a></td>";
            }else{
                html += "<tr>";
                html += "<td>"+aux.getOwnerName()+"</td>";
                html += "<td><a href=\"GroupHome?g="+aux.getId()+"\">"+aux.getGroupName()+"</a></td>";
                html += "<td>"+getDateFromTimestamp(aux.getCreationDate())+"</td>";
                html += "<td><button type=\"button\" class=\"btn btn-danger btn-xs\">\n" +
                        "  <span class=\"glyphicon glyphicon-th\"></span> Disgruppami\n" +
                        "</button></td>";
            }
            html += "</tr>";
        }
        return html+"</table>";
    }
    
}
