package sigma.training.ctp.dto;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class WalletRestDto {
  @NonNull
  private String moneyBalance;
  @NonNull
  private String cryptocurrencyBalance;
}
