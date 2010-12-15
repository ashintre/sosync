<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.mysql.*" %>

<% 
String connectionURL = "jdbc:mysql://localhost:3306/socialsyncdb?username='root'&password='mypass'"; 
Connection connection = null; 
Statement statement = null; 
ResultSet rs = null; 
String username=request.getParameter("username");
String password=request.getParameter("password");
int flag = 0;
%>     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Contact Information</title>
</head>
<body>
	<% 
		Class.forName("com.mysql.jdbc.Driver").newInstance(); 
		connection = DriverManager.getConnection(connectionURL, "root", "mypass"); 
		statement = connection.createStatement(); 
		rs = statement.executeQuery("SELECT * FROM sosync_users"); 
		
		while (rs.next()) { 
			if((rs.getString("username").equals(username)) && (rs.getString("password").equals(password))) {
				flag = 1;
				break;
			}
		} 		
		rs.close();
		
		if(flag == 0) {
			out.println("Username and password do not match. You will be redirected back to the login page shortly");
			response.setHeader("Refresh","2;Login.jsp");
		}
		else {
			out.println("Login successful. Please wait..");
			request.setAttribute("username", username);
			getServletContext().getRequestDispatcher("/Main.jsp").forward(request,response);
		}
	%>	
</body>
</html>