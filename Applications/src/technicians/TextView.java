package technicians;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import common.Visit;

public class TextView {
	
	public void displayMessage(String message) {
		System.out.println("\n" + message + "\n");
	}
	
	public void displayVisits(ArrayList<Visit> visitsList) {
		for (int i = 0; i < visitsList.size(); i++) {
			System.out.println((i+1) + " " + visitsList.get(i).toString());
		}
	}
	
	public void displayUserToTech(String name, String address, String phone) {
		System.out.println("Name:\t\t" + name + "\nAddress:\t" + address + "\nPhone Number:\t" + phone);
	}
	
	public void displayAllEquipment(ResultSet items, ResultSet tools, ResultSet orders) throws SQLException{
		System.out.println("\nYour current equipment:");
		int i = 1;
		while(items.next()) {
			String name = items.getString(1);
			int amount = items.getInt(2);
			System.out.println(i + "- " + name + ": " + amount);
			i++;
		}
		
		while(tools.next()) {
			String name = tools.getString(1);
			String isFunctioning = tools.getString(2);
			System.out.println(i + "- " + name + ": " + isFunctioning);
			i++;
		}
		
		System.out.println("\nCurrent equipment being ordered:");
		
		while(orders.next()) {
			String name = orders.getString(1);
			String amount = orders.getString(2);
			System.out.println(name + ": " + amount);
		}
		
		System.out.print("(Press enter to continue)");
		Scanner s = new Scanner(System.in);
		s.nextLine();
	}
}
