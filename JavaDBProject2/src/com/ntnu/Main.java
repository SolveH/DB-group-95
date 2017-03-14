package com.ntnu;

public class Main {

    public static void main(String[] args) {
		Connector c = new Connector();
		c.connect();
		c.printExercises();

		UserCommunication uc = new UserCommunication(c);
		uc.handleWorkoutInsert();
    }
}
