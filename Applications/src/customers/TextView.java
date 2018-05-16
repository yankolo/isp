package customers;
import java.sql.Date;

import common.*;
import common.Package;

public class TextView {

	public void displayMessage(String string) {
		System.out.println("\n" + string + "\n");
	}
	
	public void displayMessageOnLine(String string) {
		System.out.print(string);
	}
	
	public void displayPackages(Package[] packages)
	{
		System.out.println();
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
	
	public void displayUsage(Usage[] use)
	{
		System.out.println();
		System.out.println("-------------------------------------------------------");
		
		for (int i = 0; i < use.length; i++)
		{
			System.out.printf("Date: %s%n", use[i].getDay());
			System.out.printf("Download: %.2fGB%n", use[i].getDown());
			System.out.printf("Upload: %.2fGB%n", use[i].getUp());
			System.out.println("-------------------------------------------------------");
		}
		
		System.out.println();

	}
	
	public void displayInvoice(Invoice voice)
	{
		System.out.println();
		System.out.println("-------------------------------------------------------");
		System.out.printf("CostPlan: %.2f$%n", voice.getCostPlan());
		System.out.printf("Overage: %.2f$%n", voice.getOverage());
		System.out.printf("OtherServices: %.2f$%n", voice.getOtherServices());
		System.out.printf("GST: %.2f$%n", voice.getGst());
		System.out.printf("QST: %.2f$%n", voice.getQst());
		System.out.printf("Previous Balance: %.2f$%n", voice.getPreviousBalance());
		System.out.printf("New Balance: %.2f$%n", voice.getNewBalance());
		System.out.printf("Total Cost: %.2f$%n", voice.getTotal());
		System.out.printf("DueDate: %s%n", voice.getDueDate());
		System.out.printf("StartPeriod: %s%n", voice.getStartInvoicingPeriod());
		System.out.printf("EndPeriod: %s%n", voice.getEndInvoicingPeriod());
		System.out.println("-------------------------------------------------------");
		
		System.out.println();
	}
}
