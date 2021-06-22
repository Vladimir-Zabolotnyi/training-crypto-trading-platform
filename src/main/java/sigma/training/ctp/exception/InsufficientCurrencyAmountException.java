package sigma.training.ctp.exception;

public class InsufficientCurrencyAmountException extends Exception {
  public InsufficientCurrencyAmountException() {
    super("Insufficient amount of currency in the wallet");
  }
}
