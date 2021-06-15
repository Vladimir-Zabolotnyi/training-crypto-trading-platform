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

  @Schema(description = "type of the order", enumAsRef = true,example = "sell")
  private String orderType;

  @Schema(description = "id of the user", example = "1")
  private Long userId;
}
