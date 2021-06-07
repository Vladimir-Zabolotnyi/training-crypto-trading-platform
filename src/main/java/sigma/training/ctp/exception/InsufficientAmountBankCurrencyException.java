package sigma.training.ctp.exception;

public class InsufficientAmountBankCurrencyException extends Exception {
  public InsufficientAmountBankCurrencyException() {
    super("Insufficient amount of bank currency in the wallet");
  }
}
