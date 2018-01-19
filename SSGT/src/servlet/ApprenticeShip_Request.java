/** @author Manuel Ferraro **/
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApprenticeShip_Request extends HttpServlet {
public void doPost(HttpServletRequest request, HttpServletResponse response) 
throws ServletException, IOException {
	PrintWriter out = response.getWriter();
	try{
		Class.forName("oracle.jdbc.driver.OracleDriver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:oracle:thin:@localhost:1521:xe","root","root");  //Da modificare con i dati del database
		String mittente=request.getSession(false).getAttribute("userType");
		String[] destinatari=request.getParameter("destinatari");
		 if(Richiesta.create(mittente, destinatari[],"richiestaTirocinio"))
		    {  
		        out.print("Richiesta creata con successo");
		    }  
		    else
		    {  
		        out.print("Richiesta fallita");  
		        RequestDispatcher rd=request.getRequestDispatcher("#");  //inserire link redirect
		        rd.include(request,response);  
		    }
	
	}catch (Exception e2) {System.out.println(e2);}  
    
out.close();  
}  
}
