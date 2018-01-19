/** @author Manuel Ferraro **/
package servlet;

import java.io.IOException;  
import java.io.PrintWriter;  
  
import javax.servlet.RequestDispatcher;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;
  
  
public class LoginServlet extends HttpServlet
{  
public void doPost(HttpServletRequest request, HttpServletResponse response)  
        throws ServletException, IOException 
{  
  
    response.setContentType("text/html");  
    PrintWriter out = response.getWriter();  
          
    String nomeUtente =request.getParameter("username");  
    String password =request.getParameter("userpass");  
          
    if(LoginValidate.validate(nomeUtente, password))
    {  
    	
        RequestDispatcher rd=request.getRequestDispatcher("Inserire Quì Nome Del Redirect");  
        rd.forward(request,response);  
    }  
    else
    {  
        out.print("Sorry username or password error");  
        RequestDispatcher rd=request.getRequestDispatcher("index.html");  
        rd.include(request,response);  
    }  
          
    out.close();  
    }  
} 

