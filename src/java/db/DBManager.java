/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jibbo
 */
public class DBManager implements Serializable {

 // transient == non viene serializzato
    private static transient Connection con;
    private static final String URL_PREFIX = "jdbc:sqlite:";

    public DBManager(String dburl) throws SQLException {

        try {

            Class.forName("org.sqlite.JDBC", true,
                    getClass().getClassLoader());
            Connection con = DriverManager.getConnection(URL_PREFIX+dburl);
            this.con = con;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e.toString(), e);
        }
    }
    
    public boolean login(String user, String passwd) throws SQLException{
        String sql= "select count(*) where username=? passwd=?";
        PreparedStatement stm = con.prepareStatement("sql");
        stm.setString(1, user);
        stm.setString(2, passwd);
        ResultSet rs = stm.executeQuery();
        try{
            if(rs.next()){
                return rs.getInt(0)==1;
            }
        }finally{
            rs.close();
            stm.close();
        }
        return false;
    }
}
