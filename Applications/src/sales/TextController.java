package sales;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import common.*;
import java.sql.*;
import java.util.*;

/**
 * The TextController is used to only ask for input from the user.
 * To clarify: it displays a menu, asks for a choice, asks for specific input 
 * (if the input is invalid it re-asks for the specific input)
 * 
 * Using the received input, the TextController calls the SalesController 
 * methods. (ex: to create a new customer, the TextController asks all
 * the needed information from the user, create the Customer object, 
 * sends the object to the appropriate SalesController method)
 * 
 * @author Yanik
 *
 */
public class TextController {
	private SalesController salesController;
	
	private enum Command {
		REGISTER("Register New Customer"), 
		MODIFY_CUSTOMER("Modify Customer Package"), 
		RECRUIT("Cold Call"), 
		CHANGE_PASSWORD("Change Password"), 
		EXIT("Exit");
		
		String text;
		
		Command(String text) {
			this.text = text;
		}
		
		public String getText() {
			return text;
		}
	}
	
	public TextController(SalesController salesController) {
		this.salesController = salesController;
	}
	
	public void login() throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
		Scanner keyboard = new Scanner (System.in);
		
		boolean invalid = true;
		
		do 
		{
			System.out.print("Username: ");
			String username = keyboard.nextLine();
			System.out.print("Password: ");
			String password = keyboard.nextLine();
			
			boolean login = salesController.tryLogin(username, password);
			invalid = !login;
		} while (invalid);
		
