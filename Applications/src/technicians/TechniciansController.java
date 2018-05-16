package technicians;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.*;

import common.Visit;

public class TechniciansController {
	private TextView textView;
	
	public TechniciansController(TextView textView) throws IOException, SQLException {
		this.textView = textView;
		
		
	}
	
	private Connection getConnection() throws IOException, SQLException {
		String[] dbLogin = getLines("db_login.txt");
		return DriverManager.getConnection(dbLogin[0], dbLogin[1], dbLogin[2]);
	}
	
	public void displayAllEquipment(String employeeNumber) throws SQLException, IOException {
		Connection connection = getConnection();
		PreparedStatement getItems = connection.prepareStatement("SELECT name, amount FROM equipment_items WHERE (emp_id = ?)");
		PreparedStatement getTools = connection.prepareStatement("SELECT name, isFunctioning FROM equipment_tools WHERE (emp_id = ?)");
		PreparedStatement getOrders = connection.prepareStatement("SELECT name, amount FROM equipment_orders WHERE (emp_id = ?)");
		
		getItems.setString(1, employeeNumber);
		getTools.setString(1, employeeNumber);
		getOrders.setString(1, employeeNumber);
		
		ResultSet items = getItems.executeQuery();
		ResultSet tools = getTools.executeQuery();
		ResultSet orders = getOrders.executeQuery();
		
		textView.displayAllEquipment(items, tools, orders);		
	}
	
	public ArrayList<Visit> getListOfVisits(String employeeNumber, boolean onlyNew) throws SQLException, IOException 
	{
		Connection connection = getConnection();
		PreparedStatement getVisits = connection.prepareStatement("SELECT visit_id, username, \"start\", end FROM visits WHERE (technician_id = ?" 
														          + (onlyNew ? " AND end > SYSDATE)" : ")"));
		getVisits.setString(1, employeeNumber);
		ResultSet visits = getVisits.executeQuery();
		ArrayList<Visit> visitList = new ArrayList<Visit>();
		
		while (visits.next()) {
			Visit v = new Visit();
			v.setVisitId(visits.getString("visit_id"));
			v.setCustomerId(visits.getString("username"));
			v.setStart(visits.getTimestamp("start"));
			v.setEnd(visits.getTimestamp("end"));
			visitList.add(v);
		}
		
		textView.displayVisits(visitList);
		return visitList;
	}
	
	public void saveVisitNote(String visitId, String note) throws SQLException, IOException {
		Connection connection = getConnection();
		CallableStatement cstmt = connection.prepareCall("{CALL technicians_package.update_notes_visits(?, ?)}");
		cstmt.setString(1, visitId);
		cstmt.setString(2, note);
		cstmt.execute();
	}
	
	public void orderItems(String technicianId, int screws, int bolts, int fiberOptic) throws SQLException, IOException {
		Connection connection = getConnection();
		CallableStatement cstmt = connection.prepareCall("{CALL technicians_package.update_items(?, ?, ?, ?)}");
		cstmt.setString(1, technicianId);
		cstmt.setInt(2, screws);
		cstmt.setInt(3, bolts);
		cstmt.setInt(4, fiberOptic);
		cstmt.execute();
	}
	
	public String[] getFunctioningTools(String technicianId) throws SQLException, IOException 
	{
		Connection connection = getConnection();
		CallableStatement cstmt = connection.prepareCall("{? = call technicians_package.getFunctioningTools( ? )}");
		
		cstmt.setString(2, technicianId);
		cstmt.registerOutParameter(1, Types.LONGVARCHAR);
		
		cstmt.execute();
		
		String tools = cstmt.getString(1);
		
		if(tools == null) {
			tools = "";
		}
		return tools.split(",");		
	}
	
	public boolean changeTechniciansPassword(String password, String username) throws SQLException {
		boolean isSuccessul = false;
		Connection connection = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);
			
			String salt = LoginUtilities.getSalt();
			byte[] hash = LoginUtilities.hash(password, salt);
			
			PreparedStatement changeHash = connection.prepareStatement("UPDATE technicians_login "
					+ "SET hash = ? WHERE username = ?");
			
			changeHash.setBytes(1, hash);
			changeHash.setString(2, username);
			
			PreparedStatement changeSalt = connection.prepareStatement("UPDATE technicians_login " 
					+ "SET salt = ? WHERE username = ?");
			
			changeSalt.setString(1, salt);
			changeSalt.setString(2, username);
	
			changeHash.executeUpdate();
			changeSalt.executeUpdate();
			
			connection.commit();
			
			textView.displayMessage("Password changed");

			isSuccessul = true;
		} catch (SQLException e) {
			if (connection != null)
				connection.rollback();;
			textView.displayMessage("Application Error! Couldn't change password");
			return false;
		} catch (Exception e) {
			if (connection != null)
				connection.rollback();;
			textView.displayMessage("Application Error! Impossible to change password");
		} finally {
			connection.close();
		}
		
		return isSuccessul;
	}
	
	
	public void orderTools(String technicianId, boolean fusionSplicer, boolean drill) throws SQLException, IOException {
		Connection connection = getConnection();
		CallableStatement cstmt = connection.prepareCall("{call technicians_package.update_tools(?, ?, ?)}");
		cstmt.setString(1, technicianId);
		cstmt.setString(2, fusionSplicer ? "0" : "1");
		cstmt.setString(3, drill ? "0" : "1");
		cstmt.execute();
	}
	
	public String tryLogin(String username, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		boolean login = false;
		Connection connection = getConnection();
		PreparedStatement getUserStatement = connection.prepareStatement("SELECT * FROM technicians_login WHERE username = ?");
		getUserStatement.setString(1, username);
		ResultSet user = getUserStatement.executeQuery();
		if (user.next()) {
			String salt = user.getString(2);
			byte[] hash = user.getBytes(3);
			
			byte[] computedHash = LoginUtilities.hash(password, salt);
			
			login = Arrays.equals(hash, computedHash);
		}
		
		if (!login) {
			textView.displayMessage("Username or password invalid");
			return null;
		} else {
			textView.displayMessage("Successfully logged in");
			return user.getString(4);
		}
	}
	
	public void showInfo(String customerId) throws SQLException, IOException{
		Connection connection = getConnection();
		PreparedStatement getUserStatement = connection.prepareStatement("SELECT name, address, phone_number FROM customers WHERE username = ?");
		getUserStatement.setString(1, customerId);
		ResultSet user = getUserStatement.executeQuery();
		if(user.next()) {
			String name = user.getString("name");
			String address = user.getString("address");
			String phone = user.getString("phone_number");
			
			textView.displayUserToTech(name, address, phone);
		}
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
