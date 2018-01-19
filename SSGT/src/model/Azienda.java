/** @author Danilo Apicella **/

package model;

import databaseconnector.DriverManagerConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Azienda {
  /**
  * Costruttore utilizzato esclusivamente dal metodo get.
  * 
  * @param nome dell'azienda
  * @param sede dell'azienda
  * @param tutorAmministrativo username dell'account del tutor amministrativo associato all'azienda
  * @param tutorAziendale username dell'account del tutor aziendale referente dell'azienda
  * @param recapitoTutorAmministrativo indirizzo di posta elettronica del tutor amministrativo
  * @param recapitoTutorAziendale indirizzo di posta elettronica del tutor aziendale
  */
  private Azienda(String nome, String sede, String tutorAmministrativo, String tutorAziendale, 
                 String recapitoTutorAmministrativo, String recapitoTutorAziendale) {
    this.nome = nome;
    this.sede = sede;
    this.tutorAmministrativo = tutorAmministrativo;
    this.tutorAziendale = tutorAziendale;
    this.recapitoTutorAmministrativo = recapitoTutorAmministrativo;
    this.recapitoTutorAziendale = recapitoTutorAziendale;
  }

  public String getNome() {
    return nome;
  }
  
  public String getSede() {
    return sede;
  }

  /** Ritorna l'username. **/
  public String getTutorAmministrativo() {
    return tutorAmministrativo;
  }

  /** Ritorna l'username. **/
  public String getTutorAziendale() {
    return tutorAziendale;
  }

  /** Ritorna l'indirizzo mail. **/
  public String getRecapitoTutorAmministrativo() {
    return recapitoTutorAmministrativo;
  }

  /** Ritorna l'indirizzo mail. **/
  public String getRecapitoTutorAziendale() {
    return recapitoTutorAziendale;
  }

  /**
  * Ritorna una istanza precedentemente memorizzata della classe azienda che ha quel nome.
  * @param nome dell'azienda
  * @return Azienda che ha quel nome, se esiste, altrimenti null
  * @throws SQLException in caso di mancata connessione con il database
  */
  public static Azienda get(String nome) throws SQLException {

    Connection connection = null;
    connection = DriverManagerConnectionPool.getConnection();

    String sql = "select * from Azienda where nome = ?";
    PreparedStatement stm = connection.prepareStatement(sql);
    stm.setString(1, nome);
    ResultSet result = stm.executeQuery();
    if (result.next() == false) {
      return null;
    }
    return new Azienda(result.getString("nome"), result.getString("sede"), 
         result.getString("tutor_amministrativo"),
         result.getString("tutor_aziendale"), result.getString("recapito_tutor_amministrativo"),
         result.getString("recapito_tutor_aziendale"));
  }

  private final String nome;
  private final String sede;
  private final String tutorAmministrativo;
  private final String tutorAziendale;
  private final String recapitoTutorAmministrativo;
  private final String recapitoTutorAziendale;
}
