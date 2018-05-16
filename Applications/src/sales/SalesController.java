package sales;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import common.*;
import common.Package;
import oracle.jdbc.*;
import util.*;

/**
 * The SalesController is heart of the application
 * It is almost entirely used to communicate with database, but it may
 * also contain other methods that are needed for the Sales application
 * 
 * To clarify this class contains methods used to login to application,
 * register customers, modify customer's package, etc.
 * 
 * The methods of this class are called from the TextController.
 * Also when the methods are called, almost always they should receive
 * database objects (ex: if you want to register a new customer, the 
 * customer object is already created before calling the SalesController 
 * method and then the customer object is passed to this SalesController method so 
 * it will recreate this customer object inside the database)
 * 
 * @author Yanik
 *
 */
public class SalesController {
	private TextView textView;
	
	private String db_hostname;
	private String db_username;
	private String db_password;
	
	private String emp_username;

	
	public SalesController(TextView textView) throws IOException, SQLException {
		this.textView = textView;
		
		String[] dbLogin = getLines("db_login.txt");
		db_hostname = dbLogin[0];
		db_username = dbLogin[1];
		db_password = dbLogin[2];
	}
	
	public void getAllPackages()  {
		Connection connection = null;
		try {
			connection = getConnection();
			
			PreparedStatement getPackages = connection.prepareStatement("SELECT * FROM internet_packages");
			ResultSet result = getPackages.executeQuery();
			
			List<Package> packagesList = new ArrayList<Package>();
			while (result.next()) {
				Package netPackage = new Package();
				netPackage.setPackageId(result.getString(1));
				netPackage.setName(result.getString(2));
				netPackage.setDescription(result.getString(3));
				netPackage.setCost(result.getDouble(4));
				netPackage.setUp(result.getDouble(5));
				netPackage.setDown(result.getDouble(6));
				netPackage.setBandwidth(result.getDouble(7));
				netPackage.setOverage(result.getDouble(8));
				
				packagesList.add(netPackage);
			}
			
			Package[] packages = new Package[packagesList.size()];
			packages = packagesList.toArray(packages);
			
			textView.displayPackages(packages);
			
		} catch (SQLException e) {
			textView.displayMessage("Application Error! Couldn't display packages");
		} finally {
			closeConnection(connection);
		}
	}
	
	public boolean registerCustomer(Customer customer) {
		boolean isCustomerRegistered = false;
		Connection connection = null;
		try {
			connection = getConnection();
			
			PreparedStatement register = connection.prepareStatement("INSERT INTO customers VALUES"
					+ " (?, ?, ?, ?, ?, ?)");
			
			register.setString(1, customer.getUsername());
			register.setString(2, customer.getName());
			register.setString(3, customer.getAddress());
			register.setString(4, customer.getPhoneNumber());
			register.setString(5, customer.getEmailAddress());
			register.setString(6, customer.getPackageId());
			
			register.executeUpdate();
			
			textView.displayMessage("Customer registered successfully!");
			
			isCustomerRegistered = true;
		} catch (SQLException e) {
			// Get the vendor specific code
			int code = e.getErrorCode(); 
			
			// Integrity constraint violation (foreign key)
			if (code == 2291) {
				textView.displayMessage("Couldn't register customer. Package with ID " 
						+ customer.getPackageId() + " doesn't exist");
			} 
			// Unique constraint violation (username in this case)
			else if (code == 1) {
				textView.displayMessage("Couldn't register customer. Customer with username already exists: "
						+ customer.getUsername());
			}
			else {
				textView.displayMessage("Application Error! Couldn't register customer");
			}
		} finally {
			closeConnection(connection);
		}
		
		return isCustomerRegistered;
	}
	
