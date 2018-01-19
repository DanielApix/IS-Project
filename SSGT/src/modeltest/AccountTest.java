/** @author Danilo Apicella **/

package modeltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import databaseconnector.DriverManagerConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import model.Account;
import model.AccountGi‡Esistente;

public class AccountTest {
	
  /**
  * Crea e rende persistente un account.
  * 
  * @throws SQLException in caso di mancata connessione con il database
  */
  @BeforeClass
  public static void creaAccount() throws SQLException {
    System.out.println("Inizio test classe Account");
    System.out.println("");
    Connection connection = null;
    try {
      // ottengo la connessione verso il database
      connection = DriverManagerConnectionPool.getConnection();

      /* elimino eventuale account con lo stesso nome 
         gi‡ inserito nel database per il test del metodo get */
      String sql = "DELETE FROM Account WHERE username = 'danielapix';";
      PreparedStatement stm = connection.prepareStatement(sql);
      stm.executeUpdate();
      connection.commit();

      /* elimino eventuale account con lo stesso nome 
         gi‡ inserito nel database per il test del metodo create */
      sql = "DELETE FROM Account WHERE username = 'inova';";
      stm = connection.prepareStatement(sql);
      stm.executeUpdate();
      connection.commit();

      // inserisco la nuova tupla
      sql = "INSERT INTO `ssgt`.`account` (`username`, `password`, `tipo`) "
            + "VALUES ('danielapix', 'admin', 'Studente');\r\n";
      stm = connection.prepareStatement(sql);
      stm.executeUpdate();
      connection.commit();
    } finally {
      connection.close();
    }
  }

  /**
  * Classe di equivalenza: account esistente.
  * Verifica che la chiamata a metodo get passando come parametro
  * l'username di un account esistente ritorni correttamente
  * l'istanza ad esso relativa.
  * 
  * @throws SQLException in caso di mancata connessione con il database
  */
  @Test
  public void getAccountEsistente() throws SQLException {
    System.out.println("");
    System.out.println("Inizio test per metodo get passando come parametro"
        + " il nome di un account registrato nel sistema");
    System.out.println("Ottengo una istanza della classe Account con username "
           + "'danielapix'");
    account = Account.get("danielapix");

    System.out.println("Verifica username = 'danielapix'");
    assertEquals("Non c'Ë corrispondenza con l'username",
        "danielapix", account.getUsername());
    System.out.println("corrispondenza verificata");

    System.out.println("Verifica password = 'admin'");
    assertEquals("Non c'Ë corrispondenza con la password",
         "admin", account.getPassword());
    System.out.println("corrispondenza verificata");

    System.out.println("verifica tipo = 'Studente'");
    assertEquals("Non c'Ë corrispondenza con il tipo di account",
          "Studente", account.getTipo());
    System.out.println("corrispondenza verificata");

    System.out.println("Test per metodo get su Account esistente terminato "
            + "senza fallimenti");
  }

  /**
  * Classe di equivalenza: account inesistente.
  * Verifica che la chiamata al metodo get passando come parametro 
  * l'username di un account inesistente e non registrato nel sistema
  * restituisca il valore nullo.
  * 
  * @throws SQLException in caso di mancata connessione con il database
  */
  @Test
  public void getAccountInesistente() throws SQLException {
    System.out.println("");
    System.out.println("Inizio test per metodo get passando come parametro"
        + " il nome di un account non registrato nel sistema");
    System.out.println("interrogazione per un account inesistente nel db");
    System.out.println("nome scelto: 'MCD'");
    assertNull("Il valore ritornato non Ë nullo", Account.get("MCD"));
    System.out.println("valore nullo correttamente ritornato senza eccezioni");
    System.out.println("Test per metodo get su Account inesistente terminato senza fallimenti");
  }

