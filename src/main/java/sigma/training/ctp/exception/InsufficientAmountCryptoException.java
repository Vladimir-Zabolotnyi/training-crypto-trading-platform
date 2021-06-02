package sigma.training.ctp.exception;

public class InsufficientAmountCryptoException extends Exception {
  public InsufficientAmountCryptoException() {
    super("There is not enough cryptocurrency in the wallet");
  }
}
