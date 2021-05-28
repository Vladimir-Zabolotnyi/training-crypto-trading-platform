package sigma.training.ctp.model;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class UserWalletBalanceViewModel {
    @NonNull
    private BigDecimal moneyBalance;
    @NonNull
    private BigDecimal cryptocurrencyBalance;
}
