package com.ntnu;

public class Main {

    public static void main(String[] args) {
		Connector c = new Connector();
		c.connect();

		//System.out.println("************************************************************************************");
		//System.out.println("The following workout types with corresponding exercises are available as templates:");
		//c.printWorkoutsWithExercises();
		//System.out.println("************************************************************************************");

		UserCommunication uc = new UserCommunication(c);
		uc.startupQuestions();
    }
}
