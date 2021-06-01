package sigma.training.ctp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.WalletRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository repository;

    @InjectMocks
    private WalletService service;

    private static final Long ID = 1L;
    private static final BigDecimal MONEY_BALANCE = new BigDecimal("228.13");
    private static final BigDecimal CRYPTOCURRENCY_BALANCE = new BigDecimal("37");
    private static final WalletEntity WALLET = new WalletEntity(new UserEntity(), MONEY_BALANCE, CRYPTOCURRENCY_BALANCE);

    @Test
    public void testGetWalletByUserIdCheckBalances() {
        when(repository.findWalletEntityByUserId(ID)).thenReturn(WALLET);

        WalletRestDto expected = new WalletRestDto(
            WALLET.getMoneyBalance(),
            WALLET.getCryptocurrencyBalance()
        );
        WalletRestDto actual = service.getWalletByUserId(ID);

        assertEquals(expected.getMoneyBalance(), actual.getMoneyBalance());
        assertEquals(expected.getCryptocurrencyBalance(), actual.getCryptocurrencyBalance());
    }
}