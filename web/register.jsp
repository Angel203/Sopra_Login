<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Registrierung</title>
    <link rel="stylesheet" href="cssFiles/styleBasic.css" type="text/css"/>
    <link rel="stylesheet" href="cssFiles/styleRegister.css" type="text/css"/>

</head>
<%
    if (request.getAttribute("forename") == null) {
        response.sendRedirect(response.encodeURL(request.getContextPath() + "/Controller?action=register"));
        return;
    }
%>
<body>
<p>
    <a class="leftmargin"
       href="<%=response.encodeURL(request.getContextPath() + "/Controller?action=index")%>">Startseite</a>
</p>
<center>
    <h1>Registrierung</h1>
    <div id="register">
        <form method="post"
              action="<%=response.encodeURL(request.getContextPath() + "/Controller?action=doregister")%>">
            <input type="hidden" name="action" value="doregister"/>
            <table>
                <tr>
                    <td class="register-field">Email:</td>
                    <td><input type="text" maxlength="30" name="email" size="25"
                               value="<%=request.getAttribute("email")%>"/></td>
                </tr>
                <tr>
                    <td class="register-field">Vorname:</td>
                    <td><input type="text" maxlength="20" name="forename" size="25"
                               value="<%=request.getAttribute("forename")%>"/></td>
                </tr>
                <tr>
                    <td class="register-field">Nachname:</td>
                    <td><input type="text" maxlength="20" name="surname" size="25"
                               value="<%=request.getAttribute("surname")%>"/></td>
                </tr>
                <tr>
                    <td class="register-field">Passwort:</td>
                    <td><input type="password" maxlength="40" name="password" size="25"
                               value="<%=request.getAttribute("password")%>"/></td>
                </tr>
                <tr>
                    <td class="register-field">Passwort wdh.:</td>
                    <td><input type="password" maxlength="40" name="password2"
                               size="25" value="<%=request.getAttribute("password2")%>"/></td>
                </tr>
                <tr>
                    <td class="align-right" colspan="2"><input type="submit"
                                                               value="registrieren"/></td>
                </tr>
            </table>
            <p class="register-error"><%=request.getAttribute("message")%>
            </p>
        </form>
    </div>
</center>
</body>
</html>