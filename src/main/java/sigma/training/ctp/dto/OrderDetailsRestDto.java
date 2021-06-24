package sigma.training.ctp.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import sigma.training.ctp.dictionary.OrderStatus;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Details of the order")
public class OrderDetailsRestDto {

  @Schema(description = "id of the order", example = "1")
  private Long id;

  @Schema(description = "creation date of the order", example = "2020-01-01 10:10:10")
  private Instant creationDate;

  @Schema(description = "id of the user", example = "1")
  private Long userId;

  @Schema(description = "status of the order", enumAsRef = true, example = "created")
  private OrderStatus orderStatus;

  @Schema(description = "name of the currency to sell", example = "USA_dollar")
  private String sellCurrencyName;

  @Schema(description = "name of the currency to buy", example = "Bitcoin")
  private String buyCurrencyName;

  @Schema(description = "amount of the currency to sell", example = "100.00")
  @DecimalMin(value = "0.00")
  private BigDecimal sellCurrencyAmount;

  @Schema(description = "amount of the currency to buy", example = "20.00")
  @DecimalMin(value = "0.00")
  private BigDecimal buyCurrencyAmount;
}
