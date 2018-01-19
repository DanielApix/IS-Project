/** @author Danilo Apicella **/

package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import databaseconnector.DriverManagerConnectionPool;

/** Account precedentemente registrato nel sistema. */
  public final class Account {
 /**
  * @param username del nuovo account
  * @param password del nuovo account
  * @param tipo del nuovo account,
  *  ossia Studente, TutorAmminsitrativo, TutorAziendale oppure AGT
  */
  private Account(String username, String password, TipoAccount tipo) {
    this.username = username;
    this.password = password;
    this.tipo = tipo;
  }
	
  /**
  * Fornisce un oggetto account precedentemente registrato.
  * 
  * @param username dell'account
  * @return account con quell'username registrato nel sistema, altrimenti null
  * @throws SQLException per mancanta connessione al database
  *  o errore nella memorizzazione di un account (TipoAccount non valido)
  */
  public static Account get(String username) throws SQLException {
    Connection connection = null;
	connection = DriverManagerConnectionPool.getConnection();
	String sql = "select * from Account where username = ?";
	PreparedStatement stm = connection.prepareStatement(sql);
	stm.setString(1, username);
	ResultSet result = stm.executeQuery();
	if (!result.next()) {  // se non trova account con quell'username
	  return null;
	}
	return new Account(result.getString("username"), 
			result.getString("password"), 
			TipoAccount.valueOf(result.getString("tipo")));
  }
	
  /**
  * Crea e rende persistente una istanza della classe Account.
  * 
  * @param username del nuovo account
  * @param password del nuovo account
  * @param tipo del nuovo account, ossia Studente, TutorAmminsitrativo, TutorAziendale oppure AGT
  * @throws SQLException nel caso di mancata connessione con il database
  * @throws AccountGi‡Esistente nel caso venga rilevato un altro account con lo stesso username
  */
  public static void create(String username, 
		  String password, TipoAccount tipo) 
				  throws SQLException, AccountGi‡Esistente {
    Connection connection = null;
    if (get(username) != null) {
	  throw new AccountGi‡Esistente();
    }
	connection = DriverManagerConnectionPool.getConnection();
	String sql = "INSERT INTO `ssgt`.`account` (`username`, "
			+ "`password`, `tipo`) VALUES (?, ?, ?);\r\n";
	PreparedStatement stm = connection.prepareStatement(sql);
	stm.setString(1, username);
	stm.setString(2, password);
	stm.setString(3, tipo.toString());
	stm.executeUpdate();
	connection.commit();
  }
	
  public String getUsername() {
    return username;
  }
	
  public String getPassword() {
    return password;
  }
  
  /** Un account puÚ essere di diversi tipi: Studente, TutorAmministrativo...**/
  public String getTipo() {
	return tipo.toString();
  }
	
  private final String username;
  private final String password;
  private final TipoAccount tipo;
	
  public enum TipoAccount { Studente, TutorAmministrativo, TutorAziendale, AGT
  }
}
