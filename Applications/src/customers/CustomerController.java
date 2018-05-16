package customers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import common.Invoice;
import common.Package;
import common.Usage;
import util.LoginUtilities;

public class CustomerController {
	private TextView text;
	
	private String hostName;
	private String userName;
	private String passWord;
	
	private static String cust_username;
	
	public CustomerController(TextView text) throws IOException {
		this.text = text;
		
		String[] signIn = getLines("db_login.txt");
		
		hostName = signIn[0];
		userName = signIn[1];
		passWord = signIn[2];
	}
	
	public static String[] getLines(String fileName) throws IOException {
		try {
			BufferedReader files = new BufferedReader(new FileReader(fileName));
			String line;
			List<String> linesList = new ArrayList<String>();
			while ((line = files.readLine()) != null)
			{
				linesList.add(line);
			}
			files.close();
			String[] lines = new String[linesList.size()];
			lines = linesList.toArray(lines);
			
			return lines;
		}
		catch (IOException ioe) {
			throw new IOException("Application Error! Cannot read file: " + fileName);
		}
	}

	public boolean tryLogin(String user, String pass) {
		Connection conn = null;
		try
		{
			conn = getConnection();
			CallableStatement saltGetter = conn.prepareCall("{? = call CUSTOMER_PACKAGE.getSalt(?)}");
			saltGetter.registerOutParameter(1, Types.CHAR);
			saltGetter.setString(2, user);
			saltGetter.execute();
			if (saltGetter.getString(1) != null)
			{
				byte[] hashCode = LoginUtilities.hash(pass, saltGetter.getString(1));
				CallableStatement cstmt = conn.prepareCall("{? = call CUSTOMER_PACKAGE.checkIfCustomer(?,?)}");
				cstmt.registerOutParameter(1, Types.VARCHAR);
				cstmt.setString(2, user);
				cstmt.setBytes(3, hashCode);
				cstmt.execute();
				if (cstmt.getString(1) != null)
				{
					text.displayMessage("Successfully logged in!");
					cust_username = user;
					return true;
				}
				else
				{
					text.displayMessage("Username or password invalid!");
				}
			}
			else
			{
				text.displayMessage("Username does not exist!");
			}
		}
		catch (SQLException e)
		{
			text.displayMessage("Application Error! Impossible to get login information!");
		}
		catch (Exception e)
		{
			text.displayMessage("Application Error! Login impossible!");
		}
		finally
		{
			closeConnection(conn);
		}
		return false;
	}
	
	private void closeConnection(Connection connection) {
		if (connection !=  null)
		{
			try {
				connection.close();
			}
			catch (SQLException e) {
				text.displayMessage("Application Error! Couldn't close connection!");
			}
		}
	}
	
	public void getUserName() {
		text.displayMessageOnLine(cust_username);
	}
	
