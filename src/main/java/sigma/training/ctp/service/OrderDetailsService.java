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
import sigma.training.ctp.mapper.OrderMapper;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.dictionary.OrderType;
import sigma.training.ctp.persistence.repository.OrderDetailsRepository;

import javax.transaction.Transactional;

@Service
public class OrderDetailsService {
  @Autowired
  OrderDetailsRepository orderDetailsRepository;

  @Autowired
  WalletService walletService;

  @Autowired
  OrderMapper orderMapper;

  @Transactional
  public OrderDetailsRestDto postOrder(OrderDetailsRestDto orderDto, UserEntity user) throws InsufficientAmountCryptoException, InsufficientAmountBankCurrencyException {
    OrderDetailsEntity order = orderMapper.toEntity(orderDto, user);
    switch (order.getOrderType()) {
      case SELL:
        walletService.reduceWalletCryptocurrencyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount());
        break;
      case BUY:
        walletService.subtractWalletMoneyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount(), order.getCryptocurrencyPrice());
        break;
    }
    return orderMapper.toRestDto(orderDetailsRepository.save(order));
  }

  @Transactional
  public OrderDetailsRestDto fulfillOrder(Long orderId, UserEntity currentUser) throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException, CannotFulfillOwnOrderException, InsufficientAmountCryptoException {
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
    walletService.reduceWalletCryptocurrencyBalanceByUserId(currentUser.getId(), order.getCryptocurrencyAmount());
    walletService.addWalletMoneyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount(), order.getCryptocurrencyPrice());
    walletService.addWalletCryptocurrencyBalanceByUserId(currentUser.getId(), order.getCryptocurrencyAmount());
    order.setOrderStatus(OrderStatus.FULFILLED);
    return orderMapper.toRestDto(order);
  }
}
