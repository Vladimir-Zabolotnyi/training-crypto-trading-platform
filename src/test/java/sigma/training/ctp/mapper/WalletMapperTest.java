package sigma.training.ctp.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.persistence.entity.CurrencyEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.WalletEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WalletMapperTest {

  private static final BigDecimal AMOUNT = new BigDecimal("200");
  private static final CurrencyEntity CURRENCY= new CurrencyEntity(1L,false,false,"Bitcoin","BTC");
  private static final CurrencyEntity CURRENCY_WITHOUT_ID= new CurrencyEntity(null,false,false,"Bitcoin","BTC");
  private static final WalletEntity WALLET = new WalletEntity(1L,new UserEntity(),CURRENCY,AMOUNT);
  private static final WalletEntity WALLET_WITHOUT_USER = new WalletEntity(1L,new UserEntity(),CURRENCY_WITHOUT_ID,AMOUNT);

  private static final WalletRestDto WALLET_DTO = new WalletRestDto(1L,CURRENCY.getName(),CURRENCY.getAcronym(),AMOUNT);

  WalletMapper walletMapper;

  @BeforeEach
  void setUp() {
    walletMapper = new WalletMapper();
  }

  @Test
  void toRestDto() {
    assertEquals(WALLET_DTO,walletMapper.toRestDto(WALLET));
  }

  @Test
  void toEntity() {

    assertEquals(WALLET_WITHOUT_USER,walletMapper.toEntity(WALLET_DTO));
  }
}
