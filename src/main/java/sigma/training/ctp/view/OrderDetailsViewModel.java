package sigma.training.ctp.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Schema(description = "order from the body")
@Getter
@Setter
public class OrderDetailsViewModel {

  private OrderDetailsViewModel() {
  }
  @Schema(description = "price of the cryptocurrency", example = "400.00")
  private BigDecimal cryptocurrencyPrice;

  @Schema(description = "amount of the cryptocurrency", example = "20.00")
  private BigDecimal cryptocurrencyAmount;
}
