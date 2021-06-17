package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.dto.OrderFilterDto;
import sigma.training.ctp.exception.CannotFulfillOwnOrderException;
import sigma.training.ctp.exception.InsufficientAmountBankCurrencyException;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;
import sigma.training.ctp.exception.NoActiveOrdersFoundException;
import sigma.training.ctp.mapper.OrderFilterMapper;
import sigma.training.ctp.mapper.OrderMapper;
import sigma.training.ctp.persistence.OrderFilter;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.repository.OrderDetailsRepository;
import sigma.training.ctp.persistence.repository.specification.OrderSpecification;

import javax.transaction.Transactional;
import java.util.List;
import java.util.ArrayList;

@Service
public class OrderDetailsService {

  @Autowired
  OrderDetailsRepository orderDetailsRepository;

  @Autowired
  WalletService walletService;

  @Autowired
  OrderMapper orderMapper;

  @Autowired
  OrderFilterMapper orderFilterMapper;

  @Autowired
  UserService userService;

  @Autowired
  AuditTrailService auditTrailService;


  @Transactional
  public OrderDetailsRestDto postOrder(OrderDetailsRestDto orderDto, UserEntity user) throws InsufficientAmountCryptoException, InsufficientAmountBankCurrencyException {
    OrderDetailsEntity order = orderMapper.toEntity(orderDto, user);
    switch (order.getOrderType()) {
      case SELL:
        walletService.subtractWalletCryptocurrencyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount());
        break;
      case BUY:
        walletService.subtractWalletMoneyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount(), order.getCryptocurrencyPrice());
        break;
    }
    return orderMapper.toRestDto(orderDetailsRepository.save(order));
  }

  @Transactional
  public OrderDetailsRestDto fulfillOrder(Long orderId, UserEntity currentUser) throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException, CannotFulfillOwnOrderException, InsufficientAmountCryptoException, InsufficientAmountBankCurrencyException {
    OrderDetailsEntity order = orderDetailsRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    if (order.getOrderStatus() == OrderStatus.CANCELLED) {
      throw new OrderAlreadyCancelledException(orderId);
    }
    if (order.getOrderStatus() == OrderStatus.FULFILLED) {
      throw new OrderAlreadyFulfilledException(orderId);
    }
    if (order.getUser().getId() == currentUser.getId()) {
      throw new CannotFulfillOwnOrderException();
    }
    switch (order.getOrderType()) {
      case SELL:
        walletService.subtractWalletMoneyBalanceByUserId(currentUser.getId(), order.getCryptocurrencyAmount(), order.getCryptocurrencyPrice());
        walletService.addWalletMoneyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount(), order.getCryptocurrencyPrice());
        walletService.addWalletCryptocurrencyBalanceByUserId(currentUser.getId(), order.getCryptocurrencyAmount());
        break;
      case BUY:
        walletService.addWalletCryptocurrencyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount());
        walletService.subtractWalletCryptocurrencyBalanceByUserId(currentUser.getId(), order.getCryptocurrencyAmount());
        walletService.addWalletMoneyBalanceByUserId(currentUser.getId(), order.getCryptocurrencyAmount(), order.getCryptocurrencyPrice());
        break;
    }
    order.setOrderStatus(OrderStatus.FULFILLED);
    return orderMapper.toRestDto(order);
  }

  @Transactional
  public OrderDetailsRestDto cancelOrder(Long orderId)
    throws OrderNotFoundException, OrderAlreadyFulfilledException, OrderAlreadyCancelledException {
    OrderDetailsEntity order = orderDetailsRepository
      .findById(orderId)
      .orElseThrow(() -> new OrderNotFoundException(orderId));

    if (order.getOrderStatus().equals(OrderStatus.FULFILLED)) {
      throw new OrderAlreadyFulfilledException(orderId);
    }
    if (order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
      throw new OrderAlreadyCancelledException(orderId);
    }

    switch (order.getOrderType()) {
      case SELL:
        walletService.addWalletCryptocurrencyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount());
        break;
      case BUY:
        walletService.addWalletMoneyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount(), order.getCryptocurrencyPrice());
        break;
    }

    order.setOrderStatus(OrderStatus.CANCELLED);
    orderDetailsRepository.save(order);
    return orderMapper.toRestDto(order);
  }

  @Transactional
  public List<OrderDetailsRestDto> getAllOrders(OrderFilterDto orderFilterDto) throws NoActiveOrdersFoundException {
    OrderFilter orderFilter = orderFilterMapper.toEntity(orderFilterDto);
    List<OrderDetailsEntity> orderList;
    if (orderFilter.getOrderType() == null) {
      orderList = orderDetailsRepository.findAll(OrderSpecification.byOrderStatus(OrderStatus.CREATED));
      return orderMapper.toRestDto(orderList);
    }
    switch (orderFilter.getOrderType()) {
      case SELL:
        orderList = orderDetailsRepository.findAll(
          orderFilterToCriteria(orderFilter)
            .and(OrderSpecification.orderByCryptocurrencyAmount(true)));
        break;
      case BUY:
        orderList = orderDetailsRepository.findAll(
          orderFilterToCriteria(orderFilter)
            .and(OrderSpecification.orderByCryptocurrencyAmount(false)));
        break;
      default:
        orderList = new ArrayList<>();
    }

    if (orderList.isEmpty()) {
      throw new NoActiveOrdersFoundException();
    }

    return orderMapper.toRestDto(orderList);
  }

  private Specification<OrderDetailsEntity> orderFilterToCriteria(OrderFilter orderFilter) {
    Specification<OrderDetailsEntity> specification = OrderSpecification.byOrderType(orderFilter.getOrderType());
    if (orderFilter.getUserId() == null) {
      return specification.and(OrderSpecification.byOrderStatus(OrderStatus.CREATED));
    }
    if (orderFilter.getOrderStatus() == null) {
      return specification.and(OrderSpecification.byUser(orderFilter.getUserId()));
    }
    return specification
      .and(OrderSpecification.byUser(orderFilter.getUserId()))
      .and(OrderSpecification.byOrderStatus(orderFilter.getOrderStatus()));
  }
}
