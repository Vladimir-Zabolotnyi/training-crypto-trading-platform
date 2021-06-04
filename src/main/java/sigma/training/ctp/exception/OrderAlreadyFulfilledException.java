package sigma.training.ctp.exception;

public class OrderAlreadyFulfilledException extends Exception {
  public OrderAlreadyFulfilledException(Long id) {
    super("Order with id= " + id + " is already fulfilled by another user");
  }
}
