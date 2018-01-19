/** @author Danilo Apicella **/

package model;

import databaseconnector.DriverManagerConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

/** Richiesta riguardnate un tirocinio. **/
public class Richiesta {
  /**
  * Costruttore utilizzato solo dal metodo get.
  * @param mittente username dell'account che invia la richiesta
  * @param destinatari username degli account che ricevono la richiesta
  * @param tipoRichiesta tipo richiesta, ossia RichiestaConvalida o RichiestaTirocinio
  * @param risposteDestinatari Hashatable che ha come chiave l'username di chi 
  *     fornisce la risposta e come valore la risposta, vero o falso
  */
  private Richiesta(int id, String mittente, String[] destinatari, TipoRichiesta tipoRichiesta, 
      Hashtable<String, String> risposteDestinatari) {
    this.mittente = mittente;
    this.destinatari = destinatari;
    this.tipo = tipoRichiesta;
    this.risposteDestinatari = risposteDestinatari;
  }

  /**
  * Fornisce una lista di tutte le richieste rivolte ad un dato destinatario.
  * @param destinatario username
  * @return ArrayList delle richieste rivolte al destinatario
  * @throws SQLException in caso di mancata connessione
  */
  public static ArrayList<Richiesta> get(String destinatario) throws SQLException {
    Connection connection = DriverManagerConnectionPool.getConnection();
    String sql = "select * from ricevuto_da, richiesta where destinatario = ? "
         + "and richiesta.id = ricevuto_da.id; ";
    PreparedStatement stm = connection.prepareStatement(sql);
    stm.setString(1, destinatario);
    ResultSet result = stm.executeQuery();
    ArrayList<Richiesta> array = new ArrayList<Richiesta>();

    while (result.next()) {
      String[] destinatari = getDestinatari(connection, result.getInt("id"));
      Hashtable<String, String> risposteDestinatari = 
             getRisposteDestinatari(result.getInt("id"), connection);
      String tipoRichiesta = result.getString("tipo");
      Richiesta richiesta = new Richiesta(result.getInt("id"), 
            result.getString("mittente"), destinatari, 
            TipoRichiesta.valueOf(tipoRichiesta), risposteDestinatari);
      array.add(richiesta);
    }
    return array;
  }
  
  /**
  * Crea una instanza della classe Richiesta e lo rende persistente.
  * @param mittente username dell'account che invia la richiesta
  * @param destinatari username degli account che ricevono la richiesta
  * @param tipoRichiesta , ossia RichiestaConvalida o RichiestaTirocinio
  * @throws SQLException in caso di mancata connessione con il database
  */
  public static void create(String mittente, String[] destinatari, 
        TipoRichiesta tipoRichiesta) throws SQLException {

    Connection connection = DriverManagerConnectionPool.getConnection();
    String sql = "INSERT INTO `ssgt`.`Richiesta` (`tipo`, `mittente`) VALUES (?, ?);\r\n";
    PreparedStatement stm = connection.prepareStatement(sql);
    stm.setString(1, tipoRichiesta.toString());
    stm.setString(2, mittente);
    stm.executeUpdate();
    connection.commit();
    
    int id = getIdCorrente(connection);
    sql = "INSERT INTO `ssgt`.`ricevuto_da` (`id`, `mittente`, `destinatario`, `risposta`) "
           + "VALUES (?, ?, ?, 'nonRicevuto');\r\n";
    for (int i = 0; i < destinatari.length; i++) {
      stm = connection.prepareStatement(sql);
      stm.setInt(1, id);
      stm.setString(2, mittente);
      stm.setString(3, destinatari[i]);
      stm.executeUpdate();
      connection.commit();
    }
  }

  /**
  * Ritorna tutti i destinatari associati ad una richiesta.
  * @param connection utilizzato per interrogare il database
  * @return array di stringhe che contiene gli username dei destinatari
  * @throws SQLException in caso di mancata connessione
  */
  private static String[] getDestinatari(Connection connection, int id) throws SQLException {
    String sql = "select * from ricevuto_da where id = ?";
    PreparedStatement stm = connection.prepareStatement(sql);
    stm.setInt(1, id);
    ResultSet result = stm.executeQuery();
    ArrayList<String> destinatari = new ArrayList<String>();
    while (result.next()) {
      destinatari.add(result.getString("destinatario"));
    }
    String[] destinatariString = new String[destinatari.size()];
    for (int i = 0; i < destinatari.size(); i++) {
      destinatariString[i] = destinatari.get(i);
    }
    return destinatariString;
  }

