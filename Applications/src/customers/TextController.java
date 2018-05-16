package customers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.Scanner;
import util.LoginUtilities;
import common.Invoice;
import common.Package;

public class TextController {
	private CustomerController control;
	
	private enum MainCommand {
		ReviewPackage("Review Package"),
		ViewUsage("View Usage"),
		ViewInvoice("View Invoice"),
		SchedualeService("Scheduale Service Request"),
		ChangePassword("Change Password"),
		Exit("Exit");
		
		String text;
		
		MainCommand(String text) {
			this.text = text;
		}
		
		public String getText() {
			return text;
		}
	}
	
	private enum PackageCommand {
		Upgrade("Upgrade"),
		Exit("Exit");
		
		String text;
		
		PackageCommand(String text) {
			this.text = text;
		}
		
		public String getText() {
			return text;
		}
	}
	
	private enum ViewCommand {
		Current("Current"),
		Previous("Previous"),
		Exit("Exit");
		
		String text;
		ViewCommand(String text) {
			this.text = text;
		}
		
		public String getText() {
			return text;
		}
	}
	
	private enum SchedualeCommand {
		Removal("Removal of Service request"),
		Upgrade("Upgrade request"),
		Support("Support request"),
		Exit("Cancel");
		
		String text;
		SchedualeCommand(String text) {
			this.text = text;
		}
		
		public String getText() {
			return text;
		}
	}
	
	public TextController(CustomerController control)
	{
		this.control = control;
	}
	
	public void login() throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
		Scanner keyboard = new Scanner(System.in);
		
		while (true)
		{
			System.out.print("Username: ");
			String username = keyboard.nextLine();
			System.out.print("Password: ");
			String password = keyboard.nextLine();
			
			if (control.tryLogin(username, password))
			{
				break;
			}
		}
		
