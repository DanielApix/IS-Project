/** @author Danilo Apicella **/

package model;

public class DestinatarioRichiestaNonEsistente extends Exception {

  private static final long serialVersionUID = 1L;

  public DestinatarioRichiestaNonEsistente(String message) {
    super(message);
  }

  public DestinatarioRichiestaNonEsistente() {
    super("non esiste nessuna richiesta in sospeso che abbia questo destinatario");
  }
}
