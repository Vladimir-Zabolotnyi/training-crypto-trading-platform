package sigma.training.ctp.persistence.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Security data of the user")
public class UserDto {
    @Schema(description = "id of the user",example = "1")
    private Long id;

    @NotBlank
    @Size(min = 0,max = 20)
    @Schema(description = "login of the user",example = "Vova")
    private String login;
}
