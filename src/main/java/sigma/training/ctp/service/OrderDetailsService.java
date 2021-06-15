package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.exception.CannotFulfillOwnOrderException;
import sigma.training.ctp.exception.InsufficientAmountBankCurrencyException;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;
import sigma.training.ctp.exception.NoActiveOrdersFoundException;
import sigma.training.ctp.mapper.OrderMapper;
import sigma.training.ctp.persistence.entity.AuditTrail;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.dictionary.OrderType;
import sigma.training.ctp.persistence.repository.AuditTrailRepository;
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
  AuditTrailRepository auditTrailRepository;

  @Autowired
  UserService userService;


  @Transactional
  public OrderDetailsRestDto postOrder(OrderDetailsRestDto orderDto, UserEntity user) throws InsufficientAmountCryptoException, InsufficientAmountBankCurrencyException {
    AuditTrail auditTrail = new AuditTrail();
    OrderDetailsEntity order = orderMapper.toEntity(orderDto, user);
    switch (order.getOrderType()) {
      case SELL:
        walletService.subtractWalletCryptocurrencyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount());
        break;
      case BUY:
        walletService.subtractWalletMoneyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount(), order.getCryptocurrencyPrice());
        break;
    }
    OrderDetailsRestDto orderDetailsRestDto = orderMapper.toRestDto(orderDetailsRepository.save(order));
    auditTrail.setDate(orderDetailsRestDto.getCreationDate());
    auditTrail.setUser(order.getUser());
    auditTrail.setDescription("Order with id= " + orderDetailsRestDto.getId() + " was created");
    auditTrailRepository.save(auditTrail);
    return orderDetailsRestDto;
  }

  @Transactional
  public OrderDetailsRestDto fulfillOrder(Long orderId, UserEntity currentUser) throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException, CannotFulfillOwnOrderException, InsufficientAmountCryptoException, InsufficientAmountBankCurrencyException {
    AuditTrail auditTrail = new AuditTrail();
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
    auditTrail.setUser(currentUser);
    auditTrail.setDescription("Order with id= " + order.getId() + " was fulfilled");
    auditTrailRepository.save(auditTrail);
    return orderMapper.toRestDto(order);
  }

  @Transactional
  public OrderDetailsRestDto cancelOrder(Long orderId)
    throws OrderNotFoundException, OrderAlreadyFulfilledException, OrderAlreadyCancelledException {
    AuditTrail auditTrail = new AuditTrail();
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
      case SELL: {
        walletService.addWalletCryptocurrencyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount());
      }
      break;
    }

    order.setOrderStatus(OrderStatus.CANCELLED);
    orderDetailsRepository.save(order);

    auditTrail.setUser(order.getUser());
    auditTrail.setDescription("Order with id= " + order.getId() + " was cancelled");
    auditTrailRepository.save(auditTrail);
    return orderMapper.toRestDto(order);
  }

  @Transactional
  public List<OrderDetailsRestDto> getAllOrders(OrderType orderType, UserEntity currentUser) throws NoActiveOrdersFoundException {
    AuditTrail auditTrail = new AuditTrail();
    List<OrderDetailsEntity> orderList;
    switch (orderType) {
      case SELL:
        orderList = orderDetailsRepository.findAll(
          OrderSpecification.byOrderStatus(OrderStatus.CREATED)
            .and(OrderSpecification.byOrderType(orderType))
            .and(OrderSpecification.byUserNot(currentUser.getId()))
            .and(OrderSpecification.orderByCryptocurrencyAmount(true)));
        break;
      case BUY:
        orderList = orderDetailsRepository.findAll(
          OrderSpecification.byOrderStatus(OrderStatus.CREATED)
            .and(OrderSpecification.byOrderType(orderType))
            .and(OrderSpecification.byUserNot(currentUser.getId()))
            .and(OrderSpecification.orderByCryptocurrencyAmount(false)));
        break;
      default:
        orderList = new ArrayList<>();
    }

    if (orderList.isEmpty()) {
      throw new NoActiveOrdersFoundException();
    }
    auditTrail.setUser(userService.getCurrentUser());
//    auditTrail.setDescription("Orders with id= " + order.getId() + " was cancelled");
    auditTrailRepository.save(auditTrail);
    return orderMapper.toRestDto(orderList);
  }
}
