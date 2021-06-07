package sigma.training.ctp.exception;

public class CannotFulfillOwnOrderException extends Exception {
  public CannotFulfillOwnOrderException() {
    super("You are not able to fulfill own order");
  }
}
