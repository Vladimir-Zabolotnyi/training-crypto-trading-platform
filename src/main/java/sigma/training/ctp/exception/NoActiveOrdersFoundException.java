package sigma.training.ctp.exception;

public class NoActiveOrdersFoundException extends Exception{
  public NoActiveOrdersFoundException() {
    super("No active order were found");
  }
}