  /**
  * Ritorna tutte le risposte dei destinatari.
  * @param connection utilizzato per interrogare il database
  * @return Hashtable che ha come chiave l'username di chi 
  *     fornisce la risposta e come valore la risposta, vero o falso
  * @throws SQLException in caso di mancata connessione
  */
  private static Hashtable<String, String> getRisposteDestinatari(int id, 
        Connection connection) throws SQLException {
    String sql = "select * from ricevuto_da where id = ?";
    PreparedStatement stm = connection.prepareStatement(sql);
    stm.setInt(1, id);
    ResultSet result = stm.executeQuery();
    Hashtable<String, String> risposteDestinatari = new Hashtable<String, String>();
    while (result.next()) {
      risposteDestinatari.put(result.getString("destinatario"), result.getString("risposta"));
    }
    return risposteDestinatari;
  }
  
  /**
  * Ritorna l'ultimo id registrato.
  * @param connection utilizzato per interrogare il database
  * @return id dell'ultima richiesta memorizzata
  * @throws SQLException in caso di mancata connessione
  */
  private static int getIdCorrente(Connection connection) throws SQLException {
    String sql = "select * from Richiesta Order by id DESC";
    PreparedStatement stm = connection.prepareStatement(sql);
    ResultSet result = stm.executeQuery();
    result.next();
    return result.getInt("id");
  }

  /**
   * Salva la risposta di conferma in maniera persistente. 
   * @param chiConferma username
   * @throws SQLException in caso di mancata connessione con il database
   * @throws DestinatarioRichiestaNonEsistente nel caso non esista al momento una richiesta
   *       in sospeso che abbia quel destinatario
   */
  public void conferma(String chiConferma) throws SQLException, DestinatarioRichiestaNonEsistente {
    Connection connection = DriverManagerConnectionPool.getConnection();
    String sql = "select * from ricevuto_da where id = " + id 
        + " and destinatario = " + chiConferma;
    PreparedStatement stm = connection.prepareStatement(sql);
    ResultSet result = stm.executeQuery();
    result.next();
    if (result == null) {
      throw new DestinatarioRichiestaNonEsistente();
    }
    sql = "UPDATE ricevuto_da\r\n" 
        + "SET\r\n" 
        + "  risposta = 'confermato',\r\n" 
        + "WHERE id = " + id;
    stm = connection.prepareStatement(sql);
    stm.executeQuery();
    connection.commit();
    risposteDestinatari.replace(chiConferma, "confermato");
  }
  
  /**
  * Salva la risposta di rifiuto in maniera persistente. 
  * @param chiRifiuta username
  * @throws SQLException in caso di mancata connessione con il database 
  * @throws DestinatarioRichiestaNonEsistente nel caso chi conferma 
  */
  public void rifiuta(String chiRifiuta) throws SQLException, DestinatarioRichiestaNonEsistente {
    Connection connection = DriverManagerConnectionPool.getConnection();
    String sql = "select * from ricevuto_da where id = " + id + " and destinatario = " + chiRifiuta;
    PreparedStatement stm = connection.prepareStatement(sql);
    ResultSet result = stm.executeQuery();
    result.next();
    if (result == null) {
      throw new DestinatarioRichiestaNonEsistente();
    }
    sql = "UPDATE ricevuto_da\r\n" 
        + "SET\r\n" 
        + "    risposta = 'rifiutato',\r\n" 
        + "WHERE id = " + id;
    stm = connection.prepareStatement(sql);
    stm.executeQuery();
    connection.commit();
    risposteDestinatari.replace(chiRifiuta, "rifiutato");
  }

  /** Ritorna l'identificativo con cui viene identificato nel database. **/
  public int getId() {
    return id;
  }

  public String getMittente() {
    return mittente;
  }

  /** Ritorna una lista di username dei destinatari. **/
  public String[] getDestinatari() {
    return (String[])destinatari.clone();
  }

  /**
  * Ritorna una collezione di risposte da parte dei destinatari con chiave
  * l'username del destinatario e valore il valore della risposta: 
  * confermato, rifiutato, non ricevuto.
  */
  public Hashtable<String, String> getRisposteDestinatari() {
    return (Hashtable<String, String>) risposteDestinatari;
  }

  /** Richiesta di tirocinio, richiesta di convalida ecc.. **/
  public String getTipoRichiesta() {
    return tipo.toString();
  }

  public enum TipoRichiesta {
    RichiestaConvalida, RichiestaTirocinio
  }

  private int id = 0;
  private final String mittente;
  private final String[] destinatari;
  private Hashtable<String, String> risposteDestinatari;
  private final TipoRichiesta tipo;
}
