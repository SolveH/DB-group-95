package com.ntnu;

import java.sql.ResultSet;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Created by kiddi on 14.03.2017.
 */

public class UserCommunication {
    Scanner scanner;
    Connector connector;

    /**
     * Constructor
     *
     * @param connector
     */
    public UserCommunication(Connector connector)
    {
        this.scanner = new Scanner(System.in);
        this.connector = connector;
    }

    /**
     *
     */
    public void handleWorkoutInsert() {
        System.out.println("To record a workout, please provide the following information:");

        System.out.print("\tEnter the workout type ID: ");
        int typeID = scanner.nextInt();
        scanner.nextLine(); // to remove everything after the number entered by the user

        System.out.print("\tEnter this workout's name: ");
        String name = scanner.nextLine();

        System.out.print("\tEnter the date and time of the workout (format: YYYY-MM-DD hh:mm:ss): ");
        String workoutDateTime = scanner.nextLine();

        System.out.print("\tEnter the workout duration: ");
        String duration = scanner.nextLine();

        System.out.print("\tEnter your shape: ");
        String shape = scanner.nextLine();

        System.out.print("\tEnter some notes about the workout: ");
        String note = scanner.nextLine();

        System.out.print("\tEnter the weather (leave empty if not applicable): ");
        String weather = scanner.nextLine();

        System.out.print("\tEnter the number of audience members (leave empty if not applicable): ");
        String audience = scanner.nextLine();

        System.out.print("\tEnter the quality of ventilation (leave empty if not applicable): ");
        String ventilation = scanner.nextLine();

        System.out.print("\tEnter the temperature (leave empty if not applicable): ");
        String temp = scanner.nextLine();

        int workoutID = connector.insertWorkout(typeID, workoutDateTime, name, duration, shape, note, weather, audience, ventilation, temp);

        ResultSet rs = connector.getExercisesForWorkout(typeID);

        try {
            while (rs.next()) {
                handleExerciseResultInsert(rs.getInt("EXERCISE_ID"),
                        rs.getString("EXERCISE_NAME"),
                        rs.getString("DESCRIPTION"),
                        workoutID);
            }
        } catch (Exception e) {
            System.out.println("Db error: " + e);
        }
    }

    /**
     *
     * @param exerciseID
     * @param exerciseName
     * @param exerciseDescription
     * @param workoutID
     */
    public void handleExerciseResultInsert(int exerciseID, String exerciseName, String exerciseDescription, int workoutID) {
        System.out.println("Record the results of exercise " + exerciseID + ": " + exerciseName + " ('" + exerciseDescription + "')");

        System.out.print("\tEnter the number of repetitions: ");
        int repetitions = scanner.nextInt();
        scanner.nextLine(); // to remove everything after the number entered by the user

        System.out.print("\tEnter the number of sets: ");
        int sets = scanner.nextInt();
        scanner.nextLine(); // to remove everything after the number entered by the user

        System.out.print("\tEnter the weight [kg]: ");
        float weight = scanner.nextFloat();
        scanner.nextLine(); // to remove everything after the number entered by the user

        System.out.print("\tEnter the distance [km]: ");
        float distance = scanner.nextFloat();
        scanner.nextLine(); // to remove everything after the number entered by the user

        System.out.print("\tEnter the duration [h]: ");
        float duration = scanner.nextFloat();
        scanner.nextLine(); // to remove everything after the number entered by the user

        System.out.print("\tEnter the time (format hh:mm:ss): ");
        String time = scanner.nextLine();

        connector.insertExerciseResult(exerciseID, workoutID, repetitions, sets, weight, distance, duration, time);
    }

    public void handleResultExercise(int exerciseID) {
        try {
            ResultSet rs = connector.getResultsForExercise(exerciseID);

            if (rs.next())
                System.out.println("Results for Exercise: "+rs.getString("EXERCISE_NAME"));
            System.out.println("\nWEIGHT\tREPETITIONS\tSETS\tDURATION\tTIME");
            while (rs.next()) {
                System.out.print(rs.getString("WEIGHT")+"\t");
                System.out.print(rs.getString("REPETITIONS") + "\t\t\t");
                System.out.print(rs.getString("SETS")+"\t\t");
                System.out.print(rs.getString("DURATION")+"\t\t");
                System.out.print(rs.getString("EXERCISE_TIME"));

                System.out.println("");
            }
            System.out.println("************************************************************************************");
        }

        catch (Exception e)
        {
            System.err.println("Db error: "+e);
        }
    }

    public void handleResultWorkout(int typeID) {
        try {
            ResultSet rs = connector.getExercisesForWorkout(typeID);
            while (rs.next())
                handleResultExercise(rs.getInt("EXERCISE_ID"));
        }

        catch (Exception e){
            System.err.println("DB ERROR: "+e);
        }
    }
}
