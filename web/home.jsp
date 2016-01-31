<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Home</title>
    <link rel="stylesheet" href="cssFiles/styleBasic.css" type="text/css" />
    <link rel="stylesheet" href="cssFiles/styleHome.css" type="text/css" />

</head>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect(response.encodeURL(request.getContextPath() + "/Controller?action=login"));
        return;
    }
%>
<body>
<input type="hidden" id="logoutLink"
       value="<%=response.encodeURL(request.getContextPath() + "/Controller?action=logout")%>"/>
<p class="hideonprint align-right">
    <a class="rightmargin"
       href="<%=response.encodeURL(request.getContextPath() + "/Controller?action=logout")%>">Logout</a>
</p>
<p class="hideonprint leftmargin">
    Aktuell angemeldet als:
    <%=session.getAttribute("username")%>
</p>
<p class="hideonprint leftmargin">
    Account ID:
    <%=session.getAttribute("accountID")%>
</p>
<p class="hideonprint leftmargin">
    Rollen ID:
    <%=session.getAttribute("RollenID")%>
</p>
<center>
    <%
        if (Integer.parseInt(session.getAttribute("RollenID").toString()) == -1) {
            out.print("Sie sind als Benutzer angemeldet. Daher sehen Sie diese Startseite.");
        }
    %>
</center>
</body>
</html>