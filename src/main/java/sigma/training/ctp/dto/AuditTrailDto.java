package sigma.training.ctp.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Audit trail")
public class AuditTrailDto {

  @Schema(description = "id of the audit trail", example = "1")
  private Long id;

  @Schema(description = "creation date of the audit trail", example = "2020-01-01 10:10:10")
  private Instant date;

  @Schema(description = "id of the user", example = "1")
  private Long userId;

  @Schema(description = "description of the action")
  private String description;


}
