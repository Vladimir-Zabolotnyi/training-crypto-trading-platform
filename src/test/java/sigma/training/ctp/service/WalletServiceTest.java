package sigma.training.ctp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.exception.InsufficientCurrencyAmountException;
import sigma.training.ctp.exception.WalletNotFoundException;
import sigma.training.ctp.mapper.WalletMapper;
import sigma.training.ctp.persistence.entity.CurrencyEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

  private static final Long ID = 1L;
  private static final String NAME = "Bitcoin";
  private static final String ACRONYM = "BTC";
  private static final BigDecimal AMOUNT = new BigDecimal("100");
  private static final BigDecimal AMOUNT_TO_SUBTRACT = new BigDecimal("100");
  private static final BigDecimal AMOUNT_TO_ADD = new BigDecimal("50");
  private static final BigDecimal AMOUNT_TO_SUBTRACT_EXCEPTION = new BigDecimal("1000");
  private static final UserEntity USER = new UserEntity();
  private static final CurrencyEntity CURRENCY = new CurrencyEntity(ID,false,NAME,ACRONYM);

  private static final WalletEntity WALLET_BEFORE_SUBTRACT = new WalletEntity(ID,USER,CURRENCY,AMOUNT);
  private static final WalletEntity WALLET_BEFORE_ADD = new WalletEntity(ID,USER,CURRENCY,AMOUNT);
  private static final WalletEntity WALLET_AFTER_SUBTRACT = new WalletEntity(ID,USER,CURRENCY,AMOUNT.subtract(AMOUNT_TO_SUBTRACT));
  private static final WalletEntity WALLET_AFTER_ADD = new WalletEntity(ID,USER,CURRENCY,AMOUNT.add(AMOUNT_TO_ADD));
  private static final WalletRestDto WALLET_DTO = new WalletRestDto(ID,CURRENCY.getName(),CURRENCY.getAcronym(),AMOUNT);
  private static final List<WalletEntity> WALLET_LIST = new ArrayList<>();
  private static final List<WalletRestDto> WALLET_DTO_LIST = new ArrayList<>();

  @Mock
  private WalletRepository repository;

  @Mock
  private WalletMapper walletMapper;

  @InjectMocks
  private WalletService service;



  @Test
  void exceptionSubtractWalletMoneyBalanceByUserId()  {
    when(repository.findWalletEntityByUserIdAndCurrencyName(ID,NAME)).thenReturn(Optional.of(WALLET_BEFORE_SUBTRACT));
    assertThrows(InsufficientCurrencyAmountException.class, () -> service.subtractWalletCurrencyAmountByWalletId(ID,NAME, AMOUNT_TO_SUBTRACT_EXCEPTION));
  }
  @Test
  void exceptionGetWalletByUserId() {
    when(repository.findWalletEntityByUserIdAndCurrencyName(ID,NAME)).thenReturn(Optional.empty());
    assertThrows(WalletNotFoundException.class, () -> service.subtractWalletCurrencyAmountByWalletId(ID,NAME, AMOUNT_TO_SUBTRACT));
  }

  @Test
  void getAllWalletsByUserId() {
    WALLET_LIST.add(WALLET_BEFORE_SUBTRACT);
    WALLET_DTO_LIST.add(WALLET_DTO);
    when(repository.findAllByUserId(ID)).thenReturn(WALLET_LIST);
    when(walletMapper.toRestDto(WALLET_LIST)).thenReturn(WALLET_DTO_LIST);
    assertEquals(WALLET_DTO_LIST,service.getAllWalletsByUserId(ID));
  }

  @Test
  void getWalletByUserIdAndCurrencyName() throws WalletNotFoundException {
    when(repository.findWalletEntityByUserIdAndCurrencyName(ID,CURRENCY.getName())).thenReturn(Optional.of(WALLET_BEFORE_SUBTRACT));
    assertEquals(WALLET_BEFORE_SUBTRACT,service.getWalletByUserIdAndCurrencyName(ID,CURRENCY.getName()));
  }

  @Test
  void subtractWalletCurrencyAmountByWalletId() throws InsufficientCurrencyAmountException, WalletNotFoundException {
    when(repository.findWalletEntityByUserIdAndCurrencyName(ID,NAME)).thenReturn(Optional.of(WALLET_BEFORE_SUBTRACT));
    when(repository.save(WALLET_AFTER_SUBTRACT)).thenReturn(WALLET_AFTER_SUBTRACT);
    assertEquals(WALLET_AFTER_SUBTRACT,service.subtractWalletCurrencyAmountByWalletId(ID,NAME,AMOUNT_TO_SUBTRACT));
  }

  @Test
  void addWalletCurrencyAmountByWalletId() throws WalletNotFoundException {
    when(repository.findWalletEntityByUserIdAndCurrencyName(ID,NAME)).thenReturn(Optional.of(WALLET_BEFORE_ADD));
    when(repository.save(WALLET_AFTER_ADD)).thenReturn(WALLET_AFTER_ADD);
    assertEquals(WALLET_AFTER_ADD,service.addWalletCurrencyAmountByWalletId(ID,NAME, AMOUNT_TO_ADD));
  }
}
