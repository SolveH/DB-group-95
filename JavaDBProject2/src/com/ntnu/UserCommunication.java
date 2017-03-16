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

    public UserCommunication(Connector connector)
    {
        this.scanner = new Scanner(System.in);
        this.connector = connector;
    }

    public void startupQuestions() {
        while (true){
            System.out.println("Choose what you want to do:");
            System.out.println("Enter 1 if you want to add a workout " +
                    "\nEnter 2 if you want to add a workout type:" +
                    "\nEnter 3 if you want to get the results for a specific exercise:" +
                    "\nEnter 4 if you want to get the results for all the exercises in a workout:" +
                    "\nEnter 5 if you want to exit the application:");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if(choice == 1) {
                printWorkoutsWithExercises();
                handleWorkoutInsert();
            }else if(choice == 2) {
                handleWorkoutTypeInsert();
            }else if(choice == 3) {
                printWorkoutsWithExercises();
                System.out.println("Please enter a valid Exercise ID:");
                handleResultExercise(scanner.nextInt());
            }else if(choice == 4) {
                printWorkoutsWithExercises();
                System.out.println("Please enter a valid Workout ID:");
                handleResultWorkout(scanner.nextInt());
            }else if(choice == 5) {
                System.out.println("Bye :)");
                break;
            }else{
                System.out.println("Error: You didn't enter one of the numbers asked for.");
            }
        }
    }

    public void printWorkoutsWithExercises() {
        System.out.println("************************************************************************************");
        System.out.println("The following workout types with corresponding exercises are available as templates:");
        connector.printWorkoutsWithExercises();
        System.out.println("************************************************************************************");
    }

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

        System.out.print("\tEnter the duration [min]: ");
        float duration = scanner.nextFloat();
        scanner.nextLine(); // to remove everything after the number entered by the user

        System.out.print("\tEnter the time (format hh:mm:ss): ");
        String time = scanner.nextLine();

        connector.insertExerciseResult(exerciseID, workoutID, repetitions, sets, weight, distance, duration, time);
    }

    public void handleWorkoutTypeInsert(){
        System.out.println("Add exercise type");
        System.out.print("\tEnter this workout type's name: ");
        String name = scanner.nextLine();

        int typeID = connector.insertWorkoutType(name);

        System.out.print("How many exercises do your want to add to this workout? ");
        int numExercises = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numExercises; i++) {
            handleExerciseTypeInsert(typeID);
        }
    }

    public void handleExerciseTypeInsert(int workoutTypeID) {
        System.out.println("Record an exercise type");

        System.out.print("\tEnter the exercise type name: ");
        String exerciseTypeName = scanner.nextLine();

        System.out.print("\tEnter a description: ");
        String exerciseTypeDescription = scanner.nextLine();

        connector.printExerciseGroups();
        System.out.print("\tEnter to which groupID this exercise belongs: ");
        int groupID = scanner.nextInt();
        scanner.nextLine(); // to remove everything after the number entered by the user

        int exerciseTypeID = connector.insertExerciseType(exerciseTypeName, exerciseTypeDescription, groupID);
        connector.insertExerciseWorkoutType(exerciseTypeID, workoutTypeID);
    }

    public void handleResultExercise(int exerciseID) {
        try {
            ResultSet rs = connector.getResultsForExercise(exerciseID);

            if (rs.first())
                System.out.println("Results for Exercise: "+rs.getString("EXERCISE_NAME") + " ID: " + rs.getString("EXERCISE_ID"));
            System.out.println("\nWEIGHT\tREPETITIONS\tSETS\tDURATION\tTIME");
            rs.previous();
            while (rs.next()) {
                System.out.print(rs.getString("WEIGHT")+"\t");
                System.out.print(rs.getString("REPETITIONS") + "\t\t\t");
                System.out.print(rs.getString("SETS")+"\t\t");
                System.out.print(rs.getString("DURATION")+"\t\t");
                System.out.print(rs.getString("EXERCISE_TIME"));

                System.out.println("");
            }
            System.out.println("\n************************************************************************************\n");
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
