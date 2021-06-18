package sigma.training.ctp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;


@Schema(
  name = "The wallet balance model of the defined currency",
  description = "A model for displaying the balance state of the user of the defined currency"
)
@Data
public class WalletRestDto {

  @Schema(
    name = "id of the wallet ",
    example = "1"
  )
  @NonNull
  private Long id;

  @Schema(
    name = "Currency name",
    example = "Bitcoin"
  )
  @NonNull
  private String currencyName;

  @Schema(
    name = "Currency acronym",
    example = "BTC"
  )
  @NonNull
  private String currencyAcronym;

  @Schema(
    name = "Currency amount",
    example = "100"
  )
  @NonNull
  private BigDecimal amount;
}
