package com.ntnu;

import java.sql.*;
import java.time.*;

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

    public void insertWorkout(int typeID, String workoutDateTime, String name, String duration, String shape,
                              String note, String weather, String audience, String ventilation, String temp) {
        try {
            PreparedStatement stmt = null;

            String sql = "INSERT INTO WORKOUT (TYPE_ID, WORKOUT_DATE, NAME, DURATION, SHAPE, NOTE, WEATHER, AUDIENCE, VENTILATION, TEMP)\n" +
                           "VALUES (?,?,?,?,?,?,?,?,?,?);";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, typeID);
            stmt.setString(2, workoutDateTime);
            //stmt.setTimestamp(2, workoutDateTime.toLocalDate().getTime());
            stmt.setString(3, name);
            stmt.setString(4, duration);
            stmt.setString(5, shape);
            stmt.setString(6, note);
            stmt.setString(7, weather);
            stmt.setString(8, audience);
            stmt.setString(9, ventilation);
            stmt.setString(10, temp);

            int rows = stmt.executeUpdate();
            System.out.println("Rows impacted by insertWorkout: " + rows );


        } catch (Exception e) {
            System.out.println("Db error: "+e);
        }
    }
}
