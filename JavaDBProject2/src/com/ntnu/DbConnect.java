package com.ntnu;

import java.sql.*;
import java.util.Properties;

public class DbConnect {

    protected Connection conn;
    public DbConnect()
    {}

    public void connect()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Properties p = new Properties();
            String url = "jdbc:mysql://mysql.stud.ntnu.no/romancb_db?user=romancb_db&password=CocaCola";
            conn = DriverManager.getConnection(url,p);
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e);
        }
    }

}
