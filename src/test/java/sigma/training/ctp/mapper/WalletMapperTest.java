package sigma.training.ctp.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.WalletEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class WalletMapperTest {

  private static final BigDecimal MONEY_BALANCE = new BigDecimal("228.13");
  private static final BigDecimal CRYPTOCURRENCY_BALANCE = new BigDecimal("37");
  private static final WalletEntity WALLET = new WalletEntity(new UserEntity(), MONEY_BALANCE, CRYPTOCURRENCY_BALANCE);
  private static final WalletEntity WALLET_WITHOUT_USER = new WalletEntity();

  private static final String MONEY_BALANCE_DTO = "USDT 228.13";
  private static final String CRYPTOCURRENCY_BALANCE_DTO = "MarsCoin 37 ♂";
  private static final WalletRestDto WALLET_DTO = new WalletRestDto(MONEY_BALANCE_DTO,CRYPTOCURRENCY_BALANCE_DTO);

  WalletMapper walletMapper;

  @BeforeEach
  void setUp() {
    walletMapper = new WalletMapper();
  }

  @Test
  void toRestDto() {
    walletMapper.setBankCurrencyName("USDT");
    walletMapper.setCryptocurrencyName("MarsCoin");
    walletMapper.setCryptocurrencySign("♂");

    assertEquals(WALLET_DTO,walletMapper.toRestDto(WALLET));
  }

  @Test
  void toEntity() {
    WALLET_WITHOUT_USER.setCryptocurrencyBalance(CRYPTOCURRENCY_BALANCE);
    WALLET_WITHOUT_USER.setMoneyBalance(MONEY_BALANCE);
    assertEquals(WALLET_WITHOUT_USER,walletMapper.toEntity(WALLET_DTO));
  }
}
