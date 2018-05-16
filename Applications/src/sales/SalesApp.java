package sales;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

/**
 * 
 * @author Yanik
 *
 */

public class SalesApp {

	public static void main(String[] args) throws IOException, SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
		TextView textView = new TextView();
		SalesController salesController = new SalesController(textView);
		TextController textController = new TextController(salesController);
		
		textController.login();
	}

}
