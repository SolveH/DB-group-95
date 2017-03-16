package com.ntnu;

public class Main {

    public static void main(String[] args) {
		Connector c = new Connector();
		c.connect();

		UserCommunication uc = new UserCommunication(c);
		uc.startupQuestions();
    }
}
