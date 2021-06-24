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

  @Schema(description = "name of the currency to sell", example = "USA_dollar")
  private String sellCurrencyName;

  @Schema(description = "name of the currency to buy", example = "Bitcoin")
  private String buyCurrencyName;

  @Schema(description = "amount of the currency to sell", example = "100.00")
  private BigDecimal sellCurrencyAmount;

  @Schema(description = "amount of the currency to buy", example = "20.00")
  private BigDecimal buyCurrencyAmount;
}
