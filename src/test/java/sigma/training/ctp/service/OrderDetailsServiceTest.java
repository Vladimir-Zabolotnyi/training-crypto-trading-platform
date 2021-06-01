package sigma.training.ctp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.dto.WalletRestDto;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.enums.OrderType;
import sigma.training.ctp.persistence.entity.enums.Status;
import sigma.training.ctp.persistence.repository.OrderDetailsRepository;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrderDetailsServiceTest {


  private static final Long ID = 1L;
  private static final BigDecimal CRYPTOCURRENCY_AMOUNT = new BigDecimal("20");
  private static final BigDecimal CRYPTOCURRENCY_PRICE = new BigDecimal("400");
  private static final Status STATUS = Status.created;
  private static final UserEntity USER = new UserEntity();
  private static final WalletRestDto WALLET = new WalletRestDto(new BigDecimal("17"), new BigDecimal("228.13"));
  private static final OrderType ORDER_TYPE = OrderType.sell;
  private static final OrderDetailsEntity ORDER_FROM_BODY = new OrderDetailsEntity(Instant.now(), null, STATUS, null, CRYPTOCURRENCY_PRICE, CRYPTOCURRENCY_AMOUNT);
  private static final OrderDetailsEntity ORDER_DETAILS = new OrderDetailsEntity(ORDER_FROM_BODY.getCreationDate(), ORDER_FROM_BODY, ORDER_TYPE, USER);


  @Mock
  WalletService walletService;

  @Mock
  OrderDetailsRepository orderDetailsRepository;

  @InjectMocks
  OrderDetailsService orderDetailsService;


  @Test
  void saveOrder() {
    USER.setId(ID);

    Mockito.when(walletService.reduceWalletCryptocurrencyBalanceByUserId(ID, CRYPTOCURRENCY_AMOUNT)).thenReturn(WALLET);
    Mockito.when(orderDetailsRepository.save(ORDER_DETAILS)).thenReturn(ORDER_DETAILS);
    OrderDetailsRestDto orderActual = orderDetailsService.saveOrder(ORDER_FROM_BODY, ORDER_TYPE, USER);
    orderActual.setCreationDate(ORDER_DETAILS.getCreationDate());
    OrderDetailsRestDto orderExpected = new OrderDetailsRestDto(ORDER_DETAILS);
    orderExpected.setCreationDate(orderActual.getCreationDate());
    assertEquals(orderExpected, orderActual);

  }
}
