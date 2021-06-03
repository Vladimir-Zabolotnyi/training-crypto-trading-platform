package sigma.training.ctp.exception;

public class InsufficientAmountCryptoException extends Exception {
  public InsufficientAmountCryptoException() {
    super("Insufficient amount of cryptocurrency in the wallet");
  }
}
