/** @author Manuel Ferraro **/
package servlet;

import java.io.*;  
import java.sql.*;  
import javax.servlet.ServletException;  
import javax.servlet.http.*;  
  
public class Register extends HttpServlet {  
public void doPost(HttpServletRequest request, HttpServletResponse response)  
throws ServletException, IOException {  
  
response.setContentType("text/html");  
PrintWriter out = response.getWriter();  

//Da modificare nel caso ci siano altri campi da aggiungere
String n=request.getParameter("userName"); 
String p=request.getParameter("userPass");  
String e=request.getParameter("userEmail");  
String c=request.getParameter("userType");  
          
try{  
Class.forName("oracle.jdbc.driver.OracleDriver");  
Connection con=DriverManager.getConnection(  
"jdbc:oracle:thin:@localhost:1521:xe","root","root");  //Da modificare con i dati del database
  
PreparedStatement ps=con.prepareStatement("insert into registeruser values(?,?,?,?)");  
  
ps.setString(1,n);  
ps.setString(2,p);  
ps.setString(3,e);  
ps.setString(4,c);  
          
int i=ps.executeUpdate();  
if(i>0)  
out.print("Registrazione Completata");  
      
          
}catch (Exception e2) {System.out.println(e2);}  
          
out.close();  
}  
  
}  