		showMenu(keyboard);
	}
	
	public void showMenu(Scanner keyboard) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException
	{
		System.out.print("Welcome back ");
		control.getUserName();
		System.out.println("!");
		
		MainCommand[] commands = MainCommand.values();
		String menu = createMainMenu(commands);
		MainCommand choice;
		
		do
		{
			System.out.print(menu);
			choice = getUserChoice (commands, keyboard);
			
			switch (choice)
			{
			case ReviewPackage:
				ReviewPackage(keyboard);
				break;
			case ViewUsage:
				ViewUsage(keyboard);
				break;
			case ViewInvoice:
				ViewInvoice(keyboard);
				break;
			case SchedualeService:
				WriteRequest(keyboard);
				break;
			case ChangePassword:
				ChangePassword(keyboard);
				break;
			case Exit:
				System.out.println("\nProgram exit...");
			}
		}
		while (choice != MainCommand.Exit);
	}
	
	private void WriteRequest(Scanner keyboard) throws SQLException {
		SchedualeCommand[] commands = SchedualeCommand.values();
		String menu = createSchedualeMenu(commands);
		SchedualeCommand choice;
				
		System.out.print(menu);
		choice = getSchedualeChoice(commands, keyboard);
		String request = null;
		
		switch (choice)
		{
		case Removal:
			request = "1001";
			break;
		case Upgrade:
			request = "1002";
			break;
		case Support:
			request = "1003";
			break;
		case Exit:
			System.out.println("Cancelling Request");
			return;
		}
		String description = getUserString("Describe your request:", ".+", "Request cannot be blank", keyboard);
		control.addRequest(request, description);

		
	}
	
	private SchedualeCommand getSchedualeChoice(SchedualeCommand[] commands, Scanner keyboard) {
		int maxChoiceValue = commands.length;
		int userChoice = 0;
		while (true)
		{
			try
			{
				userChoice = keyboard.nextInt();
				if (userChoice <= 0 || userChoice > maxChoiceValue)
				{
					System.out.print("Invalid choice! Enter a number between 1 and "
							+ maxChoiceValue + ": ");
				}
				else {
					break;
				}
			}
			catch (java.util.InputMismatchException e)
			{
				System.out.println("Invalid choice! Enter a number between 1 and "
						+ maxChoiceValue + ": ");
				keyboard.nextLine();
			}
		}
		
		keyboard.nextLine();
		
		return commands[userChoice - 1];
	}

	private void ViewUsage(Scanner keyboard) {
		ViewCommand[] viewCom = ViewCommand.values();
		String menu = createViewMenu(viewCom);
		ViewCommand choice;
		do
		{
			System.out.print(menu);
			choice = getUserChoice(viewCom, keyboard);
			
			switch (choice)
			{
			case Current:
				control.ViewUsage();
				break;
			case Previous:
				ViewPreviousUsage(keyboard);
				break;
			case Exit:
				break;
			}
		}
		while (choice != ViewCommand.Exit);
	}
	
	
	private void ViewInvoice(Scanner keyboard) {
		ViewCommand[] viewCom = ViewCommand.values();
		String menu = createViewMenu(viewCom);
		ViewCommand choice;
		do
		{
			System.out.print(menu);
			choice = getUserChoice(viewCom, keyboard);
			
			switch (choice)
			{
			case Current:
				control.ViewInvoice();
				break;
			case Previous:
				ViewPreviousInvoice(keyboard);
				break;
			case Exit:
				
				break;
			}
		}
		while (choice != ViewCommand.Exit);
		
	}

	public void ViewPreviousInvoice(Scanner keyboard)
	{
		Invoice[] voices = control.getAllInvoice();
		
		if(voices != null) {
			String menu = showPreviousOptions(voices);
			System.out.print(menu);
			int spec_voice = getViewChoice(voices, keyboard);
			if (spec_voice != -1)
				control.ViewInvoice(spec_voice);
		} else {
			System.out.println("\nNo previous invocies!");
		}
		
	}
	
	public void ViewPreviousUsage(Scanner keyboard)
	{
		Invoice[] voices = control.getAllInvoice();
		String menu = showPreviousOptions(voices);
		System.out.print(menu);
		int spec_id = getViewChoice(voices, keyboard);
		control.ViewUsage(spec_id);
	}
	
	private void ChangePassword(Scanner keyboard) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException
	{
		System.out.println("Put in your new password!");
		String newPass = getUserString("New Password: ","^.{6,}$","The password should be at least 6 character", keyboard);
		
		System.out.println("Confirm your new password!");
		String confirmPass = getUserString("Repeat Password: ", ".+","Please repeat password", keyboard);
		if (newPass.equals(confirmPass))
		{
			control.changePassword(newPass);
		}
		else
		{
			System.out.println();
			System.out.println("Passwords do not match!");
		}
	}
	
	private void ReviewPackage(Scanner keyboard) throws SQLException
	{
		double price = control.getCurrentPackage();
		PackageCommand[] commands = PackageCommand.values();
		String menu = createPackageMenu(commands);
		PackageCommand choice;
		System.out.print(menu);
		choice = getUserChoice (commands,keyboard);
		if (choice == PackageCommand.Upgrade)
		{
			UpgradePackage(keyboard, price);
		}
	}
	
	private void UpgradePackage(Scanner keyboard, double price) throws SQLException
	{
		Package[] packs = control.getUpgrades(price);
		if (packs != null)
		{
			int number;
			number = getUpgradeChoice(packs, keyboard);
			if (number != -1)
			{
				control.sendUpgrade(number);
			}
		}
		else
		{
			System.out.println("There are no possible upgrades for you!");
		}
	}
	
	private MainCommand getUserChoice (MainCommand[] commands, Scanner keyboard)
	{
		int maxChoiceValue = commands.length;
		int userChoice = 0;
		while (true)
		{
			try
			{
				userChoice = keyboard.nextInt();
				if (userChoice <= 0 || userChoice > maxChoiceValue)
				{
					System.out.print("Invalid choice! Enter a number between 1 and "
							+ maxChoiceValue + ": ");
				}
				else {
					break;
				}
			}
			catch (java.util.InputMismatchException e)
			{
				System.out.println("Invalid choice! Enter a number between 1 and "
						+ maxChoiceValue + ": ");
				keyboard.nextLine();
			}
		}
		
		keyboard.nextLine();
		
		return commands[userChoice - 1];
	}
	
	private PackageCommand getUserChoice (PackageCommand[] commands, Scanner keyboard)
	{
		int maxChoiceValue = commands.length;
		int userChoice = 0;
		while (true)
		{
			try
			{
				userChoice = keyboard.nextInt();
				if (userChoice <= 0 || userChoice > maxChoiceValue)
				{
					System.out.print("Invalid choice! Enter a number between 1 and "
							+ maxChoiceValue + ": ");
				}
				else {
					break;
				}
			}
			catch (java.util.InputMismatchException e)
			{
				System.out.println("Invalid choice! Enter a number between 1 and "
						+ maxChoiceValue + ": ");
				keyboard.nextLine();
			}
		}
		
		keyboard.nextLine();
		
		return commands[userChoice - 1];
	}
	
	private ViewCommand getUserChoice (ViewCommand[] commands, Scanner keyboard)
	{
		int maxChoiceValue = commands.length;
		int userChoice = 0;
		while (true)
		{
			try
			{
				userChoice = keyboard.nextInt();
				if (userChoice <= 0 || userChoice > maxChoiceValue)
				{
					System.out.print("Invalid choice! Enter a number between 1 and "
							+ maxChoiceValue + ": ");
				}
				else {
					break;
				}
			}
			catch (java.util.InputMismatchException e)
			{
				System.out.println("Invalid choice! Enter a number between 1 and "
						+ maxChoiceValue + ": ");
				keyboard.nextLine();
			}
		}
		
		keyboard.nextLine();
		
		return commands[userChoice - 1];
	}
	
	private int getUpgradeChoice (Package[] packs, Scanner keyboard)
	{
		int userChoice = -1;
		boolean check = true;
		while (check)
		{
			try
			{
				System.out.print("Enter an id number or -1 to exit:");
				userChoice = keyboard.nextInt();
				if (userChoice == -1)
				{
					break;
				}
				for (int i = 0; i < packs.length; i++)
				{
					if (userChoice == Integer.parseInt(packs[i].getPackageId()))
					{
						check = false;
						break;
					}
					else if ((i + 1) == packs.length)
					{
						System.out.println("Invalid choice! Enter a number that is within the list.");
					}
				}
			}
			catch (java.util.InputMismatchException e)
			{
				System.out.println("Invalid choice! Enter a number that is within the list.");
				keyboard.nextLine();
			}
		}
		
		keyboard.nextLine();
		
		return userChoice;
	}
	
	private int getViewChoice (Invoice[] packs, Scanner keyboard)
	{
		int userChoice = -1;
		System.out.println();
		boolean check = true;
		while (check)
		{
			try
			{
				System.out.print("Enter an id number or -1 to exit:");
				userChoice = keyboard.nextInt();
				if (userChoice == -1)
				{
					break;
				}
				for (int i = 0; i < packs.length; i++)
				{
					if (userChoice == Integer.parseInt(packs[i].getInvoiceId()))
					{
						check = false;
						break;
					}
					else if ((i + 1) == packs.length)
					{
						System.out.println("Invalid choice! Enter a number that is within the list.");
					}
				}
				
			}
			catch (java.util.InputMismatchException e)
			{
				System.out.println("Invalid choice! Enter a number that is within the list.");
				keyboard.nextLine();
			}
		}
		
		keyboard.nextLine();
		
		return userChoice;
	}
	
	private String createMainMenu(MainCommand[] commands)
	{
		String menu = "\nSelect a choice from the menu:\n";
		int numChoices = commands.length;
		for (int i = 0; i < numChoices ; i ++)
			menu += "\t" + (i+1) + " - " + commands[i].getText() + "\n";
		menu += "\nEnter your choice: ";
		
		return menu;	
	}
	
	private String createSchedualeMenu(SchedualeCommand[] commands)
	{
		String menu = "\nSelect a choice from the menu:\n";
		int numChoices = commands.length;
		for (int i = 0; i < numChoices ; i ++)
			menu += "\t" + (i+1) + " - " + commands[i].getText() + "\n";
		menu += "\nEnter your choice: ";
		
		return menu;	
	}
	
	private String createPackageMenu(PackageCommand[] commands)
	{
		String menu = "\nSelect a choice from the menu:\n";
		int numChoices = commands.length;
		for (int i = 0; i < numChoices ; i ++)
			menu += "\t" + (i+1) + " - " + commands[i].getText() + "\n";
		menu += "\nEnter your choice: ";
		
		return menu;	
	}
	
	private String createViewMenu(ViewCommand[] commands)
	{
		String menu = "\nSelect a choice from the menu:\n";
		int numChoices = commands.length;
		for (int i = 0; i < numChoices ; i ++)
			menu += "\t" + (i+1) + " - " + commands[i].getText() + "\n";
		menu += "\nEnter your choice: ";
		
		return menu;	
	}
	
	private String showPreviousOptions(Invoice[] voices)
	{
		String text = "\nPrevious Invoices:\n";
		for (int i = 1; i < voices.length; i++)
		{
			text += "\n" + voices[i].getInvoiceId() + " - Start Date: " + voices[i].getStartInvoicingPeriod() + " - End Date: " + voices[i].getEndInvoicingPeriod();
		}
		text += "\n";
		return text;
	}
	private String getUserString(String message, String regex, String errorMsg, Scanner keyboard) {
		String input = null;
		while (true)
		{
			try
			{
				System.out.print(message);
				input = keyboard.nextLine();
				input = input.trim();
				if (input.matches(regex))
				{
					break;
				}
				else {
					System.out.println(errorMsg);
				}
			}
			catch (java.util.InputMismatchException e) 
			{
				System.out.println(errorMsg);
			}
		}
		
		return input;
	}	
}
