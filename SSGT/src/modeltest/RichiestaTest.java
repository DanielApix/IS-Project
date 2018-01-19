/** @author Danilo Apicella */

package modeltest;

import static org.junit.Assert.assertEquals;

import databaseconnector.DriverManagerConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import model.Richiesta;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RichiestaTest {

  /**Elimina una eventuale richiesta salvata per il testing e ne aggiunge un'altra per lo
  * stesso fine.
  * 
  * @throws SQLException in caso di mancata connessione con il database
  */
  @BeforeClass
  public static void inizializza() throws SQLException {
    System.out.println("Inizio test classe Richiesta");
    System.out.println("");
    eliminaRichiesta();
    creaRichiesta();
  }

  /**
  * Crea una richiesta utilizzata peri l testing.
  * 
  * @throws SQLException in caso id mancata connessione con il database
  */
  public static void creaRichiesta() throws SQLException {
    System.out.println("creo una richiesta per il testing");
    Connection connection = null;
    try {
      //ottengo la connessione verso il database
      connection = DriverManagerConnectionPool.getConnection();
      String sql = "INSERT INTO `ssgt`.`Richiesta` "
          + "(`tipo`, `mittente`) VALUES ('RichiestaTirocinio', 'danielapix');\r\n";
      PreparedStatement stm = connection.prepareStatement(sql);
      stm.executeUpdate();
      connection.commit();
      connection = DriverManagerConnectionPool.getConnection();

      sql = "select * from richiesta where mittente = 'danielapix';";
      stm = connection.prepareStatement(sql);
      ResultSet result = stm.executeQuery();
      result.next();
      int id = result.getInt("id");

      sql = "INSERT INTO `ssgt`.`ricevuto_da` (`id`,`mittente`, `destinatario`, "
           + "`risposta`) VALUES (?, 'danielapix', 'giuseppe', 'nonRicevuto');";
      stm = connection.prepareStatement(sql); 
      stm.setInt(1, id);
      stm.executeUpdate();
      connection.commit();

      sql = "INSERT INTO `ssgt`.`ricevuto_da` (`id`, `mittente`, `destinatario`, "
            + "`risposta`) VALUES (?, 'danielapix', 'mario', 'confermato');\r\n";
      stm = connection.prepareStatement(sql);
      stm.setInt(1, id);
      stm.executeUpdate();
      connection.commit();

      sql = "INSERT INTO `ssgt`.`ricevuto_da` (`id`,`mittente`, `risposta`, "
            + "`destinatario`) VALUES (?, 'danielapix', 'rifiutato', 'AGT');";
      stm = connection.prepareStatement(sql);
      stm.setInt(1, id);
      stm.executeUpdate();
      connection.commit();
    } finally {
      connection.close();
    }
  }

  /**
   * Elimina una richiesta utilizzata per il testing.
   * 
   * @throws SQLException in caso di mancata connessione con il database
   */
  public static void eliminaRichiesta() throws SQLException {
    System.out.println("Elimino richiesta creata per il testing");
    Connection connection = null;
    try {
      //ottengo la connessione verso il database
      connection = DriverManagerConnectionPool.getConnection();

      String sql = "delete from ricevuto_da where mittente = 'danielapix'";
      PreparedStatement stm = connection.prepareStatement(sql);
      stm.executeUpdate();
      connection.commit();

      sql = "delete from richiesta where mittente = 'danielapix'";
      stm = connection.prepareStatement(sql);
      stm.executeUpdate();
      connection.commit();
    } finally {
      connection.close();
    }
  }

  /**
  * Verifica la corretta creazione di una richiesta di tirocinio.
  * Attenzione: destinatari "mario", "giuseppe", "AGT" 
  * non devono già esistere in una richiesta, altrimenti viene individuato un fallimento.
  *
  * @throws SQLException in caso di mancata connessione con il database
  */
  @Test
  public void createTipoValido() throws SQLException {
    System.out.println("");
    System.out.println("Inizio test per metodo create con tipo valido");
    eliminaRichiesta();
    System.out.println("aggiunta di una nuova richiesta con i seguenti dati");
    System.out.println("mittente = 'danielapix'");
    String mittente = "danielapix";
    System.out.println("destinatari = {'giuseppe', 'mario', 'agt'}");
    String[] destinatari = {"giuseppe", "mario", "AGT"};
    System.out.println("tipo = RichiestaTirocinio");
    Richiesta.TipoRichiesta tipo = Richiesta.TipoRichiesta.RichiestaTirocinio;
    System.out.println("risposteDestinatari = {'giuseppe' : nonRicevuto, "
          + "'mario' : confermato, 'AGT' : rifiutato");
    System.out.println("istanziazione");
    Richiesta.create(mittente, destinatari, tipo);

    System.out.println("Avvio della procedura per la verifica"
         + " correttezza dati inseriti/registrati");

    Connection connection = null;
    try {
      //ottengo la connessione verso il database
      connection = DriverManagerConnectionPool.getConnection();

      System.out.println("Interrogo il db per verificare l'aggiunta della richiesta");
      String sql = "select * from richiesta where mittente = 'danielapix'";
      PreparedStatement stm = connection.prepareStatement(sql);
      ResultSet result = stm.executeQuery();
      result.next();

      System.out.println("Verifica tipo = 'RichiestaTirocinio'");
      assertEquals("Non c'è corrispondenza con il tipo di richiesta",
           "RichiestaTirocinio", result.getString("tipo"));
      System.out.println("corrispondenza verificata");

      System.out.println("Interrogo il db per verificare l'aggiunta dei destinatari");
      sql = "select * from ricevuto_da where mittente = 'danielapix'";
      stm = connection.prepareStatement(sql);
      result = stm.executeQuery();
      Hashtable<String, String> mappaCorrispondenza = new Hashtable<String, String>();

      while (result.next()) {
        mappaCorrispondenza.put(result.getString("destinatario"), 
             result.getString("risposta"));
      }
      System.out.println("Verifica del numero di destinatari registrati "
            + "per verificare che non ci siano ripetizioni");
      assertEquals(3, mappaCorrispondenza.size());
      System.out.println("Verifica risposta tutor amministrativo = 'nonRicevuto'");
      assertEquals("Non c'è corrispondenza con la risposta del tutor amministrativo", 
             "nonRicevuto", mappaCorrispondenza.get("giuseppe"));
      System.out.println("corrispondenza verificata");

      System.out.println("Verifica risposta tutor aziendale = 'nonRicevuto'");
      assertEquals("Non c'è corrispondenza con la risposta del tutor amministrativo", 
            "nonRicevuto", mappaCorrispondenza.get("mario"));
      System.out.println("corrispondenza verificata");

      System.out.println("Verifica risposta agt = 'nonRicevuto'");
      assertEquals("Non c'è corrispondenza con la risposta dell'agt", 
            "nonRicevuto", mappaCorrispondenza.get("AGT"));
      System.out.println("corrispondenza verificata");
      System.out.println("Test metodo create con valori validi terminato"
          + " senza fallimenti");
    } finally {
      connection.close();
    }

    System.out.println("Test per metodo create su Account inesistente terminato senza fallimenti");
  }

  /**
  * Verifica che il metodo get restituisca una istanza della 
  * classe esistente nel database in maniera consistente.
  * Attenzione: destinatari "mario", "giuseppe", "AGT" non devono 
  * già esistere, altrimenti viene individuato un fallimento.
  *
  * @throws SQLException in caso di mancata connessione con il database
  */
  @Test
  public void getDestinatarioEsistente() throws SQLException {
    System.out.println("");
    System.out.println("Inizio test per metodo get con richiesta per destinatario esistente");
    eliminaRichiesta();
    creaRichiesta();
    System.out.println("Ottenimento richieste per destinatario 'giuseppe'");
    ArrayList<Richiesta> richieste = Richiesta.get("giuseppe");
    System.out.println("verifica dell'esistenza di una sola richiesta per quel destinatario");
    assertEquals(1, richieste.size());
    System.out.println("numero di richieste corretto");
    richiesta = richieste.get(0);
    System.out.println("veririca che il mittente della richiesta ottenuta sia 'danielapix'");
    assertEquals("danielapix", richiesta.getMittente());
    System.out.println("corrispondenza verificata");

    System.out.println("verifica correttezza destinatari della richiesta");
    String[] destinatari = richiesta.getDestinatari();
    for (int i = 0; i < destinatari.length; i++) {
      switch(destinatari[i]) {
        case "giuseppe":
          break;
        case "AGT": 
          break;
        case "mario": 
          break;
        default: 
          assertEquals("1", "2"); /* destinatario non esistente tra i tre, 
                                   quindi fallimento individuato*/
      }
    }
    System.out.println("corrispondenza verificata");

    Hashtable<String, String> risposte = richiesta.getRisposteDestinatari();
    System.out.println("verifica del numero di risposte per escludere ripetizioni");
    assertEquals("Il numero di risposte non corrisponde", 3, risposte.size());
    System.out.println("corrispondenza verificata");

    System.out.println("Verifica risposta tutor amministrativo = 'nonRicevuto'");
    assertEquals("Non c'è corrispondenza con la risposta del tutor amministrativo", 
          "nonRicevuto", risposte.get("giuseppe"));
    System.out.println("corrispondenza verificata");

    System.out.println("Verifica risposta tutor aziendale = 'nonRicevuto'");
    assertEquals("Non c'è corrispondenza con la risposta del tutor amministrativo", 
          "confermato", risposte.get("mario"));
    System.out.println("corrispondenza verificata");

    System.out.println("Verifica risposta agt = 'rifiutato'");
    assertEquals("Non c'è corrispondenza con la risposta dell'agt", 
         "rifiutato", risposte.get("AGT"));
    System.out.println("corrispondenza verificata");

    System.out.println("verifica che il tipo di richiesta sia 'RichiestaTirocinio'");
    assertEquals("RichiestaTirocinio", richiesta.getTipoRichiesta());
    System.out.println("corrispondenza verificata");

    System.out.println("Test metodo get con valori validi terminato"
        + " senza fallimenti");
  }

  /**
  * Elimina la richiesta usata per il testing.
  * @throws SQLException in caso di mancata connessione con il database.
  */
  @AfterClass
  public static void terminate() throws SQLException {
    eliminaRichiesta();
  }

  private Richiesta richiesta = null;
}
