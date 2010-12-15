<%@ page import="java.io.*,java.sql.*"%>
<html>
<body>
<% 

String filename = "c:\\csv\\mycontactsfile.csv";
String connectionURL = "jdbc:mysql://localhost:3306/socialsyncdb?username='root'&password='mypass'"; 
Connection connection = null; 
Statement statement = null; 
ResultSet rs = null; 
String username=request.getParameter("username");
try
{
	FileWriter fw = new FileWriter(filename);
	fw.append("Name");
	fw.append(',');
	fw.append("Phone Number");
	fw.append(',');
	fw.append("Email Address");
	fw.append('\n');
	
	Class.forName("com.mysql.jdbc.Driver").newInstance(); 
	connection = DriverManager.getConnection(connectionURL, "root", "mypass"); 
	statement = connection.createStatement(); 
	rs = statement.executeQuery("SELECT * FROM sosync_contacts where userid = (SELECT id from sosync_users where username = '" + username + "'))"); 
	while(rs.next())
	{
		fw.append(rs.getString(1));
		fw.append(',');
		fw.append(rs.getString(2));
		fw.append(',');
		fw.append(rs.getString(3));
		fw.append('\n');
	}
	fw.flush();
	fw.close();
	connection.close();
	out.println("<b>CSV File Successfully Created</b>");
	request.setAttribute("username", username);
	getServletContext().getRequestDispatcher("/Main.jsp").forward(request,response);
} catch (Exception e) {
e.printStackTrace();
}
%>
</body>
</html>