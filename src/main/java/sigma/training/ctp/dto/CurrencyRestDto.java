package sigma.training.ctp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Schema(description = "A model for the currency displaying")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRestDto {

  @Schema(description = "An identifier for the currency", example = "1")
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @Schema(description = "The currency name", example = "Bitcoin")
  @Column(name = "name")
  private String name;

  @Schema(description = "A currency's acronym or short name", example = "BIT")
  @Column(name = "acronym")
  private String acronym;
}
