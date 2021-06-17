package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FeeService {

  private final BigDecimal rate;
  private final BigDecimal minimal;

  public FeeService(
    @Value(value = "${fee.rate}") String rate,
    @Value(value = "${fee.minimal}") String minimal) {
    this.rate = new BigDecimal(rate);
    this.minimal = new BigDecimal(minimal);
  }

  public BigDecimal calculateFee(BigDecimal total) {
    BigDecimal percentage = total.multiply(rate);

    return (percentage.compareTo(minimal) > 0)
      ? percentage
      : minimal;
  }
}
