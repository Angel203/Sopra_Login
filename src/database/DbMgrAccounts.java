package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import beans.User;

// Klasse zur Verwaltung von Datenbank Interaktionen bez�glich Accounts

public final class DbMgrAccounts {
	// URL muss auf das jeweilige System, auf dem der Server l�uft, angepasst werden
	public static final String URL = "jdbc:hsqldb:file:C:\\Users\\Stefan\\IdeaProjects\\Sopra_Login\\web\\db\\Sopra_Login";
	// Datenbank Treiber
	public static final String DRIVER = "org.hsqldb.jdbcDriver";

	// + Initialisierung
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private DbMgrAccounts() {
		// verhindert Initialisierung der Klasse
	}

	// Methode, zum �ffnen der Db-Connection
	public static Connection openConnection() throws SQLException {
		return DriverManager.getConnection(URL, "SA", "");
	}



	// Methode zum Pr�fen von Login Eingaben
	// existiert Account?
	public static boolean login(String username, String password) throws SQLException {
		boolean user = login_benutzer(username, password);
		if (!user)
		{
			boolean admin = login_admin(username, password);
			return admin;
		}
		return true;

	}

	public static boolean login_benutzer(String username, String password) throws SQLException {
		Connection conn = openConnection();
		// Suchen von Account mit genau dieser Username + Passwort Kombination
		String sql = "SELECT COUNT(*) AS c1 FROM Benutzer WHERE LCASE(email) = ? AND passwort = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, username.toLowerCase());
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();
		// Wenn count > 0, dann existiert mindestens ein Account zu dem diese Username + Passwort Kombi passt
		// da Usernames eindeutig kann count auch nicht > 1 sein.
		int count = 0;
		if (rs.next()) {
			count = rs.getInt("c1");
		}
		ps.close();
		rs.close();
		conn.close();
		if (count == 0) {
			// Account existiert so nicht => Login fehlgeschlagen
			return false;
		} else {
			// Login erfolgreich
			return true;
		}
	}

	public static boolean login_admin(String username, String password) throws SQLException {
		Connection conn = openConnection();
		// Suchen von Account mit genau dieser Username + Passwort Kombination
		String sql = "SELECT COUNT(*) AS c1 FROM Administrator WHERE LCASE(benutzername) = ? AND passwort = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, username.toLowerCase());
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();
		// Wenn count > 0, dann existiert mindestens ein Account zu dem diese Username + Passwort Kombi passt
		// da Usernames eindeutig kann count auch nicht > 1 sein.
		int count = 0;
		if (rs.next()) {
			count = rs.getInt("c1");
		}
		ps.close();
		rs.close();
		conn.close();
		if (count == 0) {
			// Account existiert so nicht => Login fehlgeschlagen
			return false;
		} else {
			// Login erfolgreich
			return true;
		}
	}

	// Methode um Datenbank eintrag f�r einen Account zu erstellen
	public static int create(User user) throws SQLException {
		// Verwendung der User Bean
		int id = -1;
		Connection conn = openConnection();
		// Duplikate bereits in Controller ausgeschlossen
		// Insert in Accounts Table null f�r id, da id = Identity
		String sql = "INSERT INTO Benutzer VALUES (null, ?, ?, ?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, user.getForename());
		ps.setString(2, user.getSurname());
		ps.setString(3, user.getEmail());
		ps.setString(4, user.getPassword());
		ps.setInt(5, -1);
		ps.executeUpdate();
		ps.close();
		// id des eben erstellten Accounts fetchen 
		// neueste ID
		// String stmnt = "SELECT ID FROM Accounts ORDER BY ID DESC LIMIT 1";
		// sichere Weg
		sql = "SELECT BenutzerID FROM Benutzer WHERE LCASE(email) = ? AND passwort = ?";
		PreparedStatement ps2 = conn.prepareStatement(sql);
		ps2.setString(1, user.getEmail().toLowerCase());
		ps2.setString(2, user.getPassword());
		ResultSet rs = ps2.executeQuery();
		if (rs.next()) {
			id = rs.getInt("BenutzerID");
		}
		ps2.close();
		rs.close();
		conn.close();
		// id des neuen Accounts wird zur�ckgegeben
		// falls id immernoch -1 => Fehler
		return id;
	}


	// Methode zur Pr�fung ob Email bereits existiert
	// analog zu existsUsername
	public static boolean existsEmail(String email) throws SQLException {
		Connection conn = openConnection();
		String sql = "SELECT COUNT(*) AS email FROM Benutzer WHERE LCASE(email) = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, email.toLowerCase());
		ResultSet rs = ps.executeQuery();
		int count = 0;
		if (rs.next()) {
			count = rs.getInt("email");
		}
		ps.close();
		rs.close();
		conn.close();
		if (count == 0) {
			// email existiert (noch) nicht
			return false;
		} else {
			// email existiert bereits
			return true;
		}
	}

	// Methode zum L�schen eins Accounts
	public static void deleteAccount(int accID) throws SQLException {
		Connection conn = openConnection();
		// l�schen aus Accounts Table
		String sql = "DELETE FROM Accounts WHERE ID = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accID);
		ps.executeUpdate();
		ps.close();
		// l�schen aus Dates Table
		sql = "DELETE FROM Dates WHERE acc_ID = ?";
		PreparedStatement ps2 = conn.prepareStatement(sql);
		ps2.setInt(1, accID);
		ps2.executeUpdate();
		ps2.close();
		// l�schen aus Events Table
		sql = "DELETE FROM Events WHERE acc_ID = ?";
		PreparedStatement ps3 = conn.prepareStatement(sql);
		ps3.setInt(1, accID);
		ps3.executeUpdate();
		ps3.close();
		conn.close();
	}


	// neu:
	// Account id fetchen nur durch username (username eindeutig)
	public static int getAccountIdByName(String email) throws SQLException {
		int id = -1;
		Connection conn = openConnection();
		String sql = "SELECT Benutzerid FROM Benutzer WHERE LCASE(email) = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, email.toLowerCase());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			id = rs.getInt("Benutzerid");
		}
		ps.close();
		rs.close();
		conn.close();
		return id;
	}
	
	// Username zur�ckgeben, durch account id
	public static String getUsernameById(int accID) throws SQLException {
		String username = null;
		Connection conn = openConnection();
		String sql = "SELECT email FROM Benutzer WHERE Benutzerid = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accID);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			username = rs.getString("email");
		}
		ps.close();
		rs.close();
		conn.close();
		return username;
	}

	public static int getRollenIDById(int accID) throws SQLException{
		int RollenID = -1;
		Connection conn = openConnection();
		String sql = "SELECT RollenID FROM Benutzer WHERE Benutzerid = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, accID);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			RollenID = rs.getInt("RollenID");
		}
		ps.close();
		rs.close();
		conn.close();
		return RollenID;
	}
}
