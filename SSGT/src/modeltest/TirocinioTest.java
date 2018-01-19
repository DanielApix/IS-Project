/** @author Danilo Apicella **/

package modeltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import databaseconnector.DriverManagerConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Tirocinio;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class TirocinioTest {

  /** Elimina le tuple che presentano esattamente gli stessi dati
  * inseriti durante il test.
  * @throws SQLException in caso di mancata connessione con il database
  */
  @BeforeClass
  public static void inizializza() throws SQLException {
    System.out.println("Inizio test classe Account");
    System.out.println("");

    rimuoviTirocinio();
  }

  /**
  * Rimuove il tirocinio eventualmente aggiunto nel database per il testing. 
  * @throws SQLException in caso di mancata connessione con il database
  */
  public static void rimuoviTirocinio() throws SQLException {
    Connection connection = null;
    try {
      // ottengo la connessione verso il database
      connection = DriverManagerConnectionPool.getConnection();

      /* elimino eventuale tirocinio con gli stessi dati 
         già inserito nel database */
      String sql = "DELETE FROM Tirocinio WHERE studente = 'danielapix' "
           + "and tutorAmministrativo = 'giuseppe' and tutorAziendale = 'mario' "
           + "and azienda = 'healthware';";
      PreparedStatement stm = connection.prepareStatement(sql);
      stm.executeUpdate();
      connection.commit();
    } finally {
      connection.close();
    }
  }

  /**
  * Aggiunge un tirocinio al database per il testing.
  * 
  * @throws SQLException in caso di mancata connessione con il database. 
  */
  public static void aggiungiTirocinio() throws SQLException {
    Connection connection = null;
    try {
      // ottengo la connessione verso il database
      connection = DriverManagerConnectionPool.getConnection();

      /* elimino eventuale tirocinio con gli stessi dati 
         già inserito nel database */
      String sql = "INSERT INTO `ssgt`.`Tirocinio` (`studente`, `tutorAmministrativo`, "
            + "`tutorAziendale`, `azienda`) VALUES "
            + "('danielapix', 'giuseppe', 'mario', 'healthware');\r\n";
      PreparedStatement stm = connection.prepareStatement(sql);
      stm.executeUpdate();
      connection.commit();
    } finally {
      connection.close();
    }
  }

  /**
  * Classe di equivalenza: tirocinio esistente.
  * Testa il metodo get passando il nome di uno studente a cui è 
  * associato il tirocinio inserito per verificarne l'ottenimento
  *
  * @throws SQLException in caso di mancata connessione con il database
  */
  @Test
  public void getTirocinioEsistente() throws SQLException {
    System.out.println("");
    System.out.println("Inizio test per metodo get con valori ammissibili");
    rimuoviTirocinio();
    aggiungiTirocinio();
    ArrayList<Tirocinio> tirocini = Tirocinio.getTirocini("danielapix");
    boolean tirocinioTrovato = false;
    for (Tirocinio x : tirocini) {
      tirocinioTrovato = x.getStudente().equals("danielapix")
            && x.getTutorAmministrativo().equals("giuseppe")
            && x.getTutorAziendale().equals("mario")
            && x.getAzienda().equals("healthware");
      if (tirocinioTrovato) {
        break;
      }
    }
    assertEquals("Fallimento: non è stato trovato il tirocinio"
          + "aggiunto", true, tirocinioTrovato);
    System.out.println("Test del metodo get con valori ammissimili"
          + " terminato senza fallimenti");
    rimuoviTirocinio();
  }
  
  /**
  * Classe di equivalenza: parametro nullo.
  * Testa il metodo get passando un parametro nullo e verificando
  * che lanci una eccezione.
  * 
  * @throws SQLException in caso di mancata connessione con il database
  */
  @Test
  public void getValoreNullo() throws SQLException {
    System.out.println("");
    System.out.println("Inizio test per metodo get con valore nullo");
    boolean eccezioneLanciata = false;
    try {
      Tirocinio.getTirocini(null);
    } catch (Exception e) {
      eccezioneLanciata = true;
      System.out.println("Il metodo ha lanciato correttamente una eccezione");
    }
    assertEquals("Non è stata lanciata l'eccezione come avrebbe dovuto",
          true, eccezioneLanciata);
    System.out.println("Test per metodo get con valore nullo terminato"
          + " senza fallimenti");
  }

  /**
  * Classe di equivalenza: valori ammissibili.
  * Verifica l'effetiva creazione di un tirocinio dopo la chiamata
  * al metodo create.
  *
  * @throws SQLException in caso di mancata connessione con il database
  */
  @Test
  public void createValoriAmmissibili() throws SQLException {
    rimuoviTirocinio();
    System.out.println("");
    System.out.println("Inizio test per metodo create passando valori ammissibili");
    System.out.println("aggiunta di un nuovo tirocinio con i seguenti dati");
    System.out.println("studente = 'danielapix'");
    String studente = "danielapix";
    System.out.println("tutorAmministrativo = 'giuseppe'");
    String tutorAmministrativo = "giuseppe";
    System.out.println("tutorAziendale = 'mario'");
    String tutorAziendale = "mario";
    System.out.println("azienda = 'healthware'");
    String azienda = "healthwre";

    System.out.println("creazione tirocinio");
    Tirocinio.create(studente, tutorAmministrativo, tutorAziendale,
           azienda);

    Connection connection = null;

    // ottengo la connessione verso il database
    connection = DriverManagerConnectionPool.getConnection();

    // interrogo il database per acquisire il tirocinio aggiunto
    String sql = "select * from Tirocinio where "
          + "studente = 'danielapix' and tutorAmministrativo = 'giuseppe' "
          + "and tutorAziendale = 'mario' and azienda = 'healthware';";
    PreparedStatement stm = connection.prepareStatement(sql);
    ResultSet result = stm.executeQuery();
    result.next();

    assertNotNull("Non è stata eseguita correttamente la creazione: "
          + "tupla non esistente", result);

    System.out.println("Test per metodo create su Account inesistente terminato senza fallimenti");
    rimuoviTirocinio();
  }


  /**
  * Classe di equivalenza: un parametro null.
  * Verifica che non venga aggiunta una tupla con un attributo senza valore.
  * 
  * @throws SQLException in caso di mancata connessione con il database
  */
  @Test
  public void createParametroNullo() throws SQLException {
    System.out.println("");
    System.out.println("Inizio test per metodo create passando un valore nullo");
    System.out.println("aggiunta di un nuovo tirocinio con i seguenti dati");
    System.out.println("studente = null");
    String studente = null;
    System.out.println("tutorAmministrativo = 'giuseppe'");
    String tutorAmministrativo = "giuseppe";
    System.out.println("tutorAziendale = 'mario'");
    String tutorAziendale = "mario";
    System.out.println("azienda = 'healthware'");
    String azienda = "healthwre";

    boolean eccezioneLanciata = false;
    System.out.println("creazione tirocinio");
    try {
      Tirocinio.create(studente, tutorAmministrativo, tutorAziendale,
            azienda);
    } catch (Exception e) {
      eccezioneLanciata = true;
      System.out.println("eccezione correttamente lanciata");
    }
    assertEquals("Non è stata lanciata l'eccezione come avrebbe dovuto",
        true, eccezioneLanciata);
    System.out.println("Test per metogo create con parametro nulla terminato "
          + "senza fallimenti");
  }

  /**
  * Elimina il tirocinio aggiunto nel database per il testing.
  *  
  * @throws SQLException in caso di mancata connessione con il database
  */
  @AfterClass
  public static void terminate() throws SQLException {
    rimuoviTirocinio();
    System.out.println("Test della classe Tirocinio terminato");
  }
}
