package sigma.training.ctp.view;

import io.swagger.v3.oas.annotations.media.Schema;
import sigma.training.ctp.enums.Status;

import java.math.BigDecimal;

@Schema(description = "order from the body")
public class OrderDetailsViewModel {

  private OrderDetailsViewModel() {
  }

  @Schema(description = "status of the order", enumAsRef = true, example = "CREATED")
  private Status status;

  @Schema(description = "price of the cryptocurrency", example = "400.00")
  private BigDecimal cryptocurrencyPrice;

  @Schema(description = "amount of the cryptocurrency", example = "20.00")
  private BigDecimal cryptocurrencyAmount;
}
