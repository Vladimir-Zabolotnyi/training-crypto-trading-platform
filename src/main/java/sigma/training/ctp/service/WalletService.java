package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;

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

  public WalletRestDto reduceWalletCryptocurrencyBalanceByUserId(Long id, BigDecimal cryptocurrencyAmount) {
    WalletEntity walletBeforeUpdate = repository.findWalletEntityByUserId(id);
    BigDecimal cryptocurrencyBalanceBeforeUpdate = walletBeforeUpdate.getCryptocurrencyBalance();

    if (cryptocurrencyBalanceBeforeUpdate.compareTo(cryptocurrencyAmount) != -1) {
      BigDecimal cryptocurrencyBalanceAfterUpdate = cryptocurrencyBalanceBeforeUpdate.subtract(cryptocurrencyAmount);
      WalletEntity walletAfterUpdate = repository.updateWalletEntityCryptocurrencyBalanceByUserId(id, cryptocurrencyBalanceAfterUpdate);

      return new WalletRestDto(
        walletAfterUpdate.getMoneyBalance(),
        walletAfterUpdate.getCryptocurrencyBalance()
      );
    } else throw new IllegalArgumentException("not enough crypto");
  }
}
