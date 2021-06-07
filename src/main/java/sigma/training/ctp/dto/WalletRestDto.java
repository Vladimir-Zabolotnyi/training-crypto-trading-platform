package sigma.training.ctp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Schema(
  name = "The wallet balance model",
  description = "A model for displaying the balance state of the user"
)
@Data
public class WalletRestDto {

  @Schema(
    name = "Money balance",
    description = "The amount of the real money",
    example = "13.37; 228.00; 17.0; 441",
    anyOf = String.class
  )
  @NonNull
  private String moneyBalance;

  @Schema(
    name = "Cryptocurrency balance",
    description = "The cryptocurrency balance",
    example = "76.46; 78.00; 77.0; 79",
    anyOf = String.class
  )
  @NonNull
  private String cryptocurrencyBalance;
}
