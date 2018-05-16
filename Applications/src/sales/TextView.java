package sales;

import common.*;
import common.Package;

/**
 * The TextView is reponsible to print information on the screen
 * All of its methods will be called from the SalesController
 * The TextView might receive materialized database objects 
 * or string messages (ex: confirmation messages, error messages, etc.)
 * 
 * Methods that receive objects should know how to print those objects
 * (ex: SalesController sends an array of package objects to display
 * and the TextView should know how to print those packages)
 * 
 * Example when the TextView might be used:
 * When registering a customer, the TextController asks the user
 * for all the necessary input, then to assign a package to the customer
 * the TextController asks the SalesController for the available packages.
 * The SalesController uses the TextView to display the available packages.
 * The TextController asks which package to assign the customer.
 * The TextController creates the Customer object, sends it to the 
 * appropriate SalesController method, which creates the customer 
 * in the database. Then the SalesController sends a confirmation message
 * to the TextView. The TextView displays the confirmation message that the 
 * customer is created.
 * 
 * @author Yanik
 *
 */
public class TextView {
	public void displayMessage(String message) {
		System.out.println("\n" + message + "\n");
	}
	
	public void displayMessageOnLine(String message) {
		System.out.print(message);
	}
	
	public void displayPackages(Package[] packages) {
		System.out.println();
		System.out.println("All possible packages: ");
		System.out.println("-------------------------------------------------------");
		
		for (int i = 0; i < packages.length; i++) {
			System.out.printf("Package ID: %s%n", packages[i].getPackageId());
			System.out.printf("Name: %s%n", packages[i].getName());
			System.out.printf("Description: %s%n", packages[i].getDescription());
			System.out.printf("Cost: %.2f$%n", packages[i].getCost());
			System.out.printf("Upload Speed: %.2fMbps%n", packages[i].getUp());
			System.out.printf("Download Speed: %.2fMbps%n", packages[i].getDown());
			System.out.printf("Bandwidth: %.0fGB%n", packages[i].getBandwidth());
			System.out.printf("Overage charges: %.2f$/GB%n", packages[i].getOverage());
			System.out.println("-------------------------------------------------------");
		}
		
		System.out.println();
	}
	
	public void displayPossibleHours(int[] hours) {
		System.out.println();
		System.out.println("Possible visit hours:");
		System.out.println();
		
		for (int hour : hours) {
			System.out.printf("%d:00%n", hour);
		}
		System.out.println();
	}
}
