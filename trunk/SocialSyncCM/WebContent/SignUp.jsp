<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sign Up</title>
</head>
<body>
	<form name="Registration" action="ProcessReg.jsp" method="POST">
		Username : <input type="text" name="username"></input><br/>
		Password : <input type="password" name="password1"></input><br/>
		Confirm Password : <input type="password" name="password2"></input><br/>
		<input type="submit" value="Sign Up"></input><br/><br/>
	</form>	
</body>
</html>