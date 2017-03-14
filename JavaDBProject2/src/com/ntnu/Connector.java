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

    /**
     *
     *
     * @param typeID
     * @param workoutDateTime
     * @param name
     * @param duration
     * @param shape
     * @param note
     * @param weather
     * @param audience
     * @param ventilation
     * @param temp
     *
     * @return Auto generated workoutID. Returns -1 if an error occurred.
     */
    public int insertWorkout(int typeID, String workoutDateTime, String name, String duration, String shape,
                              String note, String weather, String audience, String ventilation, String temp) {
        try {
            PreparedStatement stmt = null;

            String sql = "INSERT INTO WORKOUT (TYPE_ID, WORKOUT_DATE, NAME, DURATION, SHAPE, NOTE, WEATHER, AUDIENCE, VENTILATION, TEMP)\n" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?);";

            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
            //System.out.println("Rows impacted by insertExercise: " + rows);

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Error: Could not get auto generated keys.");
                }
            }

        } catch (Exception e) {
            System.out.println("Db error: "+e);
        }

        return -1;
    }

    /**
     *
     * @param exerciseID
     * @param workoutID
     * @param repetitions
     * @param sets
     * @param weight
     * @param distance
     * @param duration
     * @param time
     */
    public void insertExerciseResult(int exerciseID, int workoutID, int repetitions, int sets, float weight, float distance, float duration, String time) {
        try {
            PreparedStatement stmt = null;

            String sql = "INSERT INTO EXERCISE_RESULT (EXERCISE_ID, WORKOUT_ID, REPETITIONS, SETS, WEIGHT, DISTANCE, DURATION, EXERCISE_TIME)\n" +
                         "VALUES (?,?,?,?,?,?,?,?)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, exerciseID);
            stmt.setInt(2, workoutID);
            stmt.setInt(3, repetitions);
            stmt.setInt(4, sets);
            stmt.setFloat(5, weight);
            stmt.setFloat(6, distance);
            stmt.setFloat(7, duration);
            stmt.setString(8, time);

            int rows = stmt.executeUpdate();
            //System.out.println("Rows impacted by insertExercise: " + rows);


        } catch (Exception e) {
            System.out.println("Db error: "+e);
        }
    }

    /**
     *
     */
    public void printWorkoutsWithExercises() {
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT WORKOUT_TYPE.TYPE_ID, WORKOUT_TYPE.WORKOUT_NAME, EXERCISE.EXERCISE_ID, EXERCISE.EXERCISE_NAME, EXERCISE.DESCRIPTION\n" +
                           "FROM EXERCISE, EXERCISE_WORKOUT_TYPE, WORKOUT_TYPE\n" +
                           "WHERE EXERCISE_WORKOUT_TYPE.TYPE_ID = WORKOUT_TYPE.TYPE_ID\n" +
                           "AND EXERCISE_WORKOUT_TYPE.EXERCISE_ID = EXERCISE.EXERCISE_ID;";

            ResultSet rs = stmt.executeQuery(sql);
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

        } catch (Exception e) {
            System.out.println("Db error: "+e);
        }
    }

    /**
     *
     * @param typeID
     * @return
     */
    public ResultSet getExercisesForWorkout(int typeID) {
        try {
            PreparedStatement stmt = null;

            String sql = "SELECT EXERCISE.EXERCISE_ID, EXERCISE.EXERCISE_NAME, EXERCISE.DESCRIPTION\n" +
                    "FROM EXERCISE, EXERCISE_WORKOUT_TYPE, WORKOUT_TYPE\n" +
                    "WHERE EXERCISE_WORKOUT_TYPE.TYPE_ID = WORKOUT_TYPE.TYPE_ID\n" +
                    "AND EXERCISE_WORKOUT_TYPE.EXERCISE_ID = EXERCISE.EXERCISE_ID\n" +
                    "AND WORKOUT_TYPE.TYPE_ID = ?;";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, typeID);

            return stmt.executeQuery();

        } catch (Exception e) {
            System.out.println("Db error: "+e);
        }

        return null;
    }
}
