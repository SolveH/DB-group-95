package com.ntnu;

import java.sql.*;

/**
 * Created by kiddi on 14.03.2017.
 */
public class Connector extends DbConnect {

    public void printExercises()
    {
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM `WORKOUT`";
            ResultSet rs = stmt.executeQuery(query);
            int nr = 1;
            while (rs.next()) {
                System.out.println(" "+nr++ + " "+ rs.getString("name"));
            }
        }

        catch (Exception e)
        {
            System.out.println("Db error: "+e);
        }
    }
}
