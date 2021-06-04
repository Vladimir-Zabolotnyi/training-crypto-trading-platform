package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.exception.InsufficientAmountBankCurrencyException;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.mapper.WalletMapper;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;

import java.math.BigDecimal;

@Service
public class WalletService {

  @Autowired
  private WalletRepository repository;
  @Autowired
  WalletMapper walletMapper;

  public WalletRestDto getWalletByUserId(Long id) {
    WalletEntity wallet = repository.findWalletEntityByUserId(id);
    return walletMapper.toRestDto(wallet);
  }

  public WalletEntity reduceWalletCryptocurrencyBalanceByUserId(Long id, BigDecimal cryptocurrencyAmount) throws InsufficientAmountCryptoException {
    WalletEntity wallet = repository.findWalletEntityByUserId(id);
    BigDecimal cryptocurrencyBalance = wallet.getCryptocurrencyBalance();
    if (cryptocurrencyBalance.compareTo(cryptocurrencyAmount) < 0) {
      throw new InsufficientAmountCryptoException();
    }
    wallet.setCryptocurrencyBalance(cryptocurrencyBalance.subtract(cryptocurrencyAmount));
    return repository.save(wallet);
  }

  public boolean purchaseCryptocurrency(Long user_buy_id, Long user_sell_id, BigDecimal cryptocurrencyAmount, BigDecimal cryptocurrencyPrice) throws InsufficientAmountBankCurrencyException {
    subtractWalletMoneyBalanceByUserId(user_buy_id, cryptocurrencyAmount, cryptocurrencyPrice);
    addWalletMoneyBalanceByUserId(user_sell_id, cryptocurrencyAmount, cryptocurrencyPrice);
    addWalletCryptocurrencyBalanceByUserId(user_buy_id, cryptocurrencyAmount);
    return true;
  }

  public WalletEntity subtractWalletMoneyBalanceByUserId(Long id, BigDecimal cryptocurrencyAmount, BigDecimal cryptocurrencyPrice) throws InsufficientAmountBankCurrencyException {
    WalletEntity wallet = repository.findWalletEntityByUserId(id);
    BigDecimal moneyBalance = wallet.getMoneyBalance();
    if (moneyBalance.compareTo(cryptocurrencyAmount.multiply(cryptocurrencyPrice)) < 0) {
      throw new InsufficientAmountBankCurrencyException();
  }
    wallet.setMoneyBalance(moneyBalance.subtract(cryptocurrencyAmount.multiply(cryptocurrencyPrice)));
    return repository.save(wallet);
  }

  public WalletEntity addWalletMoneyBalanceByUserId(Long id, BigDecimal cryptocurrencyAmount, BigDecimal cryptocurrencyPrice) {
    WalletEntity wallet = repository.findWalletEntityByUserId(id);
    BigDecimal moneyBalance = wallet.getMoneyBalance();
    wallet.setMoneyBalance(moneyBalance.add(cryptocurrencyAmount.multiply(cryptocurrencyPrice)));
    return repository.save(wallet);
  }

  public WalletEntity addWalletCryptocurrencyBalanceByUserId(Long id, BigDecimal cryptocurrencyAmount) {
    WalletEntity wallet = repository.findWalletEntityByUserId(id);
    BigDecimal cryptocurrencyBalance = wallet.getCryptocurrencyBalance();
    wallet.setCryptocurrencyBalance(cryptocurrencyBalance.add(cryptocurrencyAmount));
    return repository.save(wallet);
  }
}
