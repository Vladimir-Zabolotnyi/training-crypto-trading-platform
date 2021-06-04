package sigma.training.ctp.exception;

public class OrderAlreadyCancelledException extends Exception {
  public OrderAlreadyCancelledException(Long id) {
    super("Order with id= " + id + " is already cancelled");
  }
}