	public boolean getPossibleVisitHours(Date date) {
		boolean areVisitsPossible = false;
		Connection connection = null;
		try {
			connection = getConnection();
			
			CallableStatement call = connection.prepareCall("{? = call sales.get_possible_visits(?)}");
			
			call.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
			call.setDate(2, date);
			
			call.execute();
			
			Array outArray = call.getArray(1);
			// NUMBER in Oracle is mapped to BigDecimal in java
			BigDecimal [] bigHours = (BigDecimal [])outArray.getArray();
			
			// Convert BigDecimal array to int[]
			int[] hours = new int[bigHours.length];
			for (int i = 0; i < hours.length; i++) {
				hours[i] = bigHours[i].intValue();
			}
			
			textView.displayPossibleHours(hours);
			
			areVisitsPossible = true;
			
		} catch (SQLException e) {
			// Get the vendor specific code
			int code = e.getErrorCode(); 
			
			// no_available_hours exception
			if (code == 20002) {
				textView.displayMessage("No free hours on this date");
			} else {
				textView.displayMessage("Couldn't find free hours on this date. Date is probably invalid");
			}
		} finally {
			closeConnection(connection);
		}
		
		return areVisitsPossible ;
	}
	
	public boolean scheduleVisit(Date date, int hour, Customer customer) {
		boolean isVisitScheduled = false;
		Connection connection = null;
		try {
			connection = getConnection();
			
			CallableStatement call = connection.prepareCall("{call sales.register_visit(?, ?, ?)}");
			
			call.setDate(1, date);
			call.setBigDecimal(2, new BigDecimal(hour));
			call.setString(3, customer.getUsername());
			
			call.execute();
			
			textView.displayMessage("Visit scheduled successfully!");
			
			isVisitScheduled = true;
		
		} catch (SQLException e) {
			// Get the vendor specific code
			int code = e.getErrorCode(); 
			
			// hour_not_available exception
			if (code == 20001) {
				textView.displayMessage("Hour not available");
			} else {
				textView.displayMessage("Application Error! Couldn't schedule a visit");
			}
		} finally {
			closeConnection(connection);
		}
		
		return isVisitScheduled;
	}
	
	public boolean searchCustomer(String username) {
		boolean isCustomerFound = false;
		Connection connection = null;
		try {
			connection = getConnection();
			
			CallableStatement call = connection.prepareCall("{? = call sales.search_customer(?)}");
			
			call.registerOutParameter(1, Types.CHAR);
			call.setString(2, username);
			
			call.execute();
			
			String isFound = call.getString(1);
			
			if (isFound.equals("1")) {
				textView.displayMessage("Customer exists");
				isCustomerFound = true;
			} else {
				textView.displayMessage("Customer doesn't exist");
			}
		} catch (SQLException e) {
			textView.displayMessage("Application Error! Couldn't find customer");
		} finally {
			closeConnection(connection);
		}
		
		return isCustomerFound;
	}
	
	public boolean modifyUserPackage(String username, String packageId) {
		boolean isPackageModified = false;
		Connection connection = null;
		try {
			connection = getConnection();
			
			CallableStatement call = connection.prepareCall("{call sales.modify_user_package(?, ?)}");
			
			call.setString(1, username);
			call.setString(2, packageId);
			
			call.execute();
			
			textView.displayMessage("Package updated successfully!");
			
			isPackageModified = true;
		} catch (SQLException e) {
			// Get the vendor specific code
			int code = e.getErrorCode(); 
			
			// Integrity constraint violation (foreign key)
			if (code == 2291) {
				textView.displayMessage("Couldn't update package. Package with ID " 
						+ packageId + " doesn't exist");
			}
			else {
				textView.displayMessage("Application Error! Couldn't update package");
			}	
		} finally {
			closeConnection(connection);
		}
		
		return isPackageModified;
	}
	
	public boolean tryLogin(String username, String password) {
		boolean isLoginSuccessful = false;
		Connection connection = null;
		try {
			connection = getConnection();
			
			PreparedStatement getUserStatement = connection.prepareStatement("SELECT * FROM sales_login WHERE username = ?");
			getUserStatement.setString(1, username);
			ResultSet user = getUserStatement.executeQuery();
			
			if (user.next()) {
				String salt = user.getString(2);
				byte[] hash = user.getBytes(3);
				
				byte[] computedHash = LoginUtilities.hash(password, salt);
				
				isLoginSuccessful = Arrays.equals(hash, computedHash);
			}
			
			if (!isLoginSuccessful) {
				textView.displayMessage("Username or password invalid");
			} else {
				textView.displayMessage("Successfully logged in");
				emp_username = username;
			}
		} catch (SQLException e) {
			textView.displayMessage("Application Error! Impossible to get login information");
		} catch (Exception e) {
			textView.displayMessage("Application Error! Login impossible");
		} finally {
			closeConnection(connection);
		}
		
		return isLoginSuccessful;
	}
	
