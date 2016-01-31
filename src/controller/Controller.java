package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.User;
import database.DbMgrAccounts;

// Controller zur Steuerung von Redirects und Forwarding

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Controller() {
		super();
	}

	//doGet Methode f�r simple Weiterleitungen
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		// action Parameter wird von der jeweiligen jsp gesetzt.
		String action = request.getParameter("action");

		// falls Parameter noch nicht gesetzt, wird man auf die Startseite weitergeleitet.
		if (action == null || action.equals("index")) {
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} else if (action.equals("login")) {
			// Attribute werden zu leeren Strings bei der Weiterleitung, da sie zum bef�llen der Input Felder verwendet werden.
			// Damit z.B. bei Eingabe eines falschen Passworts, man den Username nicht nochmal eingeben muss.
			request.setAttribute("username", "");
			request.setAttribute("password", "");
			// Message = Error Meldung, bei ung�ltigen Eingaben
			request.setAttribute("message", "");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		} else if (action.equals("register")) {
			// dasselbe wie beim Login, nur f�r mehr Felder
			request.setAttribute("email", "");
			request.setAttribute("forename", "");
			request.setAttribute("surname", "");
			request.setAttribute("password", "");
			request.setAttribute("password2", "");
			request.setAttribute("message", "");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
		} else if (action.equals("logout")) {
			// beim Klicken des Logout Links auf der Home Seite eines Nutzers
			// Invalidierung der Session, entfernen der Session Attribute
			request.getSession().removeAttribute("username");
			request.getSession().removeAttribute("accountID");
			request.getSession().removeAttribute("RollenID");
			request.getSession().invalidate();
			request.getRequestDispatcher("/logoutsuccess.jsp").forward(request, response);
		} else if (action.equals("home")) {
			request.getRequestDispatcher("/home.jsp").forward(request, response);
		} else {
			// wenn eine unbekannte action vorliegt, Ausgabe eines einfachen Fehlertextes
			out.println("unrecognized action");
			return;
		}
	}
	
	// doPost f�r Login- und Registrierungs-Aktionen

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String action = request.getParameter("action");

		// falls Parameter noch nicht gesetzt, wird man auf die Startseite weitergeleitet.
		if (action == null) {
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} else if (action.equals("dologin")) {
			// Beim Ausf�hren des Logins
			// Fetchen der User Eingaben
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			// Passwort wird nicht l�nger als Attribut gespeichert => Passwort muss bei ung�ltigen Eingaben neu eingegeben werden.
			request.setAttribute("username", username);
			request.setAttribute("password", "");
			request.setAttribute("message", "");

			// Datenbank Zugriff
			try {
				if (DbMgrAccounts.login(username, password)) {
					// �berpr�fen der Login Daten durch Datenbank
					//request.getSession().setAttribute("username", username);
					// zuerst anhand des usernames (wird in lowercase abgeglichen) die account id fetchen
					int accID = DbMgrAccounts.getAccountIdByName(username);
					request.getSession().setAttribute("accountID", accID);
					// dann anhand der id, den username aus der Datenbank nutzen 
					// (so passt die Gro�/Kleinschreibung, die bei der Registrierung verwendet wurde)
					request.getSession().setAttribute("username", DbMgrAccounts.getUsernameById(accID));
					request.getSession().setAttribute("RollenID", DbMgrAccounts.getRollenIDById(accID));
					// Weiterleitung auf Anmeldebest�tigungsseite (automatische Weiterleitung von dort nach 2 Sekunden; siehe jsp)
					request.getRequestDispatcher("/home.jsp").forward(request, response);
				} else {
					// Bei fehlerhaften Eingaben, setzen einer Fehlermeldung
					request.setAttribute("message", "Ungültiger Benutzername oder Passwort");
					request.getRequestDispatcher("/login.jsp").forward(request, response);
				}
			} catch (SQLException e) {
				// Bei SQLException Datenbankverbindung �berpr�fen
				// URL in den DbMgr Klassen angepasst?
				// Datenbank lokal ge�ffnet und blockiert?
				// SQL Expression einer Methode fehlerhaft? (n�, die gehen alle)
				request.setAttribute("errorMessage", "Beim Login ist ein Fehler aufgetreten.");
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			}
		} else if (action.equals("doregister")) {
			// Fetchen der User Eingaben
			String email = request.getParameter("email");
			String forename = request.getParameter("forename");
			String surname = request.getParameter("surname");
			String password = request.getParameter("password");
			String password2 = request.getParameter("password2");

			// Attribute clearen, email und username Feld bleibt bestehen
			request.setAttribute("email", email);
			request.setAttribute("forename", forename);
			request.setAttribute("surname", surname);
			request.setAttribute("password", "");
			request.setAttribute("password2", "");
			request.setAttribute("message", "");

			// �berpr�fung auf Unstimmigkeiten
			if (!password.equals(password2)) {
				// Passw�rter stimmen nicht �berein (Pw und Pw wdh.)
				request.setAttribute("message", "Passw�rter stimmen nicht �berein.");
				request.getRequestDispatcher("/register.jsp").forward(request, response);
			} else {
				// User Bean erstellen und validieren
				User user = new User(forename, surname, password, email);

				if (!user.validate()) {
					// wenn User nicht validiert, liegen ung�ltige Eingaben vor (siehe User.java)
					// message ergibt sich aus jeweiligem Fehler
					request.setAttribute("message", user.getMessage());
					request.getRequestDispatcher("/register.jsp").forward(request, response);
				} else {
					try {
						// Datenbank Abgleiche
						if (DbMgrAccounts.existsEmail(email)) {
							// existiert Email bereits?
							System.out.println("email existiert bereits.");
							request.setAttribute("message", "Die angegebene Email Adresse wird bereits verwendet.");
							request.getRequestDispatcher("/register.jsp").forward(request, response);
						} else {
							System.out.println("email wurde noch nicht verwendet.");
							// WENN alles passt, dann wird ein neuer Account angelegt
							// Methode liefert au�erdem sofort die neue Account ID
							user.setId(DbMgrAccounts.create(user));
							System.out.println("User wurde erstellt.");
							request.setAttribute("userId", user.getId());
							request.getRequestDispatcher("/registersuccess.jsp").forward(request, response);
						}
					} catch (SQLException e) {
						// hier nochmal SQLException
						// bei Fehlern, die DbMgrAccounts Klasse �berpr�fen
						request.setAttribute("errorMessage", "Bei der Registrierung ist ein Fehler aufgetreten.");
						request.getRequestDispatcher("/error.jsp").forward(request, response);
					}
				}
			}
		} else {
			// unbekannte Aktion als action Parameter
			out.println("unrecognized action");
			return;
		}
	}
}
