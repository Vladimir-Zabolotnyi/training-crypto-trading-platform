package sigma.training.ctp.persistence.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Security data of the user")
public class UserDto {
    @Schema(description = "id of the user",example = "1")
    private Long id;
    @Schema(description = "login of the user",example = "Vova")
    private String login;
}
