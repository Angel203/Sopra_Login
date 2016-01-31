<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Registrierung erfolgreich</title>
    <link rel="stylesheet" href="cssFiles/styleBasic.css" type="text/css"/>
    <link rel="stylesheet" href="cssFiles/styleRegistersuccess.css" type="text/css"/>
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
    <h1>Registrierung erfolgreich</h1>
    <div id="message">
        <p class="bigger-text">Es wurde erfolgreich ein Account erstellt</p>
        <br/>
        <table>
            <tr>
                <td class="align-right">UserID:</td>
                <td class="attribute-text"><%=request.getAttribute("userId")%>
                </td>
            </tr>
            <tr>
                <td class="align-right">Vorname:</td>
                <td class="attribute-text"><%=request.getAttribute("forename")%>
                </td>
            </tr>
            <tr>
                <td class="align-right">Nachname:</td>
                <td class="attribute-text"><%=request.getAttribute("surname")%>
                </td>
            </tr>
            <tr>
                <td class="align-right">Email:</td>
                <td class="attribute-text"><%=request.getAttribute("email")%>
                </td>
            </tr>
        </table>
    </div>
    <p class="index-link">
        <a class="bigger-text"
           href="<%=response.encodeURL(request.getContextPath() + "/Controller?action=login")%>">Zur
            Anmeldung</a>
    </p>
</center>
</body>
</html>