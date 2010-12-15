<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.mysql.*" %>

<% 
String connectionURL = "jdbc:mysql://localhost:3306/socialsyncdb?username='root'&password='mypass'"; 
Connection connection = null; 
Statement statement = null; 
ResultSet rs = null; 
ResultSet rs1 = null;
%>     

<%
String username=request.getParameter("username");
String password1=request.getParameter("password1"); 
String password2=request.getParameter("password2");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
	<%
		boolean allOK = true;
		if( username== "") {
			allOK = false;
			out.println("\n\nUsername cannot be left blank");
		}
		
		if( password1 == "" ) {
			out.println("\n\nPassword cannot be left blank");
			allOK = false;
		}
		else if ( !(password1.equals(password2)) ) {
				out.println("\n\nPasswords do not match");
				allOK = false;
		}
		
		if( allOK == true ) {
			boolean existsAlready = false; 
			
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			connection = DriverManager.getConnection(connectionURL, "root", "mypass"); 
			statement = connection.createStatement(); 
			rs = statement.executeQuery("SELECT username FROM sosync_users");
			
			while(rs.next()) {
				if(rs.getString("username").equals(username)) {
					existsAlready = true;
					out.println("\nUsername exists already!");
					allOK = false;
					break;
				}
			}
			rs.close();
			
			if(!existsAlready) {
				ResultSet rs2;
				rs2 = statement.executeQuery("SELECT MAX(id) from sosync_users");
				if(rs2.next()) {
					int userid = rs2.getInt(1);
					++userid;
				    statement.executeUpdate("INSERT INTO sosync_users (id, username,password) VALUES (" + userid+ " , '" + username + "','" + password1 + "')");
					out.println("\nRegistration Successful. Please login to continue..");
					rs2.close();
					statement.close();
					connection.close();
					response.setHeader("Refresh","3;Login.jsp");
				}
			}
			else {
				rs.close();
				statement.close();
				connection.close();
				out.println("\nThere were errors with your registration. Please review and sign up again");
				response.setHeader("Refresh","5;SignUp.jsp");
			}
		}
		else {
				out.println("There were errors with your registration. Please review and sign up again");
				response.setHeader("Refresh","5;SignUp.jsp");
		}
			
		
	%>
</body>
</html>