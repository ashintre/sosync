<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
	<form name="Main" action="Intermittent.jsp" method="POST">
		Username : <input type="text" name="username"></input><br/>
		Password : <input type="password" name="password"></input><br/>
		<input type="submit" value="Login"></input><br/><br/>
	</form>	
	<form name="signUp" action="SignUp.jsp" method="POST">
		New User? Sign up here <input type="submit" value="Sign Up"></input>
	</form>
</body>
</html>