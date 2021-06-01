package sigma.training.ctp.persistence.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderType {
  SELL("sell"),BUY("buy");

  private final String orderType;
}
