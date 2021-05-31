package sigma.training.ctp.rest_dto;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class WalletBalanceRestDTO {
    @NonNull
    private BigDecimal moneyBalance;
    @NonNull
    private BigDecimal cryptocurrencyBalance;
}
