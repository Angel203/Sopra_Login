<%--
  Created by IntelliJ IDEA.
  User: Stefan
  Date: 31.01.2016
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Index</title>
    <link rel="stylesheet" href="cssFiles/styleBasic.css" type="text/css"/>
    <link rel="stylesheet" href="cssFiles/styleIndex.css" type="text/css"/>
</head>
<body>
<center>
    <h1>Sopra_Login</h1>
    <p>von Team 21</p>
    <br/>
    <p>
        <a href="<%=response.encodeURL(request.getContextPath() + "/Controller?action=login")%>">Zur
            Anmeldung</a>
    </p>
    <p>
        <a href="<%=response.encodeURL(request.getContextPath() + "/Controller?action=register")%>">Zur
            Registrierung</a>
    </p>
</center>
</body>
</html>
