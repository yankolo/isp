package technicians;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import common.*;

public class TextController {
	private TechniciansController techniciansController;
	private String technicianId;
	
	private interface c {
		String getText();
	}
	
	private enum Command implements c{
		WRITE_NOTES("Write notes for a visit"), 
		VISITS_INFO("Upcoming visits"), 
		MANAGE_EQUIPMENT("Manage equipment"), 
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
	
	private enum EquipmentCommand implements c{
		TOOLS("Tools"),
		ITEMS("Items"),
		OVERVIEW("Overview");
		
		String text;
		
		EquipmentCommand(String text) {
			this.text = text;
		}
		
		public String getText() {
			return text;
		}
	}
	
	public TextController(TechniciansController techniciansController) {
		this.techniciansController = techniciansController;
	}
	
	public void login() throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException, IOException {
		Scanner keyboard = new Scanner (System.in);
		
		do 
		{
			System.out.print("username: ");
			String username = keyboard.nextLine();
			System.out.print("password: ");
			String password = keyboard.nextLine();
			
			technicianId = techniciansController.tryLogin(username, password);
		} while (technicianId == null);
		
		showMainMenu(keyboard);
	}
	
	public void showMainMenu(Scanner keyboard) throws SQLException, IOException {
		// Get array of all commands
		Command[] commands = Command.values ();
		String menu = createMenu (commands);
		Command choice;

		do
		{       
			System.out.print (menu);
			choice = (Command)getUserChoice (commands, keyboard);
			switch (choice)
			{
			case WRITE_NOTES:
				writeNotes(keyboard);
				break;
			case VISITS_INFO:
				visitInfo(keyboard);
				break;
			case MANAGE_EQUIPMENT:
				manageEquipment(keyboard);
				break;
			case CHANGE_PASSWORD:
				changePassword(keyboard);
				break;
			case EXIT:
				// Do nothing
			}
		}
		while (choice != Command.EXIT);
		
		// Disconnects when stops
		
	}
	
	private void writeNotes(Scanner keyboard) throws SQLException, IOException {
		ArrayList<Visit> visits = null;
		
		visits = techniciansController.getListOfVisits(technicianId, false);
		
		if(visits.size() > 0) {
			int choice = getUserInt("\nSelect a visit: ", 1, visits.size()+1, "Your choice should be between " + 1 + " and " + (visits.size()+1), keyboard);
			String note = getUserString("\nWrite note: ", "^.*{1,500}$", "The note cannot be longer than 500 characters and needs a minimum of 1 character", keyboard);
			
			techniciansController.saveVisitNote(visits.get(choice-1).getVisitId(), note);
		} else {
			System.out.println("\nYou do not have any visits in the database.");
		}
		
	}
	
	private void visitInfo(Scanner keyboard) throws SQLException, IOException {
		ArrayList<Visit> visits = null;
		
		visits = techniciansController.getListOfVisits(technicianId, true);
		
		if(visits.size() > 0) {
			int choice = getUserInt("\nSelect a visit to see more information: ", 1, visits.size()+1, "Your choice should be between " + 1 + " and " + visits.size()+1, keyboard);
			techniciansController.showInfo(visits.get(choice-1).getCustomerId());	
		} else {
			System.out.println("\nYou do not have any visits in the database.");
		}
	}
	
	public void showEquipmentMenu(Scanner keyboard) throws SQLException, IOException {
		// Get array of all commands
		EquipmentCommand[] commands = EquipmentCommand.values ();
		String menu = createMenu (commands);
		EquipmentCommand choice;

		System.out.print (menu);
		choice = (EquipmentCommand)getUserChoice (commands, keyboard);
		switch (choice)
		{
		case ITEMS:
			manageItems(keyboard);
			break;
		case TOOLS:
			manageTools(keyboard);
			break;
		case OVERVIEW:
			displayOverview();
			break;
		}
		
		// Disconnects when stops
		
	}
	
	private void displayOverview() throws SQLException, IOException {
		techniciansController.displayAllEquipment(technicianId);
	}
	
	private void manageEquipment(Scanner keyboard) throws SQLException, IOException {
		showEquipmentMenu(keyboard);
	}
	
	private void manageItems(Scanner keyboard) throws SQLException, IOException {
		boolean invalid = true;
		do {
			try {
				int screws = getUserInt("\nHow many screws did you use?", 0, 200, 
						"The number of screws should be a whole number and can't be negative nor larger than 200 ", keyboard);
				int bolts = getUserInt("\nHow many bolts did you use?", 0, 200, 
						"The number of bolts should be a whole number and can't be negative nor larger than 200", keyboard);
				int fiberOptic = getUserInt("\nHow many feet of fiber optic cable did you use?", 0, 50, 
						"The number feet of fiber optic cable should be a whole number and can't be negative nor larger than 50", keyboard);
				techniciansController.orderItems(technicianId, screws, bolts, fiberOptic);
				invalid = false;
			} catch (SQLException e) {
				int code = e.getErrorCode(); 
				
				if (code == 20002) {
					System.out.println("A number you inputted was too large!");
				} else {
					throw e;
				}
			}
		} while (invalid);
		
		
	}
	
	private void manageTools(Scanner keyboard) throws SQLException, IOException {
		String[] tools = techniciansController.getFunctioningTools(technicianId);
		String[] allTools = {"FUSION_SPLICER", "DRILL"};
		
		boolean[] toReplace = new boolean[2];
		if(tools.length == 0) {
			System.out.println("All your tools are currently being ordered.");
		}
		else {
			for (int i = 0; i < allTools.length; i++) {
				int index = findInArray(tools, allTools[i]);
				if(index != -1) {
					toReplace[i] = getUserString("\nDoes your " + tools[index] + " need to be replaced? (y/n)", "^[yn]$", "You should answer with either 'y' or 'n'", keyboard).equals("y");
				}
			}
			techniciansController.orderTools(technicianId, toReplace[0], toReplace[1]);
		}
	}
	
	private int findInArray(String[] a, String s) {
		for(int i = 0; i < a.length; i++) {
			if(a[i].equals(s)) {
				return i;
			}
		}
		return -1;
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
	
	// Helper method to get an int input
		private int getUserInt(String message, int min, int max, String errorMsg, Scanner keyboard) {
			int input = 0;
			boolean invalid = true;
			
			do 
			{
				try 
				{
					System.out.print(message);
					input = keyboard.nextInt();
					if (input <= max && input >= min) {
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
	private c getUserChoice (c[] commands, Scanner keyboard)
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
	private String createMenu (c[] commands)
	{
		String menu = "\nSelect a choice from the menu:\n";
		int numChoices = commands.length;
		for (int i = 0 ; i < numChoices ; i++)
			menu += "\t" + (i + 1) + " - " + commands[i].getText() + "\n";
		menu += "\nEnter your choice: ";

		return menu;
	}
	
	private void changePassword(Scanner keyboard) throws SQLException {
		System.out.println("\n--- Change Password ---\n");
		
		String password = getUserString("New Password: ", "^.{6,}$", "The password should be at least 6 characters", keyboard);
		String passwordRepeat = getUserString("Repeat Password: ", ".+", "Please repeat password", keyboard);
		
		if (password.equals(passwordRepeat) == false) {
			System.out.println();
			System.out.println("Passwords do not match!");
		} else {
			techniciansController.changeTechniciansPassword(password, technicianId);
		}
	}


	// Have a command list 
	
	// when textcontrolelr is first ran, asks for username and password
	// after authenticated, runs main run method that displays the possible commands (in a loop)
}
