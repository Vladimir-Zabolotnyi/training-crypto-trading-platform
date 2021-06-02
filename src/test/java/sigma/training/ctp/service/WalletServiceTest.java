package sigma.training.ctp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static sigma.training.ctp.service.WalletService.DELIMITER;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

  private static final Long ID = 1L;
  private static final BigDecimal MONEY_BALANCE = new BigDecimal("228.13");
  private static final BigDecimal CRYPTOCURRENCY_BALANCE = new BigDecimal("37");
  private static final BigDecimal CRYPTOCURRENCY_AMOUNT = new BigDecimal("20");
  private static final BigDecimal CRYPTOCURRENCY_AMOUNT_FOR_EXCEPTION = new BigDecimal("40");
  private static final BigDecimal CRYPTOCURRENCY_BALANCE_REDUCED = new BigDecimal("17");
  private static final WalletEntity WALLET = new WalletEntity(new UserEntity(), MONEY_BALANCE, CRYPTOCURRENCY_BALANCE);

  private static final String BANK_CURRENCY_NAME = "USDT";
  private static final String CRYPTOCURRENCY_NAME = "MarsOne";
  private static final String CRYPTOCURRENCY_SIGN = "♂";

  private static final WalletEntity WALLET_AFTER_UPDATE = new WalletEntity(WALLET.getUser(), MONEY_BALANCE, CRYPTOCURRENCY_BALANCE_REDUCED);
  @Mock
  private WalletRepository repository;

  @InjectMocks
  private WalletService service;

  @Test
  public void testGetWalletByUserIdCheckBalances() {
    ReflectionTestUtils.setField(service, "bankCurrencyName", BANK_CURRENCY_NAME);
    ReflectionTestUtils.setField(service, "cryptocurrencyName", CRYPTOCURRENCY_NAME);
    ReflectionTestUtils.setField(service, "cryptocurrencySign", CRYPTOCURRENCY_SIGN);

    when(repository.findWalletEntityByUserId(ID)).thenReturn(WALLET);

    WalletRestDto actualWallet = service.getWalletByUserId(ID);

    String moneyBalance = BANK_CURRENCY_NAME + DELIMITER + MONEY_BALANCE.toString();
    String cryptocurrencyBalance = CRYPTOCURRENCY_NAME + DELIMITER + CRYPTOCURRENCY_BALANCE.toString()
      + CRYPTOCURRENCY_SIGN;

    assertEquals(moneyBalance, actualWallet.getMoneyBalance());
    assertEquals(cryptocurrencyBalance, actualWallet.getCryptocurrencyBalance());
  }

  @Test
  void reduceWalletCryptocurrencyBalanceByUserId() throws InsufficientAmountCryptoException {
    when(repository.findWalletEntityByUserId(ID)).thenReturn(WALLET);
    when(repository.save(WALLET_AFTER_UPDATE)).thenReturn(WALLET_AFTER_UPDATE);
    WalletRestDto expected = new WalletRestDto(
      WALLET.getMoneyBalance(),
      CRYPTOCURRENCY_BALANCE_REDUCED
    );
    WalletEntity actual = service.reduceWalletCryptocurrencyBalanceByUserId(ID, CRYPTOCURRENCY_AMOUNT);
    assertEquals(expected.getCryptocurrencyBalance(), actual.getCryptocurrencyBalance());

  }

  @Test
  void exceptionReduceWalletCryptocurrencyBalanceByUserId() {
    when(repository.findWalletEntityByUserId(ID)).thenReturn(WALLET);
    assertThrows(InsufficientAmountCryptoException.class, () -> service.reduceWalletCryptocurrencyBalanceByUserId(ID, CRYPTOCURRENCY_AMOUNT_FOR_EXCEPTION));
  }
}
