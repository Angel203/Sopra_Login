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


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();


        String action = request.getParameter("action");


        if (action == null || action.equals("index")) {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else if (action.equals("login")) {


            request.setAttribute("username", "");
            request.setAttribute("password", "");

            request.setAttribute("message", "");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else if (action.equals("register")) {

            request.setAttribute("email", "");
            request.setAttribute("forename", "");
            request.setAttribute("surname", "");
            request.setAttribute("password", "");
            request.setAttribute("password2", "");
            request.setAttribute("message", "");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } else if (action.equals("logout")) {


            request.getSession().removeAttribute("username");
            request.getSession().removeAttribute("accountID");
            request.getSession().removeAttribute("RollenID");
            request.getSession().invalidate();
            request.getRequestDispatcher("/logoutsuccess.jsp").forward(request, response);
        } else if (action.equals("home")) {
            request.getRequestDispatcher("/home.jsp").forward(request, response);
        } else {

            out.println("unrecognized action");
            return;
        }
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");

        if (action == null) {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else if (action.equals("dologin")) {

            String username = request.getParameter("username");
            String password = request.getParameter("password");


            request.setAttribute("username", username);
            request.setAttribute("password", "");
            request.setAttribute("message", "");


            try {


                boolean user = DbMgrAccounts.login_benutzer(username, password);
                boolean admin = false;
                int RollenID = -1;
                if (user) {
                    int accID = DbMgrAccounts.getAccountIdByName(username);
                    request.getSession().setAttribute("accountID", accID);
                    request.getSession().setAttribute("username", DbMgrAccounts.getUsernameById(accID));
                    RollenID = DbMgrAccounts.getRollenIDById(accID);
                    request.getSession().setAttribute("RollenID", RollenID);
                } else if (!user) {

                    admin = DbMgrAccounts.login_admin(username, password);
                    if (admin) {
                        int accID = DbMgrAccounts.getAccountIdByNameAdmin(username);
                        request.getSession().setAttribute("accountID", accID);
                        request.getSession().setAttribute("username", DbMgrAccounts.getUsernameByIdAdmin(accID));
                        RollenID = 0;
                    }
                }


                if (user || admin) {

                    if (RollenID == 0) {
                        request.getRequestDispatcher("home_admin.jsp").forward(request, response);
                    } else if (RollenID == 1) {
                        request.getRequestDispatcher("home_hiwi.jsp").forward(request, response);
                    } else if (RollenID == 2) {
                        request.getRequestDispatcher("home_betreuer.jsp").forward(request, response);
                    } else {
                        request.getRequestDispatcher("home.jsp").forward(request, response);
                    }

                } else {

                    request.setAttribute("message", "Ungültiger Benutzername oder Passwort");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                }
            } catch (SQLException e) {

                request.setAttribute("errorMessage", "Beim Login ist ein Fehler aufgetreten.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else if (action.equals("doregister")) {

            String email = request.getParameter("email");
            String forename = request.getParameter("forename");
            String surname = request.getParameter("surname");
            String password = request.getParameter("password");
            String password2 = request.getParameter("password2");


            request.setAttribute("email", email);
            request.setAttribute("forename", forename);
            request.setAttribute("surname", surname);
            request.setAttribute("password", "");
            request.setAttribute("password2", "");
            request.setAttribute("message", "");


            if (!password.equals(password2)) {

                request.setAttribute("message", "Passw�rter stimmen nicht �berein.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            } else {

                User user = new User(forename, surname, password, email);

                if (!user.validate()) {

                    request.setAttribute("message", user.getMessage());
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                } else {
                    try {

                        if (DbMgrAccounts.existsEmail(email)) {

                            System.out.println("email existiert bereits.");
                            request.setAttribute("message", "Die angegebene Email Adresse wird bereits verwendet.");
                            request.getRequestDispatcher("/register.jsp").forward(request, response);
                        } else {
                            System.out.println("email wurde noch nicht verwendet.");

                            user.setId(DbMgrAccounts.create(user));
                            System.out.println("User wurde erstellt.");
                            request.setAttribute("userId", user.getId());
                            request.getRequestDispatcher("/registersuccess.jsp").forward(request, response);
                        }
                    } catch (SQLException e) {

                        request.setAttribute("errorMessage", "Bei der Registrierung ist ein Fehler aufgetreten.");
                        request.getRequestDispatcher("/error.jsp").forward(request, response);
                    }
                }
            }
        } else {

            out.println("unrecognized action");
            return;
        }
    }
}
