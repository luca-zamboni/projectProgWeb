/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package html;

import java.util.ArrayList;
import java.util.Iterator;
import models.Group;

/**
 *
 * @author luca
 */
public class Html {
    
    public static final String POST = "POST";
    public static final String GET = "GET";        
    
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
    
    public static String getAllGroups(ArrayList<Group> groups){
        String html = "";
        html += "<br><h3>Your groups</h3>";
        html += "<table class=\"table table-bordered\">";
        html += "<tr>";
        html += "<td><b>Owner</b></td>";
        html += "<td><b>Group Name</b></td>";
        html += "<td><b>Creation Date</b></td>";
        html += "</tr>";
        Iterator<Group> i = groups.iterator();
        while(i.hasNext()){
            Group aux = i.next();
            html += "<tr>";
            html += "<td>"+aux.getOwnerName()+"</td>";
            html += "<td>"+aux.getGroupName()+"</td>";
            html += "<td>"+aux.getCreationDate()+"</td>";
            html += "</tr>";
        }
        return html+"</table>";
    }
    
}
