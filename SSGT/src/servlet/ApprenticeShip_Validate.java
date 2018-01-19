/** @author Manuel Ferraro **/
package servlet;

import java.io.IOException;  
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ApprenticeShip_Validate extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:oracle:thin:@localhost:1521:xe","root","root");  //Da modificare con i dati del database
			if(Richiesta.conferma(request.getSession(false).getAttribute("userType")))
			{
				out.print("Confermata con successo");
			}
			else
			{
				out.print("Errore durante la conferma");
				RequestDispatcher rd=request.getRequestDispatcher("#");  //inserire link redirect
		        rd.include(request,response);  
			}
		}catch (Exception e2) {System.out.println(e2);}  
	    
		out.close();
	}

}
