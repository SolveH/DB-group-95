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

    public void printWorkoutsWithExercises() {
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT WORKOUT_TYPE.TYPE_ID, WORKOUT_TYPE.WORKOUT_NAME, EXERCISE.EXERCISE_ID, EXERCISE.EXERCISE_NAME, EXERCISE.DESCRIPTION\n" +
                           "FROM EXERCISE, EXERCISE_WORKOUT_TYPE, WORKOUT_TYPE\n" +
                           "WHERE EXERCISE_WORKOUT_TYPE.TYPE_ID = WORKOUT_TYPE.TYPE_ID\n" +
                           "AND EXERCISE_WORKOUT_TYPE.EXERCISE_ID = EXERCISE.EXERCISE_ID;";

            ResultSet rs = stmt.executeQuery(query);
            int currentWorkoutID = -1;
            while (rs.next()) {
                int newWorkoutID = rs.getInt("TYPE_ID");
                if (newWorkoutID != currentWorkoutID) {
                    System.out.print("Workout Type: " + rs.getString("WORKOUT_NAME"));
                    System.out.print(" (TypeID " + newWorkoutID + "):\n");
                    currentWorkoutID = newWorkoutID;
                }

                System.out.print("\tExercise ID " + rs.getInt("EXERCISE_ID"));
                System.out.print(": " + rs.getString("EXERCISE_NAME"));
                System.out.print(" - '" + rs.getString("DESCRIPTION") + "'\n");
            }
        }

        catch (Exception e)
        {
            System.out.println("Db error: "+e);
        }
    }
}
