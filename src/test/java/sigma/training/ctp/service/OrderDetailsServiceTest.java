package sigma.training.ctp.service;

import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.enums.OrderType;
import sigma.training.ctp.enums.Status;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.OrderDetailsRepository;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrderDetailsServiceTest {


  private static final Long ID = 1L;
  private static final BigDecimal CRYPTOCURRENCY_AMOUNT = new BigDecimal("20");
  private static final BigDecimal CRYPTOCURRENCY_PRICE = new BigDecimal("400");
  private static final Status STATUS = Status.CREATED;
  private static final UserEntity USER = new UserEntity();
  private static final OrderType ORDER_TYPE = OrderType.SELL;
  private static final WalletRestDto WALLET = new WalletRestDto(new BigDecimal("17").toString(), new BigDecimal("228.13").toString()f);
  private static final WalletEntity WALLET_AFTER_UPDATE = new WalletEntity(USER, new BigDecimal("228.13"), new BigDecimal("17"));

  private static final OrderDetailsEntity ORDER_FROM_BODY = new OrderDetailsEntity(null, null, STATUS, null, CRYPTOCURRENCY_PRICE, CRYPTOCURRENCY_AMOUNT);
  private static final OrderDetailsEntity ORDER_DETAILS = new OrderDetailsEntity(
    null, USER,
    ORDER_FROM_BODY.getStatus(), ORDER_TYPE,
    ORDER_FROM_BODY.getCryptocurrencyPrice(),ORDER_FROM_BODY.getCryptocurrencyAmount());


  @Mock
  WalletService walletService;

  @Mock
  OrderDetailsRepository orderDetailsRepository;

  @InjectMocks
  OrderDetailsService orderDetailsService;


  @Test
  void saveOrder() throws InsufficientAmountCryptoException {
    USER.setId(ID);

    Mockito.when(walletService.reduceWalletCryptocurrencyBalanceByUserId(ID, CRYPTOCURRENCY_AMOUNT)).thenReturn(WALLET_AFTER_UPDATE);
    Mockito.when(orderDetailsRepository.save(ORDER_DETAILS).getCreationDate()).thenReturn(null);
    OrderDetailsRestDto orderActual = orderDetailsService.saveOrder(STATUS,CRYPTOCURRENCY_PRICE,CRYPTOCURRENCY_AMOUNT,ORDER_TYPE,USER);
    orderActual.setCreationDate(ORDER_DETAILS.getCreationDate());
    OrderDetailsRestDto orderExpected = new OrderDetailsRestDto(null,orderActual.getCreationDate(),USER.getId(),STATUS,ORDER_TYPE,CRYPTOCURRENCY_PRICE,CRYPTOCURRENCY_AMOUNT);
    orderExpected.setCreationDate(orderActual.getCreationDate());
    assertEquals(orderExpected, orderActual);

  }
}
