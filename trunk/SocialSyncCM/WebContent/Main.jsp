<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.mysql.*" %>

<% 
String connectionURL = "jdbc:mysql://localhost:3306/socialsyncdb?username='root'&password='mypass'"; 
Connection connection = null; 
Statement statement = null; 
ResultSet rs = null; 
String obtainedUsername=request.getParameter("username");
%>     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Contact Information</title>
</head>
<body>
		<h2>Contacts List</h2>
			<table border="1" cellspacing="0" cellpadding="0">
			<tr>
				<td><b>Serial No</b></td>
				<td><b>Name</b></td>
				<td><b>Phone No</b></td>
				<td><b>Email Id</b></td>
			</tr>
			 	<%
			 	Class.forName("com.mysql.jdbc.Driver").newInstance(); 
				connection = DriverManager.getConnection(connectionURL, "root", "mypass"); 
				statement = connection.createStatement(); 
				rs = statement.executeQuery("SELECT * FROM sosync_contacts where user_id = (SELECT id from sosync_users where username = '" + obtainedUsername + "')");
				int no=1;
				while(rs.next()){
				%>
					<tr>
					  <td><%=no%></td>
					  <td><%=rs.getString("contact_name")%></td>
					  <td> <%=rs.getString("contact_phone")%> </td>
					  <td> <%=rs.getString("contact_email")%> </td>	
					</tr>
					<%
					no++;
				}
				rs.close();
				statement.close();
				connection.close();
			%>
			</table>
</body>
</html>