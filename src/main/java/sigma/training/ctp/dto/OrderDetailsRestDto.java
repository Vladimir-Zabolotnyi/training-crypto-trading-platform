package sigma.training.ctp.dto;



import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.enums.OrderType;
import sigma.training.ctp.persistence.entity.enums.Status;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@Schema(description = "Details of the order")
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

  @Schema(description = "id of the order", example = "1")
  private Long id;

  @Schema(description = "creation date of the order", example = "2020-01-01 10:10:10")
  private Instant creationDate;

  @Schema(description = "id of the user", example = "1")
  private Long userId;

  @Schema(description = "status of the order", enumAsRef = true,example = "created")
  private Status status;

  @Schema(description = "type of the order", enumAsRef = true,example = "sell")
  private OrderType orderType;

  @Schema(description = "price of the cryptocurrency", example = "400.00")
  private BigDecimal cryptocurrencyPrice;

  @Schema(description = "amount of the cryptocurrency", example = "20.00")
  private BigDecimal cryptocurrencyAmount;
}
