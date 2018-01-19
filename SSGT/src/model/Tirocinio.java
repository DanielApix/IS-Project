/** @author Danilo Apicella **/

package model;

import databaseconnector.DriverManagerConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** Tirocinio svolto da uno studente presso una data azienda. **/
public class Tirocinio {

  /**
  * Costruttore utilizzato esclusivamente dal metodo get.
  * @param studente username a cui il tirocinio è riferito
  * @param tutorAmministrativo username che supervisiona il tirocinio
  * @param tutorAziendale username che supervisiona il tirocinio
  * @param azienda nome presso cui viene svolto il tirocinio
  */
  private Tirocinio(String studente, String tutorAmministrativo, 
          String tutorAziendale, String azienda) {
    this.studente = studente;
    this.tutorAmministrativo = tutorAmministrativo;
    this.tutorAziendale = tutorAziendale;
    this.azienda = azienda;
  }

  /**
  * Restituisce una lista di tirocini svolti da uno studente.
  * @param username dello studente
  * @return ArrayList di tirocini svolti dallo studente
  * @throws SQLException nel caso di fallimento nella connessione o nella interrogazione
  */
  public static ArrayList<Tirocinio> getTirocini(String username) throws SQLException {
    if (username == null) {
      throw new SQLException();
    }
    Connection connection = DriverManagerConnectionPool.getConnection();
    String sql = "select * from Tirocinio where studente = ?";
    PreparedStatement stm = connection.prepareStatement(sql);
    stm.setString(1, username);
    ResultSet result = stm.executeQuery();
    ArrayList<Tirocinio> array = new ArrayList<Tirocinio>();
    while (result.next()) {
      String studente = result.getString("studente");
      String tutorAmministrativo = result.getString("tutorAmministrativo");
      String tutorAziendale = result.getString("tutorAziendale");
      String azienda = result.getString("azienda");
      Tirocinio elm = new Tirocinio(studente, tutorAmministrativo, tutorAziendale, azienda);
      array.add(elm);
    }
    return array;
  }

  /**
  * Crea e rende persistente una istanza della classe Tirocinio.
  * @param studente username a cui il tirocinio è riferito
  * @param tutorAmministrativo username che supervisiona il tirocinio
  * @param tutorAziendale username che supervisiona il tirocinio
  * @param azienda nome presso cui viene svolto il tirocinio
  * @throws SQLException in caso di mancata connessione con il database
  */
  public static void create(String studente, String tutorAmministrativo, 
        String tutorAziendale, String azienda) throws SQLException {
    Connection connection = DriverManagerConnectionPool.getConnection();
    String sql = "INSERT INTO `ssgt`.`Tirocinio` (`studente`, `tutorAmministrativo`, "
         + "`tutorAziendale`, `azienda`) VALUES (?, ?, ?, ?);\r\n";
    PreparedStatement stm = connection.prepareStatement(sql);
    stm.setString(1, studente);
    stm.setString(2, tutorAmministrativo);
    stm.setString(3, tutorAziendale);
    stm.setString(4, azienda);
    stm.executeUpdate();
    connection.commit();
  }
  
  /** Ritorna l'username. **/
  public String getStudente() {
    return studente;
  }

  /** Ritorna l'username. **/
  public String getTutorAmministrativo() {
    return tutorAmministrativo;
  }

  /** Ritorna l'username. **/
  public String getTutorAziendale() {
    return tutorAziendale;
  }

  /** Ritorna il nome. **/
  public String getAzienda() {
    return azienda;
  }

  private final String studente;
  private final String tutorAmministrativo;
  private final String tutorAziendale;
  private final String azienda;
}
