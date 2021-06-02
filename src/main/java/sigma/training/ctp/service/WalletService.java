package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class WalletService {

  @Autowired
  private WalletRepository repository;


  public WalletRestDto getWalletByUserId(Long id) {
    WalletEntity wallet = repository.findWalletEntityByUserId(id);

    return new WalletRestDto(
      wallet.getMoneyBalance(),
      wallet.getCryptocurrencyBalance()
    );
  }

  public WalletEntity reduceWalletCryptocurrencyBalanceByUserId(Long id, BigDecimal cryptocurrencyAmount) throws InsufficientAmountCryptoException {
    WalletEntity walletBeforeUpdate = repository.findWalletEntityByUserId(id);
    BigDecimal cryptocurrencyBalanceBeforeUpdate = walletBeforeUpdate.getCryptocurrencyBalance();

    if (cryptocurrencyBalanceBeforeUpdate.compareTo(cryptocurrencyAmount) != -1) {
      BigDecimal cryptocurrencyBalanceAfterUpdate = cryptocurrencyBalanceBeforeUpdate.subtract(cryptocurrencyAmount);
      walletBeforeUpdate.setCryptocurrencyBalance(cryptocurrencyBalanceAfterUpdate);
      WalletEntity walletAfterUpdate = repository.save(walletBeforeUpdate);
      return walletAfterUpdate;
    } else throw new InsufficientAmountCryptoException();
  }
}
