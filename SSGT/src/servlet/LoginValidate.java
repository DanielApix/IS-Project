/** @author Manuel Ferraro **/
package servlet;

import java.sql.*;  
import javax.servlet.http.HttpSession;

public class LoginValidate {  
public static boolean validate(String name,String pass){  
boolean status=false;  
try{  
Class.forName("oracle.jdbc.driver.OracleDriver");  
Connection con=DriverManager.getConnection(  
"jdbc:oracle:thin:@localhost:1521:xe","root","root");  //Da modificare con i dati del database
      
PreparedStatement ps=con.prepareStatement(  
"select * from userreg where name=? and pass=?");  // name e pass vanno modificate in base al nome nel DB
ps.setString(1,name);  
ps.setString(2,pass);  
ses = request.getSession(true);
ses.setAttribute("userType");	      
ResultSet rs=ps.executeQuery();  
status=rs.next();  
          
}catch(Exception e){System.out.println(e);}  
return status;  
}  
} 