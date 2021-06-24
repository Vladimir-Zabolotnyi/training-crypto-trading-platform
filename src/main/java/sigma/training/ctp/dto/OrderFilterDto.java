package sigma.training.ctp.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "params for order filter")
public class OrderFilterDto {

  @Schema(description = "status of the order", enumAsRef = true, example = "created")
  private String orderStatus;

  @Schema(description = "id of the user", example = "1")
  private Long userId;

  @Schema(description = "name of the currency to sell", example = "USA_dollar")
  private String sellCurrencyName;

  @Schema(description = "name of the currency to buy", example = "Bitcoin")
  private String buyCurrencyName;

}
