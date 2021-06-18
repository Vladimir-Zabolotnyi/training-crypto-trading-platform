package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.exception.InsufficientCurrencyAmountException;
import sigma.training.ctp.mapper.WalletMapper;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

  @Autowired
  private WalletRepository repository;

  @Autowired
  WalletMapper walletMapper;

  @Autowired
  UserService userService;


  public List<WalletRestDto> getAllWalletsByUserId(Long userId) {

    return walletMapper.toRestDto(repository.findAllByUserId(userId));
  }

  public WalletRestDto getWalletByUserIdAndCurrencyName(Long userId,String currencyName) {

    return walletMapper.toRestDto(repository.findWalletEntityByUserIdAndCurrencyName(userId,currencyName));
  }


  public WalletEntity subtractWalletCurrencyAmountByWalletId(Long id, BigDecimal currencyAmountToSubtract) throws InsufficientCurrencyAmountException {
    WalletEntity wallet = repository.findWalletEntityById(id);
    BigDecimal walletCurrencyAmount = wallet.getAmount();
    if (walletCurrencyAmount.compareTo(currencyAmountToSubtract) < 0) {
      throw new InsufficientCurrencyAmountException();
    }
    wallet.setAmount(walletCurrencyAmount.subtract(currencyAmountToSubtract));
    return repository.save(wallet);
  }


  public WalletEntity addWalletCurrencyAmountByWalletId(Long id, BigDecimal currencyAmountToAdd) {
    WalletEntity wallet = repository.findWalletEntityById(id);
    BigDecimal walletCurrencyAmount = wallet.getAmount();
    wallet.setAmount(walletCurrencyAmount.add(currencyAmountToAdd));
    return repository.save(wallet);
  }
}
