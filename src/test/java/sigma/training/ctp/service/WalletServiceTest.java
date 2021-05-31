package sigma.training.ctp.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import sigma.training.ctp.dto.WalletBalanceRestDto;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository repository;

    @InjectMocks
    private WalletService service;

    private static WalletEntity wallet;

    @BeforeAll
    public static void setUp() {
        UserEntity user = new UserEntity();
        BigDecimal moneyBalance = new BigDecimal("228.13");
        BigDecimal cryptocurrencyBalance = new BigDecimal("37");

        wallet = new WalletEntity(user, moneyBalance, cryptocurrencyBalance);
    }

    @Test
    public void testGetWalletByUserIdCheckMoneyBalance() {
        Long id = 1L;
        when(repository.findUserWalletEntityByUserId(id)).thenReturn(wallet);

        WalletBalanceRestDto expected = new WalletBalanceRestDto(
                wallet.getMoneyBalance(),
                wallet.getCryptocurrencyBalance()
        );
        WalletBalanceRestDto real = service.getWalletByUserId(id);

        assertEquals(expected.getMoneyBalance(), real.getMoneyBalance());
    }

    @Test
    public void testGetWalletByUserIdCheckCryptocurrencyBalance() {
        Long id = 1L;
        when(repository.findUserWalletEntityByUserId(id)).thenReturn(wallet);

        WalletBalanceRestDto expected = new WalletBalanceRestDto(
                wallet.getMoneyBalance(),
                wallet.getCryptocurrencyBalance()
        );
        WalletBalanceRestDto real = service.getWalletByUserId(id);

        assertEquals(expected.getCryptocurrencyBalance(), real.getCryptocurrencyBalance());
    }

    @Test
    public void testGetWalletByUserIdCheckAll() {
        Long id = 1L;
        when(repository.findUserWalletEntityByUserId(id)).thenReturn(wallet);

        WalletBalanceRestDto expected = new WalletBalanceRestDto(
                wallet.getMoneyBalance(),
                wallet.getCryptocurrencyBalance()
        );
        WalletBalanceRestDto real = service.getWalletByUserId(id);

        assertEquals(expected, real);
    }
}