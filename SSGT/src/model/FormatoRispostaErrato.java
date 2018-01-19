/** @author Danilo Apicella **/

package model;

public class FormatoRispostaErrato extends Exception {

  public FormatoRispostaErrato(String message) {
    super(message);
  }

  public FormatoRispostaErrato() {
    super("Formato risposta errato: il valore può essere solo di tre tipi"
        + ", confermato, rifiuato e nonRicevuto");
  }

  private static final long serialVersionUID = 1L;
}