	private Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(hostName,userName,passWord);
		}
		catch (SQLException e) {
			text.displayMessage("Application Error! Couldn't get connection!");
		}
		connection.setAutoCommit(false);
		return connection;
	}

	public double getCurrentPackage() throws SQLException {
		Connection conn = null;
		double price = -1;
		try {
			conn = getConnection();
			Package[] item = new Package[1];
			item[0] = new Package();
			Statement stmt = conn.createStatement();
			String query = "SELECT i.package_id AS package_id, i.name AS name, i.description AS description, i.cost AS cost, i.up AS up, i.down AS down, i.bandwidth AS bandwidth, i.overage AS overage "
					+ "FROM Customers c JOIN Internet_Packages i ON c.package_id = i.package_id WHERE c.username = '" + cust_username + "'";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				item[0].setPackageId(rs.getString("package_id"));
				item[0].setName(rs.getString("name"));
				item[0].setDescription(rs.getString("description"));
				price = rs.getDouble("cost");
				item[0].setCost(price);
				item[0].setBandwidth(rs.getDouble("bandwidth"));
				item[0].setDown(rs.getDouble("down"));
				item[0].setUp(rs.getDouble("up"));
				item[0].setOverage(rs.getDouble("overage"));
			}
			text.displayPackages(item);
			stmt.close();
		}
		catch (SQLException e)
		{
			text.displayMessage("There appears to be no current package for you!");
		}
		finally
		{
			closeConnection(conn);
		}
		return price;
	}
	
	public Package[] getUpgrades(double price) {
		Connection conn = null;
		List<Package> items = new ArrayList<Package>();
		Package[] array = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM Internet_Packages WHERE cost > " + price;
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Package item = new Package();
				item.setPackageId(rs.getString("package_id"));
				item.setName(rs.getString("name"));
				item.setDescription(rs.getString("description"));
				item.setCost(rs.getDouble("cost"));
				item.setCost(price);
				item.setBandwidth(rs.getDouble("bandwidth"));
				item.setDown(rs.getDouble("down"));
				item.setUp(rs.getDouble("up"));
				item.setOverage(rs.getDouble("overage"));
				items.add(item);
			}
			stmt.close();
		}
		catch (SQLException e)
		{
			text.displayMessage("Application Error! Possible upgrades are impossible to get!");
		}
		finally
		{
			closeConnection(conn);
		}
		if (items.size() != 0)
		{
			array = new Package[items.size()];
			array = items.toArray(array);
			
			text.displayPackages(array);
		}
		return array;
	}
	
	public void sendUpgrade(int number) throws SQLException 
	{
		Connection conn = null;
		try
		{
			conn = getConnection();
			CallableStatement cstmt = conn.prepareCall("{CALL CUSTOMER_PACKAGE.upgradeing (?,?)}");
			cstmt.setString(1, cust_username);
			cstmt.setString(2, Integer.toString(number));
			cstmt.execute();
			conn.commit();
		}
		catch (SQLException e)
		{
			conn.rollback();
		}
		finally
		{
			closeConnection(conn);
		}
	}
	
	public void changePassword(String pass) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
		Connection conn = null;
		try
		{
			conn = getConnection();
			String salt = LoginUtilities.getSalt();
			CallableStatement cstmt = conn.prepareCall("{call CUSTOMER_PACKAGE.changePassword(?,?,?)}");
			cstmt.setString(1, cust_username);
			cstmt.setString(2, salt);
			cstmt.setBytes(3, LoginUtilities.hash(pass, salt));
			cstmt.executeUpdate();
			conn.commit();
		}
		catch (SQLException e) 
		{
			conn.rollback();
		}
		catch (NoSuchAlgorithmException e)
		{
			conn.rollback();
		}
		catch (InvalidKeySpecException e)
		{
			conn.rollback();
		}
		finally
		{
			closeConnection(conn);
		}
		
	}

	public void ViewInvoice() {
		Invoice[] words = getAllInvoice();
		if (words != null)
			text.displayInvoice(words[0]);
		else 
			text.displayMessage("No current invoice!");
	}
	
	public void ViewUsage() {
		Invoice[] word = getAllInvoice();
		Usage[] use = getUsage(word[0].getStartInvoicingPeriod(),word[0].getEndInvoicingPeriod());
		if (use != null)
			text.displayUsage(use);
		else
			text.displayMessage("No current usage!");
	}
	
	public void ViewUsage(int spec_id)
	{
		Invoice voice = getSpecificInvoice(spec_id);
		if (spec_id != -1) {
			Usage[] use = getUsage(voice.getStartInvoicingPeriod(),voice.getEndInvoicingPeriod());
			if (use != null)
				text.displayUsage(use);
			else 
				text.displayMessage("No usage for this invoice!");
		}
	}
	
	public Invoice getSpecificInvoice(int spec_voice) {
		Connection conn = null;
		Invoice item = new Invoice();
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM Invoice WHERE username = '" + cust_username + "' AND invoice_id = '" + Integer.toString(spec_voice) + "'";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				item.setInvoiceId(rs.getString("INVOICE_ID"));
				item.setCustomerName(rs.getString("USERNAME"));
				item.setCostPlan(rs.getDouble("COST_PLAN"));
				item.setOtherServices(rs.getDouble("OTHER_SERVICES"));
				item.setOverage(rs.getDouble("OVERAGE"));
				item.setGst(rs.getDouble("GST"));
				item.setQst(rs.getDouble("QST"));
				item.setPreviousBalance(rs.getDouble("PREVIOUS_BALANCE"));
				item.setNewBalance(rs.getDouble("NEW_BALANCE"));
				item.setTotal(rs.getDouble("TOTAL"));
				item.setDueDate(rs.getDate("DUE_DATE"));
				item.setStartInvoicingPeriod(rs.getDate("START_INVOICING_PERIOD"));
				item.setEndInvoicingPeriod(rs.getDate("END_INVOICING_PERIOD"));
			}
			stmt.close();
		}
		catch (SQLException e)
		{
			text.displayMessage("The specific invoice cannot be found!");
		}
		finally
		{
			closeConnection(conn);
		}
		return item;
	}
	
	public Usage[] getUsage(Date start, Date end)
	{
		Connection conn = null;
		List<Usage> items = new ArrayList<Usage>();
		Usage[] array = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			SimpleDateFormat data = new SimpleDateFormat("dd-MMM-yy");
			String query = "SELECT * FROM Usage WHERE username = '" + cust_username + "' AND day >= '" + data.format(start) + "' AND day <= '" + data.format(end) + "'";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Usage item = new Usage();
				item.setCustomer(rs.getString("USERNAME"));
				item.setDay(rs.getDate("DAY"));
				item.setDown(rs.getDouble("DOWN"));
				item.setUp(rs.getDouble("UP"));
				items.add(item);
			}
			stmt.close();
		}
		catch (SQLException e)
		{
			text.displayMessage("Application Error! Cannot find a usage!");
		}
		finally
		{
			closeConnection(conn);
		}
		if (items.size() != 0)
		{
			array = new Usage[items.size()];
			items.toArray(array);
		}
		return array;
	}
		
	public Invoice[] getAllInvoice() {
		Connection conn = null;
		List<Invoice> items = new ArrayList<Invoice>();
		Invoice[] array = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM INVOICE WHERE username = '" + cust_username + "' ORDER BY end_invoicing_period DESC";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Invoice item = new Invoice();
				item.setInvoiceId(rs.getString("INVOICE_ID"));
				item.setCustomerName(rs.getString("USERNAME"));
				item.setCostPlan(rs.getDouble("COST_PLAN"));
				item.setOtherServices(rs.getDouble("OTHER_SERVICES"));
				item.setOverage(rs.getDouble("OVERAGE"));
				item.setGst(rs.getDouble("GST"));
				item.setQst(rs.getDouble("QST"));
				item.setPreviousBalance(rs.getDouble("PREVIOUS_BALANCE"));
				item.setNewBalance(rs.getDouble("NEW_BALANCE"));
				item.setTotal(rs.getDouble("TOTAL"));
				item.setDueDate(rs.getDate("DUE_DATE"));
				item.setStartInvoicingPeriod(rs.getDate("START_INVOICING_PERIOD"));
				item.setEndInvoicingPeriod(rs.getDate("END_INVOICING_PERIOD"));
				items.add(item);
			}
			stmt.close();
		}
		catch (SQLException e)
		{
			text.displayMessage("Application Error! No invoices can be found at the time!");
		}
		finally
		{
			closeConnection(conn);
		}
		if (items.size() != 0)
		{
			array = new Invoice[items.size()];
			items.toArray(array);
		}
		return array;
	}

	public void ViewInvoice(int spec_voice) {
		Invoice item = getSpecificInvoice(spec_voice);
		if (item != null)
			text.displayInvoice(item);
	}

	public void addRequest(String request, String description) throws SQLException {
		Connection conn = null;
		try
		{
			conn = getConnection();
			CallableStatement cstmt = conn.prepareCall("{call CUSTOMER_PACKAGE.addRequest(?,?,?)}");
			cstmt.setString(1, cust_username);
			cstmt.setString(2, request);
			cstmt.setString(3, description);
			cstmt.execute();
			conn.commit();
			
			text.displayMessage("Successfully requested service");
		}
		catch (SQLException e) 
		{
			// Get the vendor specific code
			int code = e.getErrorCode(); 
			
			// Integrity constraint violation (foreign key)
			if (code == 2291) {
				text.displayMessage("Application Error! Couldn't request the service. Service doesn't exist");
			} 
			else {
				text.displayMessage("Application Error! Couldn't request the service");
			}
			conn.rollback();
		}
		finally
		{
			closeConnection(conn);
		}
	}
}
