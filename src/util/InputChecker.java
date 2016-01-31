package util;

import java.util.regex.Pattern;

// Klasse zur �berpr�fung von Email Eingaben

public class InputChecker {

	// regex Pattern f�r Emails
	private static final Pattern pattern = Pattern
			.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");

	// Methode, um Test durchzuf�hren
	public static boolean checkEMailAddress(String address) {
		return pattern.matcher(address).matches();
	}

	// main zum Testen
	public static void main(String[] args) {
		String address = "daniel.nerbas@googlemail.com";
		System.out.println("E-Mail Adresse " + address + " ist "
				+ (checkEMailAddress(address) ? "valide" : "nicht valide"));
	}
}
