package sigma.training.ctp.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.enums.OrderType;
import sigma.training.ctp.persistence.entity.enums.Status;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
public class OrderDetailsRestDto {

  public OrderDetailsRestDto(OrderDetailsEntity order) {
    this.id = order.getId();
    this.creationDate = order.getCreationDate();
    this.userId = order.getUser().getId();
    this.status = order.getStatus();
    this.orderType = order.getOrderType();
    this.cryptocurrencyPrice = order.getCryptocurrencyPrice();
    this.cryptocurrencyAmount = order.getCryptocurrencyAmount();
  }

  private Long id;

  private Instant creationDate;

  private Long userId;

  private Status status;

  private OrderType orderType;

  private BigDecimal cryptocurrencyPrice;

  private BigDecimal cryptocurrencyAmount;
}
