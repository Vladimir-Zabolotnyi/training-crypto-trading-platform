package sigma.training.ctp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.dto.OrderFilterDto;
import sigma.training.ctp.exception.CannotFulfillOwnOrderException;
import sigma.training.ctp.exception.InsufficientCurrencyAmountException;
import sigma.training.ctp.exception.NoActiveOrdersFoundException;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;
import sigma.training.ctp.exception.WalletNotFoundException;
import sigma.training.ctp.mapper.OrderFilterMapper;
import sigma.training.ctp.mapper.OrderMapper;
import sigma.training.ctp.persistence.OrderFilter;
import sigma.training.ctp.persistence.entity.CurrencyEntity;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.repository.CurrencyRepository;
import sigma.training.ctp.persistence.repository.OrderDetailsRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderDetailsServiceTest {

  private static final Long ID = 1L;
  private static final Long ID_2 = 2L;
  private static final UserEntity USER = new UserEntity();
  private static final UserEntity USER_2 = new UserEntity();
  private static final OrderStatus ORDER_STATUS = OrderStatus.CREATED;
  private static final String SELL_CURRENCY_NAME = "USA_dollar";
  private static final String BUY_CURRENCY_NAME = "Bitcoin";
  private static final BigDecimal SELL_CURRENCY_AMOUNT = new BigDecimal("20");
  private static final BigDecimal BUY_CURRENCY_AMOUNT = new BigDecimal("400");
  private static final CurrencyEntity SELL_CURRENCY = new CurrencyEntity(null, false, false, SELL_CURRENCY_NAME, null);

  private static final CurrencyEntity BUY_CURRENCY = new CurrencyEntity(null, false, false, BUY_CURRENCY_NAME, null);

  private static final OrderDetailsRestDto ORDER_DTO = new OrderDetailsRestDto(
    ID, Instant.now(), ID, ORDER_STATUS, SELL_CURRENCY_NAME, BUY_CURRENCY_NAME, SELL_CURRENCY_AMOUNT, BUY_CURRENCY_AMOUNT);

  private static final OrderDetailsEntity ORDER = new OrderDetailsEntity(
    ID, Instant.now(), USER, ORDER_STATUS, SELL_CURRENCY, BUY_CURRENCY, SELL_CURRENCY_AMOUNT, BUY_CURRENCY_AMOUNT);
  private static final OrderDetailsEntity ORDER_BY_ID_FOR_EXCEPTION1 = new OrderDetailsEntity(
    ID, Instant.now(), USER, ORDER_STATUS, SELL_CURRENCY, BUY_CURRENCY, SELL_CURRENCY_AMOUNT, BUY_CURRENCY_AMOUNT);
  private static final OrderDetailsEntity ORDER_BY_ID_FOR_EXCEPTION2 = new OrderDetailsEntity(
    ID, Instant.now(), USER, ORDER_STATUS, SELL_CURRENCY, BUY_CURRENCY, SELL_CURRENCY_AMOUNT, BUY_CURRENCY_AMOUNT);
  private static final List<OrderDetailsEntity> ORDER_LIST = new ArrayList<>();
  private static final List<OrderDetailsRestDto> ORDER_DTO_LIST = new ArrayList<>();
  private static final OrderFilterDto ORDER_FILTER_DTO = new OrderFilterDto(ORDER_STATUS.toString(), ID);
  private static final OrderFilter ORDER_FILTER = new OrderFilter(ORDER_STATUS, ID);
  private static final UserEntity ROOT_USER = new UserEntity() ;


  @Mock
  WalletService walletService;

  @Mock
  OrderDetailsRepository orderDetailsRepository;

  @Mock
  OrderMapper orderMapper;
  @Mock
  OrderFilterMapper orderFilterMapper;

  @Mock
  CurrencyRepository currencyRepository;

  @InjectMocks
  OrderDetailsService orderDetailsService;

  @Mock
  FeeService feeService;

  @Mock
  UserService userService;

  @Test
  void postOrder() throws WalletNotFoundException, InsufficientCurrencyAmountException {
    USER.setId(ID);
    when(orderMapper.toEntity(ORDER_DTO, USER)).thenReturn(ORDER);
    when(orderMapper.toRestDto(ORDER)).thenReturn(ORDER_DTO);
    when(userService.getRootUser()).thenReturn(ROOT_USER);
    when(orderDetailsRepository.save(ORDER)).thenReturn(ORDER);
    when(feeService.getOrderFee(SELL_CURRENCY_AMOUNT)).thenReturn(SELL_CURRENCY_AMOUNT);
    when(currencyRepository.findByName(SELL_CURRENCY_NAME)).thenReturn(Optional.of(SELL_CURRENCY));
    when(currencyRepository.findByName(BUY_CURRENCY_NAME)).thenReturn(Optional.of(BUY_CURRENCY));
    assertEquals(ORDER_DTO, orderDetailsService.postOrder(ORDER_DTO, USER));
  }

  @Test
  void fulfillOrder() throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException, WalletNotFoundException, CannotFulfillOwnOrderException, InsufficientCurrencyAmountException {
    USER_2.setId(ID_2);
    ORDER.setOrderStatus(ORDER_STATUS);
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER));
    when(orderMapper.toRestDto(ORDER)).thenReturn(ORDER_DTO);
    assertEquals(ORDER_DTO, orderDetailsService.fulfillOrder(ID, USER_2));
  }

  @Test
  void cancelOrder() throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException, WalletNotFoundException {
    USER.setId(ID);
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER));
    when(orderDetailsRepository.save(ORDER)).thenReturn(ORDER);
    when(orderMapper.toRestDto(ORDER)).thenReturn(ORDER_DTO);
    assertEquals(ORDER_DTO, orderDetailsService.cancelOrder(ID));
  }

  @Test
  void getAllOrders() throws NoActiveOrdersFoundException {
    ORDER_LIST.add(ORDER);
    ORDER_DTO_LIST.add(ORDER_DTO);
    when(orderDetailsRepository.findAll(any(Specification.class))).thenReturn(ORDER_LIST);
    when(orderMapper.toRestDto(ORDER_LIST)).thenReturn(ORDER_DTO_LIST);
    when(orderFilterMapper.toEntity(ORDER_FILTER_DTO)).thenReturn(ORDER_FILTER);
    assertEquals(ORDER_DTO_LIST, orderDetailsService.getAllOrders(ORDER_FILTER_DTO));
  }


  @Test
  void exceptionNotFoundOrder() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.empty());
    assertThrows(OrderNotFoundException.class, () -> orderDetailsService.fulfillOrder(ID, USER_2));
  }

  //
  @Test
  void exceptionAlreadyCancelledOrder() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION1));
    ORDER_BY_ID_FOR_EXCEPTION1.setOrderStatus(OrderStatus.CANCELLED);
    assertThrows(OrderAlreadyCancelledException.class, () -> orderDetailsService.fulfillOrder(ID, USER_2));
  }

  @Test
  void exceptionAlreadyFulfilledOrder() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION2));
    ORDER_BY_ID_FOR_EXCEPTION2.setOrderStatus(OrderStatus.FULFILLED);
    assertThrows(OrderAlreadyFulfilledException.class, () -> orderDetailsService.fulfillOrder(ID, USER_2));
  }

  @Test
  void exceptionNotFulfilledOwnOrder() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION1));
    assertThrows(CannotFulfillOwnOrderException.class, () -> orderDetailsService.fulfillOrder(ID, USER));
  }


  @Test
  void exceptionNoActiveOrdersFound() {
    when(orderFilterMapper.toEntity(ORDER_FILTER_DTO)).thenReturn(ORDER_FILTER);
    when(orderDetailsRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
    assertThrows(NoActiveOrdersFoundException.class, () -> orderDetailsService.getAllOrders(ORDER_FILTER_DTO));
  }


  @Test
  public void testCancelOrderPassFulfilledStatus() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION2));
    ORDER_BY_ID_FOR_EXCEPTION2.setOrderStatus(OrderStatus.FULFILLED);
    assertThrows(OrderAlreadyFulfilledException.class, () -> orderDetailsService.cancelOrder(ID));
  }

  @Test
  public void testCancelOrderPassCancelledStatus() {
    when(orderDetailsRepository.findById(ID)).thenReturn(Optional.of(ORDER_BY_ID_FOR_EXCEPTION1));
    ORDER_BY_ID_FOR_EXCEPTION1.setOrderStatus(OrderStatus.CANCELLED);
    assertThrows(OrderAlreadyCancelledException.class, () -> orderDetailsService.cancelOrder(ID));
  }

  @Test
  public void testCancelOrderPassNonExistingOrderId() {
    Long id = 555L;
    assertThrows(OrderNotFoundException.class, () -> orderDetailsService.cancelOrder(id));
  }


}
