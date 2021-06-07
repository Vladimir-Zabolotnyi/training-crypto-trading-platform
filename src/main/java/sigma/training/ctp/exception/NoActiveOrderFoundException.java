package sigma.training.ctp.exception;

public class NoActiveOrderFoundException extends Exception{
  public NoActiveOrderFoundException() {
    super("No active order were found");
  }
}
