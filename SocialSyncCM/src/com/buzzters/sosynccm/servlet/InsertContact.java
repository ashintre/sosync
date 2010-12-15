package com.buzzters.sosynccm.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.sql.Statement;
import java.sql.Connection;
import com.mysql.jdbc.*;
//import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class InsertContact
 */
public class InsertContact extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertContact() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String connectionURL = "jdbc:mysql://localhost:3306/socialsyncdb?username='root'&password='mypass'"; 
		Connection connection = null;
		Statement statement = null; 
		ResultSet rs = null;
		
		String username = request.getParameter("username");
		String contactname = request.getParameter("contactname");
		String phonenumber = request.getParameter("phonenumber");
		String emailaddress = request.getParameter("emailaddress");
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			connection = DriverManager.getConnection(connectionURL, "root", "mypass"); 
			statement = connection.createStatement(); 
			rs = statement.executeQuery("SELECT * FROM sosync_contacts where user_id = (SELECT id from sosync_users where username = '" + username + "')");
			if(rs.next()) {
				int userid = rs.getInt("user_id");
				statement.executeUpdate("INSERT INTO sosync_contacts (contact_name, contact_phone, contact_email, user_id) values ('" + contactname + "','" + phonenumber + "','" + emailaddress + "', " + userid + " )"); 				
			}
			
			rs.close();
			statement.close();
			connection.close();
			
		} catch (Exception e) {
			System.out.println("Exception Occured: ");
			e.printStackTrace();
		
		
		}
		}
}

