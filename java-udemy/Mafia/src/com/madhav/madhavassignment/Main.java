package com.madhav.madhavassignment;

import java.util.Scanner;

public class Main {

    private static Scanner scan=new Scanner(System.in);
    public static void main(String[] args) {
	// write your code he
        System.out.println("Welcome to Mafia");
        System.out.println("Enter Number of players:");
        int no_of_players=scan.nextInt();
        scan.nextLine();
        Start start=new Start(no_of_players);
        start.play();
    }
}
