package common;

import java.sql.*;

public class Customer {
	private String username;
	private String name;
	private String address;
	private String phoneNumber;
	private String emailAddress;
	private String packageId;
	
	private Package inernetPackage;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public Package getInernetPackage() {
		return inernetPackage;
	}

	public void setInernetPackage(Package inernetPackage) {
		this.inernetPackage = inernetPackage;
	}
	
}
