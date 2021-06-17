package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FeeService {

  private final BigDecimal rate;
  private final BigDecimal minimalValue;

  public FeeService(
    @Value(value = "${fee.rate}") String rate,
    @Value(value = "${fee.minimal-value}") String minimalValue) {
    this.rate = new BigDecimal(rate);
    this.minimalValue = new BigDecimal(minimalValue);
  }

  public BigDecimal getOrderFee(BigDecimal orderTotal) {
    return (orderTotal.multiply(rate).compareTo(minimalValue) > 0)
      ? orderTotal.multiply(rate)
      : minimalValue;
  }
}
