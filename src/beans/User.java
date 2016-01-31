package beans;

import util.InputChecker;

// User bean
// erleichtert Account Erstellung durch die validate Methode

public class User {
    private int id = -1;
    private String forename = "";
    private String surname = "";
    private String password = "";
    private String email = "";

    private String message = "";


    public User() {

    }


    public User(String forename, String surname, String password, String email) {
        this.forename = forename;
        this.surname = surname;
        this.password = password;
        this.email = email;
    }

    public boolean validate() {
        if (email.length() <= 0) {
            message = "Email Adresse fehlt.";
            return false;
        } else if (!InputChecker.checkEMailAddress(email)) { // !email.matches("\\w+@\\w+\\.\\w+")
            message = "Ungültige Email Adresse.";
            return false;
        } else if (forename.length() <= 0) {
            message = "Vorname fehlt.";
            return false;
        } else if (surname.length() <= 0) {
            message = "Nachname fehlt.";
            return false;
        } else if (password.length() < 6) {
            message = "Passwort muss mindestens 6 Zeichen lang sein.";
            return false;
        } else if (password.matches("\\w*\\s+\\w*")) {
            message = "Passwort darf keine Leerzeichen enthalten.";
            return false;
        }
        message = "User validates OK.";
        return true;
    }

    // Getter und Setter

    public int getId() {
        return id;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }


    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
