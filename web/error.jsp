<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Error</title>
    <link rel="stylesheet" href="cssFiles/styleBasic.css" type="text/css"/>
    <link rel="stylesheet" href="cssFiles/styleError.css" type="text/css"/>
</head>
<body>
<center>
    <h1>Error</h1>
    <p class="error-text">Das hätte nicht passieren dürfen.</p>
    <br/>
    <p><%=request.getAttribute("errorMessage")%>
    </p>
</center>
</body>
</html>