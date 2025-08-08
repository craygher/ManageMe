package com.example.manageme;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
public class DBConnection {
    static final String url = "jdbc:mysql://localhost:3306/managemedb";
    static final String user = "root";
    static final String password = "";
    static Connection conn;
    static String LoggedUser;




    protected static Connection getConnection() throws Exception{
        if(conn == null){
            conn = DriverManager.getConnection(url, user, password);
        }
        String query="Select * from persona";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()){}


        return conn;
    }


    protected void login(String username, String password) throws Exception{
        String query = "select password from utente where Nome_Utente=?";
        String DBPassword = "";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        boolean isRecord=rs.next();
        if(!isRecord){ throw new Exception("Nessun utente Trovato"); }
        DBPassword=rs.getString("Password");

        if (BCrypt.checkpw(password,DBPassword)) {
            System.out.println("✅ Password corretta!");
            LoggedUser=username;
        } else {
            throw new Exception("❌ Password errata!");
        }
    }

    protected ResultSet getDeposito(String query) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        return rs;
    }

    protected void insert(String query) throws SQLException {

    }
}


