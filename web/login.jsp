<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Login</title>
    <link rel="stylesheet" href="cssFiles/styleBasic.css" type="text/css"/>
    <link rel="stylesheet" href="cssFiles/styleLogin.css" type="text/css"/>
</head>
<%
    if (request.getAttribute("username") == null) {
        response.sendRedirect(response.encodeURL(request.getContextPath() + "/Controller?action=login"));
        return;
    }
%>
<body>
<p>
    <a class="leftmargin"
       href="<%=response.encodeURL(request.getContextPath() + "/Controller?action=index")%>">Startseite</a>
</p>
<center>
    <h1>Anmeldung</h1>
    <div id="login">
        <form method="post"
              action="<%=response.encodeURL(request.getContextPath() + "/Controller?action=dologin")%>">
            <input type="hidden" name="action" value="dologin"/>
            <table>
                <tr>
                    <td class="login-field">Email:</td>
                    <td><input type="text" maxlength="50" name="username"
                               size="25" value="<%=request.getAttribute("username")%>"/></td>
                </tr>
                <tr>
                    <td class="login-field">Passwort:</td>
                    <td><input type="password" maxlength="50" name="password"
                               size="25" value="<%=request.getAttribute("password")%>"/></td>
                </tr>
                <tr>
                    <td class="align-right" colspan="2"><input type="submit"
                                                               value="login"/></td>
                </tr>
            </table>
            <p class="login-error"><%=request.getAttribute("message")%>
            </p>
        </form>
    </div>
</center>
</body>
</html>