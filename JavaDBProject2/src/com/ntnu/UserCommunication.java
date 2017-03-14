package com.ntnu;

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

    public void handleWorkoutInsert() {
        System.out.println("### To record a workout, please provide the following information. ###");

        System.out.print("Enter the workout type ID: ");
        int typeID = scanner.nextInt();
        scanner.nextLine(); // to remove everything after the workout type id entered by the user

        System.out.print("Enter this workout's name: ");
        String name = scanner.nextLine();

        System.out.print("Enter the date and time of the workout (format: YYYY-MM-DD hh:mm:ss): ");
        String workoutDateTime = scanner.nextLine();

        System.out.print("Enter the workout duration: ");
        String duration = scanner.nextLine();

        System.out.print("Enter your shape: ");
        String shape = scanner.nextLine();

        System.out.print("Enter some notes about the workout: ");
        String note = scanner.nextLine();

        System.out.print("Enter the weather (leave empty if not applicable): ");
        String weather = scanner.nextLine();

        System.out.print("Enter the number of audience members (leave empty if not applicable): ");
        String audience = scanner.nextLine();

        System.out.print("Enter the quality of ventilation (leave empty if not applicable): ");
        String ventilation = scanner.nextLine();

        System.out.print("Enter the temperature (leave empty if not applicable): ");
        String temp = scanner.nextLine();

        connector.insertWorkout(typeID, workoutDateTime, name, duration, shape, note, weather, audience, ventilation, temp);
    }
}
