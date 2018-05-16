package technicians;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

public class TechniciansApp {

	public static void main(String[] args) throws IOException, SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
		// Create TextView
		// Create Model (Pass TextView, so model will be able to call TextView's methods to display 
		// Create TextController (Pass model to be able to call model's methods)
				
		// Run controller that will display possible controls (menu)
		TextView textView = new TextView();
		TechniciansController techniciansController = new TechniciansController(textView);
		TextController textController = new TextController(techniciansController);
		
		textController.login();
	}

}
