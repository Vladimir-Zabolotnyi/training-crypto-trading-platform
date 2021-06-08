package sigma.training.ctp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
  private static final UserEntity USER_TO_SELL = new UserEntity();
  private static final OrderType ORDER_TYPE_SELL = OrderType.SELL;
  private static final OrderType ORDER_TYPE_BUY = OrderType.BUY;

  private static final WalletEntity WALLET_AFTER_UPDATE = new WalletEntity(USER, new BigDecimal("228.13"), new BigDecimal("17"));
  private static final Instant CREATION_DATE = Instant.ofEpochMilli(1000);
  private static final OrderDetailsRestDto ORDER_FROM_BODY_SELL = new OrderDetailsRestDto(null, null, null, null, ORDER_TYPE_SEll, CRYPTOCURRENCY_PRICE, CRYPTOCURRENCY_AMOUNT);
  private static final OrderDetailsRestDto ORDER_FROM_BODY_BUY = new OrderDetailsRestDto(null, null, null, null, ORDER_TYPE_BUY, CRYPTOCURRENCY_PRICE, CRYPTOCURRENCY_AMOUNT);

  private static final OrderDetailsEntity ORDER = new OrderDetailsEntity(
    USER, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());
  private static final OrderDetailsEntity ORDER_DETAILS_SELL = new OrderDetailsEntity(
    USER, ORDER_TYPE_SEll,
    ORDER_FROM_BODY_SELL.getCryptocurrencyPrice(), ORDER_FROM_BODY_SELL.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_1 = new OrderDetailsEntity(null, null,
    USER, ORDER_STATUS, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());
    USER, ORDER_STATUS, ORDER_TYPE_SEll,
    ORDER_FROM_BODY_SELL.getCryptocurrencyPrice(), ORDER_FROM_BODY_SELL.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_DETAILS_BUY = new OrderDetailsEntity(
    USER, ORDER_TYPE_BUY,
    ORDER_FROM_BODY_SELL.getCryptocurrencyPrice(), ORDER_FROM_BODY_SELL.getCryptocurrencyAmount());

  private static final OrderDetailsRestDto ORDER_DTO_SELL = new OrderDetailsRestDto(
    null, null,
    USER.getId(),
    ORDER_FROM_BODY_SELL.getOrderStatus(), ORDER_TYPE_SEll,
    ORDER_FROM_BODY_SELL.getCryptocurrencyPrice(), ORDER_FROM_BODY_SELL.getCryptocurrencyAmount());

  private static final OrderDetailsRestDto ORDER_DTO_BUY = new OrderDetailsRestDto(
    null, null,
    USER.getId(),
    ORDER_FROM_BODY.getOrderStatus(), ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());
    ORDER_FROM_BODY_SELL.getOrderStatus(), ORDER_TYPE_BUY,
    ORDER_FROM_BODY_SELL.getCryptocurrencyPrice(), ORDER_FROM_BODY_SELL.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_BY_ID_SELL = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, ORDER_STATUS, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_BY_ID_BUY = new OrderDetailsEntity(
    ID_2, CREATION_DATE,
    USER_TO_SELL, ORDER_STATUS, ORDER_TYPE_BUY,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());
    USER, ORDER_STATUS, ORDER_TYPE_SEll,
    ORDER_FROM_BODY_SELL.getCryptocurrencyPrice(), ORDER_FROM_BODY_SELL.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_BY_ID_FOR_EXCEPTION1 = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, ORDER_STATUS, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());
    USER, ORDER_STATUS, ORDER_TYPE_SEll,
    ORDER_FROM_BODY_SELL.getCryptocurrencyPrice(), ORDER_FROM_BODY_SELL.getCryptocurrencyAmount());

  private static final OrderDetailsEntity ORDER_BY_ID_FOR_EXCEPTION2 = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, ORDER_STATUS, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());
    USER, ORDER_STATUS, ORDER_TYPE_SEll,
    ORDER_FROM_BODY_SELL.getCryptocurrencyPrice(), ORDER_FROM_BODY_SELL.getCryptocurrencyAmount());

  private static final OrderDetailsRestDto ORDER_DTO_BY_ID_SELL = new OrderDetailsRestDto(
    ID, CREATION_DATE,
    USER.getId(), ORDER_STATUS_AFTER_PURCHASE, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());

  private static final OrderDetailsRestDto ORDER_DTO_BY_ID_BUY = new OrderDetailsRestDto(
    ID_2, CREATION_DATE,
    USER.getId(), ORDER_STATUS_AFTER_PURCHASE, ORDER_TYPE_SELL,
    ORDER_FROM_BODY.getCryptocurrencyPrice(), ORDER_FROM_BODY.getCryptocurrencyAmount());
    USER.getId(), ORDER_STATUS_AFTER_PURCHASE, ORDER_TYPE_SEll,
    ORDER_FROM_BODY_SELL.getCryptocurrencyPrice(), ORDER_FROM_BODY_SELL.getCryptocurrencyAmount());

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
  void postOrder() throws InsufficientAmountCryptoException, InsufficientAmountBankCurrencyException {
    Mockito.when(walletService.subtractWalletMoneyBalanceByUserId(ID, CRYPTOCURRENCY_AMOUNT,CRYPTOCURRENCY_PRICE)).thenReturn(WALLET_AFTER_UPDATE);
    Mockito.when(orderDetailsRepository.save(ORDER_DETAILS_SELL)).thenReturn(ORDER_DETAILS_SELL);
    Mockito.when(orderDetailsRepository.save(ORDER_DETAILS_BUY)).thenReturn(ORDER_DETAILS_BUY);
    Mockito.when(orderMapper.toRestDto(ORDER_DETAILS_SELL)).thenReturn(ORDER_DTO_SELL);
    Mockito.when(orderMapper.toRestDto(ORDER_DETAILS_BUY)).thenReturn(ORDER_DTO_BUY);
    Mockito.when(orderMapper.toEntity(ORDER_FROM_BODY_SELL, USER)).thenReturn(ORDER_DETAILS_SELL);
    Mockito.when(orderMapper.toEntity(ORDER_FROM_BODY_BUY, USER)).thenReturn(ORDER_DETAILS_BUY);
    OrderDetailsRestDto orderDtoActualSell = orderDetailsService.postOrder(ORDER_FROM_BODY_SELL, USER);
    OrderDetailsRestDto orderDtoActualBuy = orderDetailsService.postOrder(ORDER_FROM_BODY_BUY, USER);
    orderDtoActualSell.setUserId(ID);
    orderDtoActualSell.setCreationDate(CREATION_DATE);
    orderDtoActualSell.setOrderStatus(ORDER_STATUS);
    orderDtoActualBuy.setUserId(ID);
    orderDtoActualBuy.setCreationDate(CREATION_DATE);
    orderDtoActualBuy.setOrderStatus(ORDER_STATUS);
    OrderDetailsRestDto orderDtoExpectedSell = new OrderDetailsRestDto(null, CREATION_DATE, USER.getId(), ORDER_STATUS, ORDER_TYPE_SEll, CRYPTOCURRENCY_PRICE, CRYPTOCURRENCY_AMOUNT);
    OrderDetailsRestDto orderDtoExpectedBuy = new OrderDetailsRestDto(null, CREATION_DATE, USER.getId(), ORDER_STATUS, ORDER_TYPE_BUY, CRYPTOCURRENCY_PRICE, CRYPTOCURRENCY_AMOUNT);
    orderDtoExpectedSell.setCreationDate(orderDtoActualSell.getCreationDate());
    orderDtoExpectedBuy.setCreationDate(orderDtoActualBuy.getCreationDate());
    assertEquals(orderDtoExpectedSell, orderDtoActualSell);
    assertEquals(orderDtoExpectedBuy, orderDtoActualBuy);

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
    Mockito.when(orderDetailsRepository.findById(ID)).thenReturn(Optional.empty());
    assertThrows(OrderNotFoundException.class, () -> orderDetailsService.fulfillOrder(ID, USER_TO_BUY));
  }

  @Test
  void exceptionAlreadyCancelledOrder() {
    Mockito.when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION1));
    ORDER_BY_ID_FOR_EXCEPTION1.setOrderStatus(OrderStatus.CANCELLED);
    assertThrows(OrderAlreadyCancelledException.class, () -> orderDetailsService.fulfillOrder(ID, USER_TO_BUY));
  }

  @Test
  void exceptionAlreadyFulfilledOrder() {
    Mockito.when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION2));
    ORDER_BY_ID_FOR_EXCEPTION2.setOrderStatus(OrderStatus.FULFILLED);
    assertThrows(OrderAlreadyFulfilledException.class, () -> orderDetailsService.fulfillOrder(ID, USER_TO_BUY));
  }

  @Test
  void exceptionNotFulfilledOwnOrder() {
    Mockito.when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION1));
    assertThrows(CannotFulfillOwnOrderException.class, () -> orderDetailsService.fulfillOrder(ID, USER));
  }

  @Test
  void getAllOrders() throws NoActiveOrdersFoundException {
    USER.setId(ID_2);
    orderList.add(ORDER_1);
    orderList.add(ORDER_BY_ID_SELL);
    orderDtoList.add(ORDER_DTO_SELL);
    orderDtoList.add(ORDER_DTO_BY_ID_SELL);

    Mockito.when(orderDetailsRepository.findAll(Mockito.any(Specification.class))).thenReturn(orderList);
    Mockito.when(orderMapper.toRestDto(orderList)).thenReturn(orderDtoList);
    assertEquals(orderDtoList, orderDetailsService.getAllOrders(ORDER_TYPE_SELL, USER));
  }

  @Test
  void exceptionNoActiveOrdersFound() {
    Mockito.when(orderDetailsRepository.findAll(Mockito.any(Specification.class))).thenReturn(Collections.emptyList());
    assertThrows(NoActiveOrdersFoundException.class, () ->  orderDetailsService.getAllOrders(ORDER_TYPE_SELL, USER));
  }

}
