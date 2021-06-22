package sigma.training.ctp.exception;

public class WalletNotFoundException extends Exception {
  public WalletNotFoundException(Long id) {
    super("Wallet with id= " + id + " not found");
  }
  public WalletNotFoundException(String name) {
    super("Wallet with currency= " + name + " not found");
  }
}