  /**
  * Classe di equivalenza: account inesistente.
  * Verifica che la chiamata sul metodo create con username
  * non ancora usato registri un nuovo account nel sistema
  * @throws SQLException in caso di mancata connessione con il database
  * @throws AccountGi‡Esistente nel caso esista gi‡ un account esistente nel
  *         database
  */
  @Test
  public void createAccountInesistente() throws SQLException, AccountGi‡Esistente {
    System.out.println("");
    System.out.println("Inizio test per metodo create passando come parametro"
         + "un username non ancora utilizzato");
    System.out.println("aggiunta di un nuovo account con i seguenti dati");
    System.out.println("username = 'inova'");
    String username = "inova";
    System.out.println("password = 'pass'");
    String password = "pass";
    System.out.println("tipo = 'Studente'");
    Account.TipoAccount tipo = Account.TipoAccount.Studente;

    System.out.println("istanziazione");
    Account.create(username, password, tipo);

    System.out.println("Avvio della procedura per la verifica"
         + " correttezza dati inseriti/registrati");
    System.out.println("Ottengo una istanza della classe Account con username 'inova'");
    account = Account.get("inova");

    System.out.println("Verifica username = 'inova'");
    assertEquals("Non c'Ë corrispondenza con l'username",
         "inova", account.getUsername());
    System.out.println("corrispondenza verificata");

    System.out.println("Verifica password = 'pass'");
    assertEquals("Non c'Ë corrispondenza con la password", 
          "pass", account.getPassword());
    System.out.println("corrispondenza verificata");

    System.out.println("verifica tipo = 'Studente'");
    assertEquals("Non c'Ë corrispondenza con il tipo di account",
          "Studente", account.getTipo());
    System.out.println("corrispondenza verificata");

    System.out.println("Test per metodo create su Account inesistente terminato senza fallimenti");
  }

  /**
  * Classe di equivalenza: account esistente.
  * Verifica che la chiamata sul metodo create con username
  * gi‡ usato non ne crei uno e lanci una eccezione 
  * di tipo 'AccountGi‡Esistente'.
  * 
  * @throws SQLException in caso di mancata connessione con il database
  * @throws AccountGi‡Esistente nel caso esist‡ gi‡ un account con quell'username salvato
  *         nel database
  */
  @Test
  public void createAccountEsistente() throws SQLException, AccountGi‡Esistente {
    System.out.println("");
    System.out.println("Inizio test per metodo create passando come username"
        + " uno gi‡ utilizzato");
    System.out.println("elimino eventuale account pre-esistente di confronto");
    Connection connection = null;

    // ottengo la connessione verso il database
    connection = DriverManagerConnectionPool.getConnection();

    /* elimino eventuale account con lo stesso nome 
       gi‡ inserito nel database per il test del metodo get */
    String sql = "DELETE FROM Account WHERE username = 'usernameGiaUtilizzato';";
    PreparedStatement stm = connection.prepareStatement(sql);
    stm.executeUpdate();
    connection.commit();
    System.out.println("Aggiungo account con username = 'usernameGiaUtilizzato'"
        + "e password = 'firstPassword'");
    Account.create("usernameGiaUtilizzato", "firstPassword", Account.TipoAccount.Studente);

    System.out.println("aggiunta di un nuovo account con i seguenti dati");
    System.out.println("username = 'usernameGiaUtilizzato', gi‡ esistente");
    String username = "usernameGiaUtilizzato";
    System.out.println("password = 'anotherPassword', diversa dall'account esistente");
    String password = "pass";
    System.out.println("tipo = 'Studente'");
    Account.TipoAccount tipo = Account.TipoAccount.Studente;

    boolean fallimentoPerEccezioneMancante = false;
    System.out.println("istanziazione");
    try {
      Account.create(username, password, tipo);
      System.out.println("eccezione non lanciata");
      fallimentoPerEccezioneMancante = true;
    } catch (AccountGi‡Esistente e) {
      System.out.println("eccezione correttamente generata");
    }
    if (fallimentoPerEccezioneMancante) {
      assertNull("valore non nullo");
    } else {
      System.out.println("Avvio della procedura per la verifica della presenza"
           + " di un solo account registrato con quell'username e di password"
           + " differente da quella inserita");
      System.out.println("Interrogo il database con una query per username in account");
      Connection conn = DriverManagerConnectionPool.getConnection();
      sql = "select * from account where username = 'inova'";
      stm = conn.prepareStatement(sql);
      ResultSet result = stm.executeQuery();
      int cont = 0; boolean accountAggiunto = false;
      while (result.next()) {
      cont++;
      accountAggiunto = result.getString("password").equals("anotherPassword");
      if (accountAggiunto) {
        break;
      }
      assertEquals("Pi˘ di un account registrato con lo stesso username",
           0,  cont);
      assertEquals("Pi˘ di un account registrato con lo stesso username", 
            accountAggiunto, false);
      System.out.println("Test per metodo create su Account "
           + "esistente terminato senza fallimenti");
      }
    }
  }	

  /**
   * Stampa nella console la terminazione del test
   */
  @AfterClass
  public static void concludiTest() {
    System.out.println("Test della classe Account terminato");
    System.out.println("");
    System.out.println("");
  }
  
  private Account account;
}
