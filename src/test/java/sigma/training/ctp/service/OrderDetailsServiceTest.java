package sigma.training.ctp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.exception.CannotFulfillOwnOrderException;
import sigma.training.ctp.exception.InsufficientAmountBankCurrencyException;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.exception.NoActiveOrdersFoundException;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
  private static final UserEntity USER_TO_SELL = new UserEntity();
  private static final OrderType ORDER_TYPE_SELL = OrderType.SELL;
  private static final OrderType ORDER_TYPE_BUY = OrderType.BUY;
  private static final WalletEntity WALLET_AFTER_UPDATE = new WalletEntity(USER, new BigDecimal("228.13"), new BigDecimal("17"));
  private static final Instant CREATION_DATE = Instant.ofEpochMilli(1000);
  private static final OrderDetailsRestDto ORDER_FROM_BODY = new OrderDetailsRestDto(null, null, null, null, null, CRYPTOCURRENCY_PRICE, CRYPTOCURRENCY_AMOUNT);

  private static final OrderStatus CREATED_STSTUS = OrderStatus.CREATED;
  private static final OrderStatus FULFILLED_STATUS = OrderStatus.FULFILLED;
  private static final OrderStatus CANCELLED_STATUS = OrderStatus.CANCELLED;

  private static final OrderDetailsEntity ORDER = new OrderDetailsEntity(
    USER, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_1 = new OrderDetailsEntity(null, null,
    USER, ORDER_STATUS, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsRestDto ORDER_DTO = new OrderDetailsRestDto(
    null, null,
    USER.getId(),
    ORDER_FROM_BODY.getOrderStatus(), ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_BY_ID_SELL = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, ORDER_STATUS, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_BY_ID_BUY = new OrderDetailsEntity(
    ID_2, CREATION_DATE,
    USER_TO_SELL, ORDER_STATUS, ORDER_TYPE_BUY,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_BY_ID_FOR_EXCEPTION1 = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, ORDER_STATUS, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_BY_ID_FOR_EXCEPTION2 = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, ORDER_STATUS, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsRestDto ORDER_DTO_BY_ID_SELL = new OrderDetailsRestDto(
    ID, CREATION_DATE,
    USER.getId(), ORDER_STATUS_AFTER_PURCHASE, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsRestDto ORDER_DTO_BY_ID_BUY = new OrderDetailsRestDto(
    ID_2, CREATION_DATE,
    USER.getId(), ORDER_STATUS_AFTER_PURCHASE, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  /* cancelOrder(id) */

  private static final OrderDetailsEntity CANCEL_SELL_ORDER_STATUS_CREATED = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, CREATED_STSTUS, ORDER_TYPE,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount()
  );

  private static final OrderDetailsEntity CANCEL_SELL_ORDER_STATUS_FULFILLED = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, FULFILLED_STATUS, ORDER_TYPE,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount()
  );

  private static final OrderDetailsEntity CANCEL_SELL_ORDER_STATUS_CANCELLED = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, CANCELLED_STATUS, ORDER_TYPE,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount()
  );

  private static final OrderDetailsRestDto CANCEL_SELL_ORDER_DTO_1 = new OrderDetailsRestDto(
    ID, CREATION_DATE,
    USER.getId(), CANCELLED_STATUS, ORDER_TYPE,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount()
  );

  private static final List<OrderDetailsEntity> orderList = new ArrayList<>();
  private static final List<OrderDetailsRestDto> orderDtoList = new ArrayList<>();

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
    Mockito.when(orderDetailsRepository.save(ORDER)).thenReturn(ORDER);
    Mockito.when(orderMapper.toRestDto(ORDER)).thenReturn(ORDER_DTO);
    Mockito.when(orderMapper.toEntity(ORDER_FROM_BODY, USER)).thenReturn(ORDER);
    OrderDetailsRestDto orderDtoActual = orderDetailsService.postOrder(ORDER_FROM_BODY, USER);
    orderDtoActual.setUserId(ID);
    orderDtoActual.setCreationDate(CREATION_DATE);
    orderDtoActual.setOrderStatus(ORDER_STATUS);
    OrderDetailsRestDto orderDtoExpected = new OrderDetailsRestDto(null, CREATION_DATE, USER.getId(), ORDER_STATUS, ORDER_TYPE_SELL, CRYPTOCURRENCY_PRICE, CRYPTOCURRENCY_AMOUNT);
    orderDtoExpected.setCreationDate(orderDtoActual.getCreationDate());
    assertEquals(orderDtoExpected, orderDtoActual);

  }

  @Test
  void fulfillOrder() throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException, CannotFulfillOwnOrderException, InsufficientAmountCryptoException, InsufficientAmountBankCurrencyException {
    USER.setId(ID);
    USER_TO_BUY.setId(ID_2);
    USER_TO_SELL.setId(ID_2);
    Mockito.when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_SELL));
    Mockito.when(orderDetailsRepository.findById(ID_2)).thenReturn(Optional.of(ORDER_BY_ID_BUY));
    Mockito.when(orderMapper.toRestDto(ORDER_BY_ID_SELL)).thenReturn(ORDER_DTO_BY_ID_SELL);
    Mockito.when(orderMapper.toRestDto(ORDER_BY_ID_BUY)).thenReturn(ORDER_DTO_BY_ID_BUY);
    OrderDetailsRestDto actualFulfilledOrderSell = orderDetailsService.fulfillOrder(ID, USER_TO_BUY);
    OrderDetailsRestDto actualFulfilledOrderBuy = orderDetailsService.fulfillOrder(ID_2, USER);
    ORDER_BY_ID_BUY.setOrderStatus(OrderStatus.FULFILLED);
    ORDER_BY_ID_SELL.setOrderStatus(OrderStatus.FULFILLED);
    assertEquals(ORDER_DTO_BY_ID_SELL, actualFulfilledOrderSell);
    assertEquals(ORDER_DTO_BY_ID_BUY, actualFulfilledOrderBuy);
  }

  @Test
  void exceptionNotFoundOrder() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.empty());
    assertThrows(OrderNotFoundException.class, () -> orderDetailsService.fulfillOrder(ID, USER_TO_BUY));
  }

  @Test
  void exceptionAlreadyCancelledOrder() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION1));
    ORDER_BY_ID_FOR_EXCEPTION1.setOrderStatus(OrderStatus.CANCELLED);
    assertThrows(OrderAlreadyCancelledException.class, () -> orderDetailsService.fulfillOrder(ID, USER_TO_BUY));
  }

  @Test
  void exceptionAlreadyFulfilledOrder() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION2));
    ORDER_BY_ID_FOR_EXCEPTION2.setOrderStatus(OrderStatus.FULFILLED);
    assertThrows(OrderAlreadyFulfilledException.class, () -> orderDetailsService.fulfillOrder(ID, USER_TO_BUY));
  }

  @Test
  void exceptionNotFulfilledOwnOrder() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION1));
    assertThrows(CannotFulfillOwnOrderException.class, () -> orderDetailsService.fulfillOrder(ID, USER));
  }

  @Test
  void getAllOrders() throws NoActiveOrdersFoundException {
    USER.setId(ID_2);
    orderList.add(ORDER_1);
    orderList.add(ORDER_BY_ID_SELL);
    orderDtoList.add(ORDER_DTO);
    orderDtoList.add(ORDER_DTO_BY_ID_SELL);

    when(orderDetailsRepository.findAll(any(Specification.class))).thenReturn(orderList);
    when(orderMapper.toRestDto(orderList)).thenReturn(orderDtoList);
    assertEquals(orderDtoList, orderDetailsService.getAllOrders(ORDER_TYPE, USER));
  }

  @Test
  void exceptionNoActiveOrdersFound() {
    when(orderDetailsRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
    assertThrows(NoActiveOrdersFoundException.class, () ->  orderDetailsService.getAllOrders(ORDER_TYPE, USER));
  }

  /* cancelOrder(id) */

  @Test
  public void testCancelOrderPassCreatedStatusCheckOrderStatus()
    throws OrderAlreadyFulfilledException, OrderAlreadyCancelledException, OrderNotFoundException {

    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(CANCEL_SELL_ORDER_STATUS_CREATED));
    when(orderMapper.toRestDto(CANCEL_SELL_ORDER_STATUS_CREATED)).thenReturn(CANCEL_SELL_ORDER_DTO_1);

    OrderDetailsRestDto actual = orderDetailsService.cancelOrder(ID);

    assertEquals(CANCEL_SELL_ORDER_DTO_1.getOrderStatus(), actual.getOrderStatus());
  }

  @Test
  public void testCancelOrderPassFulfilledStatus() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(CANCEL_SELL_ORDER_STATUS_FULFILLED));

    assertThrows(OrderAlreadyFulfilledException.class, () -> orderDetailsService.cancelOrder(ID));
  }

  @Test
  public void testCancelOrderPassCancelledStatus() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(CANCEL_SELL_ORDER_STATUS_CANCELLED));

    assertThrows(OrderAlreadyCancelledException.class, () -> orderDetailsService.cancelOrder(ID));
  }

  @Test
  public void testCancelOrderPassNonExistingOrderId() {
    Long id = 555L;

    assertThrows(OrderNotFoundException.class, () -> orderDetailsService.cancelOrder(id));
  }

}
