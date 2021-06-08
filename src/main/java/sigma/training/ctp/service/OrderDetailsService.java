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
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.dictionary.OrderType;
import sigma.training.ctp.persistence.repository.OrderDetailsRepository;
import sigma.training.ctp.persistence.repository.specification.OrderSpecification;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailsService {
  @Autowired
  OrderDetailsRepository orderDetailsRepository;

  @Autowired
  WalletService walletService;

  @Autowired
  OrderMapper orderMapper;

  @Transactional
  public OrderDetailsRestDto postOrder(OrderDetailsRestDto orderDto, UserEntity user) throws InsufficientAmountCryptoException {
    OrderDetailsEntity order = orderMapper.toEntity(orderDto, user);
    walletService.reduceWalletCryptocurrencyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount());
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
    walletService.subtractWalletMoneyBalanceByUserId(currentUser.getId(), order.getCryptocurrencyAmount(), order.getCryptocurrencyPrice());
    walletService.addWalletMoneyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount(), order.getCryptocurrencyPrice());
    walletService.addWalletCryptocurrencyBalanceByUserId(currentUser.getId(), order.getCryptocurrencyAmount());
    order.setOrderStatus(OrderStatus.FULFILLED);
    return orderMapper.toRestDto(order);
  }

  @Transactional
  public List<OrderDetailsRestDto> getAllOrders(OrderType orderType, UserEntity currentUser) throws NoActiveOrdersFoundException {
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
    return orderMapper.toRestDto(orderList);
  }
}
