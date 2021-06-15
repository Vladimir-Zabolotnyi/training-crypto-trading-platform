package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.exception.InsufficientAmountBankCurrencyException;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.mapper.WalletMapper;
import sigma.training.ctp.persistence.entity.AuditTrail;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.AuditTrailRepository;
import sigma.training.ctp.persistence.repository.WalletRepository;

import java.math.BigDecimal;

@Service
public class WalletService {

  @Autowired
  private WalletRepository repository;

  @Autowired
  WalletMapper walletMapper;

  @Autowired
  AuditTrailRepository auditTrailRepository;

  @Autowired
  UserService userService;

  public WalletRestDto getWalletByUserId(Long id) {
    AuditTrail auditTrail = new AuditTrail();
    WalletEntity wallet = repository.findWalletEntityByUserId(id);

    auditTrail.setUser(userService.getCurrentUser());
    auditTrail.setDescription("Wallet wit id= " + wallet.getId() + " was got");
    auditTrailRepository.save(auditTrail);
    return walletMapper.toRestDto(wallet);
  }

  public WalletEntity subtractWalletCryptocurrencyBalanceByUserId(Long id, BigDecimal cryptocurrencyAmount) throws InsufficientAmountCryptoException {
    AuditTrail auditTrail = new AuditTrail();
    WalletEntity wallet = repository.findWalletEntityByUserId(id);
    BigDecimal cryptocurrencyBalance = wallet.getCryptocurrencyBalance();
    if (cryptocurrencyBalance.compareTo(cryptocurrencyAmount) < 0) {
      throw new InsufficientAmountCryptoException();
    }
    wallet.setCryptocurrencyBalance(cryptocurrencyBalance.subtract(cryptocurrencyAmount));
    auditTrail.setUser(wallet.getUser());
    auditTrail.setDescription("Cryptocurrency balance was subtracted " + cryptocurrencyAmount + " in the wallet with id= " + wallet.getId());
    auditTrailRepository.save(auditTrail);
    return repository.save(wallet);
  }

  public WalletEntity subtractWalletMoneyBalanceByUserId(Long id, BigDecimal cryptocurrencyAmount, BigDecimal cryptocurrencyPrice) throws InsufficientAmountBankCurrencyException {
    AuditTrail auditTrail = new AuditTrail();
    WalletEntity wallet = repository.findWalletEntityByUserId(id);
    BigDecimal moneyBalance = wallet.getMoneyBalance();
    if (moneyBalance.compareTo(cryptocurrencyAmount.multiply(cryptocurrencyPrice)) < 0) {
      throw new InsufficientAmountBankCurrencyException();
    }
    wallet.setMoneyBalance(moneyBalance.subtract(cryptocurrencyAmount.multiply(cryptocurrencyPrice)));
    auditTrail.setUser(wallet.getUser());
    auditTrail.setDescription("Money balance was subtracted " + cryptocurrencyAmount.multiply(cryptocurrencyPrice) + " in the wallet with id= " + wallet.getId());
    auditTrailRepository.save(auditTrail);
    return repository.save(wallet);
  }

  public WalletEntity addWalletMoneyBalanceByUserId(Long id, BigDecimal cryptocurrencyAmount, BigDecimal cryptocurrencyPrice) {
    AuditTrail auditTrail = new AuditTrail();
    WalletEntity wallet = repository.findWalletEntityByUserId(id);
    BigDecimal moneyBalance = wallet.getMoneyBalance();
    wallet.setMoneyBalance(moneyBalance.add(cryptocurrencyAmount.multiply(cryptocurrencyPrice)));
    auditTrail.setUser(wallet.getUser());
    auditTrail.setDescription("Money balance was added " + cryptocurrencyAmount.multiply(cryptocurrencyPrice) + " in the wallet with id= " + wallet.getId());
    auditTrailRepository.save(auditTrail);
    return repository.save(wallet);
  }

  public WalletEntity addWalletCryptocurrencyBalanceByUserId(Long id, BigDecimal cryptocurrencyAmount) {
    AuditTrail auditTrail = new AuditTrail();
    WalletEntity wallet = repository.findWalletEntityByUserId(id);
    BigDecimal cryptocurrencyBalance = wallet.getCryptocurrencyBalance();
    wallet.setCryptocurrencyBalance(cryptocurrencyBalance.add(cryptocurrencyAmount));
    auditTrail.setUser(wallet.getUser());
    auditTrail.setDescription("Cryptocurrency balance was added " + cryptocurrencyAmount + " in the wallet with id= " + wallet.getId());
    auditTrailRepository.save(auditTrail);
    return repository.save(wallet);
  }
}
