package sigma.training.ctp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.exception.CannotFulfillOwnOrderException;
import sigma.training.ctp.exception.InsufficientAmountBankCurrencyException;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;
import sigma.training.ctp.mapper.OrderMapper;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.dictionary.OrderType;
import sigma.training.ctp.persistence.entity.WalletEntity;
import sigma.training.ctp.persistence.repository.OrderDetailsRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderDetailsServiceTest {


  private static final Long ID = 1L;
  private static final Long ID_2 = 2L;
  private static final BigDecimal CRYPTOCURRENCY_AMOUNT = new BigDecimal("20");
  private static final BigDecimal CRYPTOCURRENCY_PRICE = new BigDecimal("400");
  private static final OrderStatus ORDER_STATUS = OrderStatus.CREATED;
  private static final OrderStatus ORDER_STATUS_AFTER_PURCHASE = OrderStatus.CREATED;
  private static final UserEntity USER = new UserEntity();
  private static final UserEntity USER_TO_BUY = new UserEntity();
  private static final OrderType ORDER_TYPE = OrderType.SELL;
  private static final WalletEntity WALLET_AFTER_UPDATE = new WalletEntity(USER, new BigDecimal("228.13"), new BigDecimal("17"));
  private static final Instant CREATION_DATE = Instant.ofEpochMilli(1000);
  private static final OrderDetailsRestDto ORDER_FROM_BODY = new OrderDetailsRestDto(null, null, null, null, null, CRYPTOCURRENCY_PRICE, CRYPTOCURRENCY_AMOUNT);

  private static final OrderDetailsEntity ORDER_DETAILS = new OrderDetailsEntity(
    USER, ORDER_TYPE,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsRestDto ORDER_DTO = new OrderDetailsRestDto(
    null, null,
    USER.getId(),
    ORDER_FROM_BODY.getOrderStatus(), ORDER_TYPE,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_BY_ID = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, ORDER_STATUS, ORDER_TYPE,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());
  private static final OrderDetailsEntity ORDER_BY_ID_FOR_EXCEPTION1 = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, ORDER_STATUS, ORDER_TYPE,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_BY_ID_FOR_EXCEPTION2 = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, ORDER_STATUS, ORDER_TYPE,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsRestDto ORDER_DTO_BY_ID = new OrderDetailsRestDto(
    ID, CREATION_DATE,
    USER.getId(), ORDER_STATUS_AFTER_PURCHASE, ORDER_TYPE,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  @Mock
  WalletService walletService;

  @Mock
  OrderDetailsRepository orderDetailsRepository;

  @Mock
  OrderMapper orderMapper;

  @InjectMocks
  OrderDetailsService orderDetailsService;


  @Test
  void postOrder() throws InsufficientAmountCryptoException {
    USER.setId(ID);
    Mockito.when(walletService.reduceWalletCryptocurrencyBalanceByUserId(ID, CRYPTOCURRENCY_AMOUNT)).thenReturn(WALLET_AFTER_UPDATE);
    Mockito.when(orderDetailsRepository.save(ORDER_DETAILS)).thenReturn(ORDER_DETAILS);
    Mockito.when(orderMapper.toRestDto(ORDER_DETAILS)).thenReturn(ORDER_DTO);
    Mockito.when(orderMapper.toEntity(ORDER_FROM_BODY, USER)).thenReturn(ORDER_DETAILS);
    OrderDetailsRestDto orderDtoActual = orderDetailsService.postOrder(ORDER_FROM_BODY, USER);
    orderDtoActual.setUserId(ID);
    orderDtoActual.setCreationDate(CREATION_DATE);
    orderDtoActual.setOrderStatus(ORDER_STATUS);
    OrderDetailsRestDto orderDtoExpected = new OrderDetailsRestDto(null, CREATION_DATE, USER.getId(), ORDER_STATUS, ORDER_TYPE, CRYPTOCURRENCY_PRICE, CRYPTOCURRENCY_AMOUNT);
    orderDtoExpected.setCreationDate(orderDtoActual.getCreationDate());
    assertEquals(orderDtoExpected, orderDtoActual);

  }

  @Test
  void fulfillOrder() throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException, CannotFulfillOwnOrderException, InsufficientAmountCryptoException, InsufficientAmountBankCurrencyException {
    USER.setId(ID);
    USER_TO_BUY.setId(ID_2);
    Mockito.when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID));
    Mockito.when(orderMapper.toRestDto(ORDER_BY_ID)).thenReturn(ORDER_DTO_BY_ID);
    OrderDetailsRestDto actualFulfilledOrder = orderDetailsService.fulfillOrder(ID, USER_TO_BUY);
    ORDER_BY_ID.setOrderStatus(OrderStatus.FULFILLED);
    assertEquals(ORDER_DTO_BY_ID, actualFulfilledOrder);
  }

  @Test
  void exceptionNotFoundOrder() {
    Mockito.when(orderDetailsRepository.findById(ID)).thenReturn(Optional.empty());
    assertThrows(OrderNotFoundException.class,()->orderDetailsService.fulfillOrder(ID, USER_TO_BUY));
  }
  @Test
  void exceptionAlreadyCancelledOrder() {
    Mockito.when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION1));
    ORDER_BY_ID_FOR_EXCEPTION1.setOrderStatus(OrderStatus.CANCELLED);
    assertThrows(OrderAlreadyCancelledException.class,()->orderDetailsService.fulfillOrder(ID, USER_TO_BUY));
  }
  @Test
  void exceptionAlreadyFulfilledOrder() {
    Mockito.when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION2));
    ORDER_BY_ID_FOR_EXCEPTION2.setOrderStatus(OrderStatus.FULFILLED);
    assertThrows(OrderAlreadyFulfilledException.class,()->orderDetailsService.fulfillOrder(ID, USER_TO_BUY));
  }
  @Test
  void exceptionNotFulfilledOwnOrder() {
    Mockito.when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION1));
    assertThrows(CannotFulfillOwnOrderException.class,()->orderDetailsService.fulfillOrder(ID, USER));
  }
}
