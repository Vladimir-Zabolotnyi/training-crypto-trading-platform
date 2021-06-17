package sigma.training.ctp.mapper;


import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.persistence.entity.WalletEntity;

import java.math.BigDecimal;

@Component
@Data
public class WalletMapper {
  public static final String SPACE = " ";

  @Value("${bankcurrency.name}")
  private String bankCurrencyName;
  @Value("${cryptocurrency.name}")
  private String cryptocurrencyName;
  @Value("${cryptocurrency.sign}")
  private String cryptocurrencySign;

  public WalletRestDto toRestDto(WalletEntity wallet) {
    String moneyBalance = bankCurrencyName
      .concat(SPACE)
      .concat(wallet.getMoneyBalance().toString());
    String cryptocurrencyBalance = cryptocurrencyName
      .concat(SPACE)
      .concat(wallet.getCryptocurrencyBalance().toString())
      .concat(SPACE)
      .concat(cryptocurrencySign);
    return new WalletRestDto(moneyBalance,cryptocurrencyBalance);
  }

  public WalletEntity toEntity(WalletRestDto walletDto) {
    WalletEntity wallet = new WalletEntity();
    wallet.setCryptocurrencyBalance(new BigDecimal(StringUtils.substringBetween(walletDto.getCryptocurrencyBalance(), SPACE, SPACE)));
    wallet.setMoneyBalance(new BigDecimal(walletDto.getMoneyBalance().split(SPACE)[1]));
    return wallet;
  }

}
