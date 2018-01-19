package modeltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import databaseconnector.DriverManagerConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Azienda;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AziendaTest {

  Azienda azienda = null;

  /**
   * Crea una azienda e la rende persistente nel database
   * per il testing.
   * 
   * @throws SQLException in caso di mancata connessione con il database
   */
  @BeforeClass
  public static void createAzienda() throws SQLException {
    System.out.println("Inizio test classe Azienda");
    System.out.println("");
    Connection connection = null;
    try {
      // ottengo la connessione verso il database
      connection = DriverManagerConnectionPool.getConnection();
      // elimino eventuale azienda con lo stesso nome già inserito nel database
      String sql = "delete from Azienda where nome = 'Healthware'; ";
      PreparedStatement stm = connection.prepareStatement(sql);
      stm.executeUpdate();
      connection.commit();

      //inserisco la nuova tupla
      sql = "INSERT INTO `ssgt`.`azienda` (`nome`, `sede`, `tutor_amministrativo`, "
            + "`tutor_aziendale`, `recapito_tutor_amministrativo`, "
            + "`recapito_tutor_aziendale`) VALUES ('Healthware', 'Avellino', 'Esposito', "
            + "'Giuliano', '324234241324', '3241421432314');\r\n";
      stm = connection.prepareStatement(sql);
      stm.executeUpdate();
      connection.commit();
    } finally {
      connection.close();
    }
  }

  /**
  * Classe di equivalenza: nome azienda esistente.
  * Verifica che la chiamata a metodo get passando come parametro 
  * il nome di una azienda registrata nel sistema
  * restituisca correttamente l'istanza relativa.
  * 
  * @throws SQLException in caso di mancata connessione con il database
  */
  @Test
  public void getAziendaEsistente() throws SQLException {
    System.out.println("");
    System.out.println("Inizio test su metodo get passando come"
         + " parametro il nome di una azienda esistente");
    System.out.println("Ottengo una istanza della classe Azienda con nome 'Healthware'");
    azienda = Azienda.get("Healthware");

    System.out.println("Verifica nome = 'Healthware'");
    assertEquals("Non c'è corrispondenza con il nome", 
          "Healthware", azienda.getNome());
    System.out.println("corrispondenza verificata");
  
    System.out.println("Verifica sede = 'Avellino'");
    assertEquals("Non c'è corrispondenza con la sede",
          "Avellino", azienda.getSede());
    System.out.println("corrispondenza verificata");

    System.out.println("verifica tutorAmministrativo = 'Esposito'");
    assertEquals("Non c'è corrispondenza con il tutor amministrativo",
          "Esposito", azienda.getTutorAmministrativo());
    System.out.println("corrispondenza verificata");

    System.out.println("verifica tutorAziendale = 'Giuliano'");
    assertEquals("Non c'è corrispondenza con il tutor aziendale", 
          "Giuliano", azienda.getTutorAziendale());
    System.out.println("corrispondenza verificata");

    System.out.println("verifica recapitoTutorAmministrativo = '324234241324'");
    assertEquals("Non c'è corrispondenza con il recapito del tutor amminsitrativo",
         "324234241324", azienda.getRecapitoTutorAmministrativo());
    System.out.println("corrispondenza verificata");

    System.out.println("verifica recapitoTutorAziendale = '3241421432314'");
    assertEquals("Non c'è corrispondenza con il recapito del tutor aziendale",
         "3241421432314", azienda.getRecapitoTutorAziendale());
    System.out.println("corrispondenza verificata");

    System.out.println("Test per metodo get su azienda esistente terminato senza fallimenti");
  }
  
  /**
  * Classe di equivalenza: nome azienda inesistente.
  * Verifica che la chiamata a metodo get passando  come parametro
  * il nome di una azienda
  * non esistente nel sistema non generi una eccezione e ritorni
  * correttamente il valore nullo.
  * 
  * @throws SQLException in caso di mancata connessione con il database
  */
  @Test
  public void getAziendaInesistente() throws SQLException {
    System.out.println("");
    System.out.println("Inizio test su metodo get passando come"
        + " parametro il nome di una azienda non esistente"
        + " nel sistema");
    System.out.println("chiamo il metodo passando come nome la stringa 'MDB'");
    azienda = Azienda.get("MDB");
    assertNull("Il valore restituito non è nullo", azienda);
    System.out.println("Test per metodo get su azienda inesistente terminato senza fallimenti");
  }

  /**
   * Stampa su console che il test è terminato.
   */
  @AfterClass
  public static void terminate() {
    System.out.println("Test della classe Azienda terminato");
    System.out.println("");
    System.out.println("");
  }
}