		showMenu(keyboard);
	}
	
	public void showMenu(Scanner keyboard) {
		System.out.print("Welcome back ");
		salesController.getEmployeeName();
		System.out.println("!");
		
		// Get array of all commands
		Command[] commands = Command.values ();
		String menu = createMenu (commands);
		Command choice;

		do
		{       
			System.out.print (menu);
			choice = getUserChoice (commands, keyboard);
			switch (choice)
			{
			case REGISTER:
				registerCustomer(keyboard);
				break;
			case MODIFY_CUSTOMER:
				modifyCustomerPackage(keyboard);
				break;
			case RECRUIT:
				coldCall(keyboard);
				break;
			case CHANGE_PASSWORD:
				changePassword(keyboard);
				break;
			case EXIT:
				System.out.println("\nProgram exists...");
			}
		}
		while (choice != Command.EXIT);
		
		// Disconnects when stops
		
	}
	
	private Customer registerCustomer(Scanner keyboard) {
		System.out.println("\n--- Register Customer ---\n");
		String username = getUserString("Username: ", "^[a-zA-Z0-9_]{6,30}$", "The username should be between 6 and 30 "
				+ "characters and may contain only numbers, letters and underscores", keyboard);
		String name = getUserString("Full Name: ", "^[a-zA-Z -]{1,100}$", "The full name may contain "
				+ "only letters, spaces and dashes", keyboard);
		String address = getUserString("Address: ", "^.{1,100}$", "The address should be between "
				+ "1 and 100 characters", keyboard);
		String phone = getUserString("Phone Number: ", "^[0-9]{10}$",
				"The phone number should be 10 digits", keyboard);
		String email = getUserString("Email Address: ", "^[a-zA-Z0-9_-](?:(?:[a-zA-Z0-9_-]|(?:(\\.)(?!\\1))){0,30}"
				+ "[a-zA-Z0-9_-])?@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,30}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]"
				+ "{0,30}[a-zA-Z0-9])?)*$", "The email is invalid",keyboard);
		
		// Ask controller to display possible packages using the textView
		salesController.getAllPackages();
		
		String packageId = getUserString("Choose Package ID: ", "^[0-9]{4}$", "The Package ID should be"
				+ " 4 digits", keyboard);
		
		Customer newCustomer = new Customer();
		newCustomer.setUsername(username);
		newCustomer.setName(name);
		newCustomer.setAddress(address);
		newCustomer.setPhoneNumber(phone);
		newCustomer.setEmailAddress(email);
		newCustomer.setPackageId(packageId);
		
		boolean isRegistered = false;
		isRegistered = salesController.registerCustomer(newCustomer);
		
		if (isRegistered) {
			scheduleVisit(newCustomer, keyboard);
			return newCustomer;
		} else {
			return null; // Return null since customer is not registered
		}
	}
	
	private void scheduleVisit(Customer customer, Scanner keyboard) {
		java.sql.Date date;
		int hour;
		boolean scheduled = false;
		
		System.out.println("\n--- Schedule a visit ---\n");

		do {
			date = getUserFutureDate("Write desired date (MM/DD/YYYY): ", keyboard);
			salesController.getPossibleVisitHours(date);
			hour = Integer.parseInt(getUserString("Choose hour (HH:MM): ", "^(0?[0-9]|1?[0-9]|2[0-4]):00$", 
					"Invalid hour or invalid format (format should be HH:MM)", keyboard).split(":")[0]);
		
			scheduled = salesController.scheduleVisit(date, hour, customer);
		} while (!scheduled);
	}
	
	public void modifyCustomerPackage(Scanner keyboard) {
		System.out.println("\n--- Modify User's Package ---\n");
		
		String username = getUserString("Customer Username: ", "^.+$", "The username shouldn't be empty", keyboard);
		
		if (salesController.searchCustomer(username)) {
			salesController.getAllPackages();
			
			String packageId = getUserString("Choose Package ID: ", "^[0-9]{4}$", "The Package ID should be"
					+ " 4 digits", keyboard);
			
			salesController.modifyUserPackage(username, packageId);
		}
	}
	
	public void coldCall(Scanner keyboard) {
		System.out.println("\n--- Cold Call ---\n");
		System.out.println("Call:");
		
		salesController.generateLocalNumber();
		
		String isSale = getUserString("Is it a sale? (yes/no): ", "^([Yy][Ee][Ss]|[Nn][Oo])$", 
				"Please input \"Yes\" or \"No\"", keyboard);
		
		if (isSale.matches("^[Yy][Ee][Ss]$")) {
			Customer newCustomer = registerCustomer(keyboard);
			salesController.recordSalesCall(newCustomer.getUsername());
		} else {
			salesController.recordSalesCall(null);
		}
	}
	
	private void changePassword(Scanner keyboard) {
		System.out.println("\n--- Change Password ---\n");
		
		String password = getUserString("New Password: ", "^.{6,}$", "The password should be at least 6 characters", keyboard);
		String passwordRepeat = getUserString("Repeat Password: ", ".+", "Please repeat password", keyboard);
		
		if (password.equals(passwordRepeat) == false) {
			System.out.println();
			System.out.println("Passwords do not match!");
		} else {
			salesController.changeSalesPassword(password);
		}
	}
	
	// Helper method to get input for future date
	private java.sql.Date getUserFutureDate(String message, Scanner keyboard) {
		java.sql.Date sqlDate = null;
		boolean invalid = true;
		String formatError = "Invalid date. The date should be in the MM/DD/YYYY format";
		String pastDateError = "This date is not in the future";
		String invalidDateError = "Invalid date";
		
		do 
		{
			try 
			{
				System.out.print(message);
				String input = keyboard.nextLine();
				input = input.trim();
				
				if (input.matches("^\\d{2}\\/\\d{2}\\/\\d{4}$") == false) {
					System.out.println(formatError);
				} else {
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
					format.setLenient(false);
					java.util.Date utilDate = format.parse(input);
					sqlDate = new java.sql.Date(utilDate.getTime());
					
					if (new java.util.Date().after(utilDate)) {
						System.out.println(pastDateError);
					} else {
						invalid = false;
					}
				}
			} catch (Exception e) {
				System.out.println(invalidDateError );
			}
		} while (invalid);
		
		return sqlDate;
	}
	
	// Helper method to get input for past date
	private java.sql.Date getUserPastDate(String message, Scanner keyboard) {
		java.sql.Date sqlDate = null;
		boolean invalid = true;
		String formatError = "Invalid date. The date should in the MM/DD/YYYY format";
		String pastDateError = "This date is not in the past";
		String invalidDateError = "Invalid date";
		
		do 
		{
			try 
			{
				System.out.print(message);
				String input = keyboard.nextLine();
				input = input.trim();
				
				if (input.matches("^\\d{2}\\/\\d{2}\\/\\d{4}$") == false) {
					System.out.println(formatError);
				} else {
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
					format.setLenient(false);
					java.util.Date utilDate = format.parse(input);
					sqlDate = new java.sql.Date(utilDate.getTime());
					
					if (new java.util.Date().before(utilDate)) {
						System.out.println(pastDateError);
					} else {
						invalid = false;
					}
				}
			} catch (Exception e) {
				System.out.println(invalidDateError );
			}
		} while (invalid);
		
		return sqlDate;
	}
	
	// Helper method to get an input string
	private String getUserString(String message, String regex, String errorMsg, Scanner keyboard) {
		String input = null;
		boolean invalid = true;
		
		do 
		{
			try 
			{
				System.out.print(message);
				input = keyboard.nextLine();
				input = input.trim();
				if (input.matches(regex)) {
					invalid = false;
				} else {
					System.out.println(errorMsg);
				}
			} catch (java.util.InputMismatchException e) {
				System.out.println(errorMsg);
				// Does nothing, loop continues
			}
		} while (invalid);
		
		return input;
	}
	
	// Helper method to get a double input
	private double getUserDouble(String message, double min, double max, String errorMsg, Scanner keyboard) {
		double input = 0;
		boolean invalid = true;
		
		do 
		{
			try 
			{
				System.out.print(message);
				input = keyboard.nextDouble();
				if (input >= max && input <= min) {
					invalid = false;
				} else {
					System.out.println(errorMsg);
				}
			} catch (java.util.InputMismatchException e) {
				System.out.println(errorMsg);
				keyboard.nextLine (); // consumes the invalid value
			}
		} while (invalid);
		
		keyboard.nextLine (); // Consumes the rest of the line
		
		return input;
	}
	
	// Helper method to get an int input
		private double getUserInt(String message, double min, double max, String errorMsg, Scanner keyboard) {
			int input = 0;
			boolean invalid = true;
			
			do 
			{
				try 
				{
					System.out.print(message);
					input = keyboard.nextInt();
					if (input >= max && input <= min) {
						invalid = false;
					} else {
						System.out.println(errorMsg);
					}
				} catch (java.util.InputMismatchException e) {
					System.out.println(errorMsg);
					keyboard.nextLine (); // consumes the invalid value
				}
			} while (invalid);
			
			keyboard.nextLine (); // Consumes the rest of the line
			
			return input;
		}
	
	// Helper method to validate the user choice
	private Command getUserChoice (Command[] commands, Scanner keyboard)
	{ 
		boolean invalid = true;
		int maxChoiceValue = commands.length;
		int userChoice = 0;
		do
		{
			try
			{
				userChoice = keyboard.nextInt ();
				if (userChoice <= 0 || userChoice > maxChoiceValue)
				{
					System.out.print ("Invalid choice! Enter a number between"
							+ " 1 and " + maxChoiceValue + ": ");
				} else {
					invalid = false;
				}
				
			}
			catch (java.util.InputMismatchException e)
			{
				System.out.print ("Invalid choice! Enter a number between"
						+ " 1 and " + maxChoiceValue + ": ");
				keyboard.nextLine (); // consumes the invalid value
			}
		}
		while (invalid);
		
		keyboard.nextLine (); // Consumes the rest of the line
		
		return commands [userChoice - 1];
	}
	
	// Method to create a menu based on the possible commands
	private String createMenu (Command[] commands)
	{
		String menu = "\nSelect a choice from the menu:\n";
		int numChoices = commands.length;
		for (int i = 0 ; i < numChoices ; i++)
			menu += "\t" + (i + 1) + " - " + commands[i].getText() + "\n";
		menu += "\nEnter your choice: ";

		return menu;
	}
}
