package util;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginUtilities {
	public static String getSalt() {
		SecureRandom random = new SecureRandom();
		String salt = new BigInteger(130, random).toString(32);
		
		return salt;
	}
	
	public static byte[] hash(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String hash_algorith = "PBEWithSHA1AndDESede";
		PBEKeySpec spec = new PBEKeySpec((password+salt).toCharArray());
		SecretKeyFactory skf = SecretKeyFactory.getInstance(hash_algorith);
		byte[] hash = skf.generateSecret(spec).getEncoded();
		
		return hash;
	}
}
