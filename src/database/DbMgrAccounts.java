package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import beans.User;


public final class DbMgrAccounts {

    public static final String URL = "jdbc:hsqldb:file:C:\\Users\\Stefan\\IdeaProjects\\Sopra_Login\\web\\db\\Sopra_Login";
    public static final String DRIVER = "org.hsqldb.jdbcDriver";


    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private DbMgrAccounts() {

    }


    public static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(URL, "SA", "");
    }


    public static boolean login_benutzer(String username, String password) throws SQLException {
        Connection conn = openConnection();

        String sql = "SELECT COUNT(*) AS c1 FROM Benutzer WHERE LCASE(email) = ? AND passwort = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username.toLowerCase());
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        int count = 0;
        if (rs.next()) {
            count = rs.getInt("c1");
        }
        ps.close();
        rs.close();
        conn.close();
        if (count == 0) {

            return false;
        } else {

            return true;
        }
    }

    public static boolean login_admin(String username, String password) throws SQLException {
        Connection conn = openConnection();
        String sql = "SELECT COUNT(*) AS c1 FROM Administrator WHERE LCASE(benutzername) = ? AND passwort = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username.toLowerCase());
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        int count = 0;
        if (rs.next()) {
            count = rs.getInt("c1");
        }
        ps.close();
        rs.close();
        conn.close();
        if (count == 0) {

            return false;
        } else {

            return true;
        }
    }


    public static int create(User user) throws SQLException {

        int id = -1;
        Connection conn = openConnection();


        String sql = "INSERT INTO Benutzer VALUES (null, ?, ?, ?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getForename());
        ps.setString(2, user.getSurname());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPassword());
        ps.setInt(5, -1);
        ps.executeUpdate();
        ps.close();


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


        return id;
    }


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

            return false;
        } else {

            return true;
        }
    }


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

    public static int getRollenIDById(int accID) throws SQLException {
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

    public static int getAccountIdByNameAdmin(String username) throws SQLException {
        int id = -1;
        Connection conn = openConnection();
        String sql = "SELECT AdministratorID FROM Administrator WHERE LCASE(Benutzername) = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username.toLowerCase());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            id = rs.getInt("AdministratorID");
        }
        ps.close();
        rs.close();
        conn.close();
        return id;
    }

    public static String getUsernameByIdAdmin(int accID) throws SQLException {
        String username = null;
        Connection conn = openConnection();
        String sql = "SELECT Benutzername FROM Administrator WHERE AdministratorID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            username = rs.getString("Benutzername");
        }
        ps.close();
        rs.close();
        conn.close();
        return username;
    }
}
