package customers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class CustomerApp {
	public static void main(String[] args) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, IOException
	{	
		TextView text = new TextView();
		CustomerController customerControl = new CustomerController(text);
		TextController textControl = new TextController(customerControl);
		textControl.login();
	}
}
