package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;
import sigma.training.ctp.dto.WalletRestDto;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class WalletService {

  public static final String DELIMITER = ": ";

  @Autowired
  private WalletRepository repository;


  @Value("${bankcurrency.name}")
  private String bankCurrencyName;
  @Value("${cryptocurrency.name}")
  private String cryptocurrencyName;
  @Value("${cryptocurrency.sign}")
  private String cryptocurrencySign;

  public WalletRestDto getWalletByUserId(Long id) {
    WalletEntity wallet = repository.findWalletEntityByUserId(id);

    String moneyBalance = bankCurrencyName
      .concat(DELIMITER)
      .concat(wallet.getMoneyBalance().toString());

    String cryptocurrencyBalance = cryptocurrencyName
      .concat(DELIMITER)
      .concat(wallet.getCryptocurrencyBalance().toString())
      .concat(cryptocurrencySign);

    return new WalletRestDto(moneyBalance, cryptocurrencyBalance);
  }

  public WalletEntity reduceWalletCryptocurrencyBalanceByUserId(Long id, BigDecimal cryptocurrencyAmount) throws InsufficientAmountCryptoException {
    WalletEntity wallet = repository.findWalletEntityByUserId(id);
    BigDecimal cryptocurrencyBalance = wallet.getCryptocurrencyBalance();
    if (cryptocurrencyBalance.compareTo(cryptocurrencyAmount) == -1) {
      throw new InsufficientAmountCryptoException();
    }
    wallet.setCryptocurrencyBalance(cryptocurrencyBalance.subtract(cryptocurrencyAmount));
    return repository.save(wallet);
  }
}