	public void generateLocalNumber() {
		Random random = new Random();
		String[] areaCodes = {"514", "450", "438"};
		
		String randomAreaCode = areaCodes[random.nextInt(areaCodes.length)];
		int middleThreeDigits = random.nextInt(1000);
		int lastSevenDigits = random.nextInt(10000);
		
		String fullPhoneNumber = String.format("%s-%03d-%04d", randomAreaCode, middleThreeDigits, lastSevenDigits);
		
		textView.displayMessage(fullPhoneNumber);
	}
	
	public boolean changeSalesPassword(String password) {
		boolean isSuccessul = false;
		Connection connection = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			
			String salt = LoginUtilities.getSalt();
			byte[] hash = LoginUtilities.hash(password, salt);
			
			PreparedStatement changeHash = connection.prepareStatement("UPDATE sales_login "
					+ "SET hash = ? WHERE username = ?");
			
			changeHash.setBytes(1, hash);
			changeHash.setString(2, emp_username);
			
			PreparedStatement changeSalt = connection.prepareStatement("UPDATE sales_login " 
					+ "SET salt = ? WHERE username = ?");
			
			changeSalt.setString(1, salt);
			changeSalt.setString(2, emp_username);
	
			changeHash.executeUpdate();
			changeSalt.executeUpdate();
			
			connection.commit();
			
			textView.displayMessage("Password changed");

			isSuccessul = true;
		} catch (SQLException e) {
			if (connection != null)
				rollbackConnection(connection);
			textView.displayMessage("Application Error! Couldn't change password");
			return false;
		} catch (Exception e) {
			if (connection != null)
				rollbackConnection(connection);
			textView.displayMessage("Application Error! Impossible to change password");
		} finally {
			closeConnection(connection);
		}
		
		return isSuccessul;
	}
	
	public boolean recordSalesCall(String customerUsername) {
		boolean isRecorded = false;
		Connection connection = null;
		try {
			connection = getConnection();
			
			CallableStatement call = connection.prepareCall("{call sales.record_sales_call(?, ?)}");
			
			call.setString(1, customerUsername);
			call.setString(2, emp_username);
			
			call.execute();

			textView.displayMessage("Sales call recorded successfully!");
			
			isRecorded = true;
		} catch (SQLException e) {
			textView.displayMessage("Application Error! Couldn't record sales call");
		} finally {
			closeConnection(connection);
		}
		
		return isRecorded;
	}
	
	public void getEmployeeName() {
		Connection connection = null;
		try {
			connection = getConnection();
			
			PreparedStatement getEmpName = connection.prepareStatement("SELECT name FROM employees " + 
					"JOIN sales_login USING(\"number\") " + 
					"WHERE username = ?");
			
			getEmpName.setString(1, emp_username);
			
			ResultSet results = getEmpName.executeQuery();
			
			if (results.next()) {
				textView.displayMessageOnLine(results.getString(1));
			} else {
				textView.displayMessage("Application Error! Couldn't display employee name");
			}
		} catch (SQLException e) {
			textView.displayMessage("Application Error! Couldn't display employee name");
		} finally {
			closeConnection(connection);
		}
	}
	
	// Helper method to close connection 
	private void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				textView.displayMessage("Application Error! Coulnd't close connection");
			}
		}
	}
	
	// Helper method to rollback connection 
	private void rollbackConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				textView.displayMessage("Application Error! Couldn't rollback connection");
			}
		}
	}
	
	// Helper method to get a connection
	private Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_hostname, db_username, db_password);
		} catch (SQLException e) {
			textView.displayMessage("Application Error! Couldn't get connection");
		}
		
		return connection;
	}
	
	// Helper method to read lines from file
	private static String[] getLines(String filename) throws IOException {
		Path file = Paths.get(filename);

		try {
			List<String> linesList = Files.readAllLines(file, Charset.forName("UTF-8"));

			String[] lines = new String[linesList.size()];
			lines = linesList.toArray(lines);

			return lines;
		} catch (IOException ioe) {
			throw new IOException("Application Error! Cannot read file: " + filename);
		}
	}
	
	// Provide access to methods to manipulate DB
}
