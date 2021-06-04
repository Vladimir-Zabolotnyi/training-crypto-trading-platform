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
import java.math.BigDecimal;
import java.time.Instant;

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
  public OrderDetailsRestDto fulfillOrder(Long order_id,UserEntity current_user) throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException, InsufficientAmountBankCurrencyException, CannotFulfillOwnOrderException {
    OrderDetailsEntity order = orderDetailsRepository.findById(order_id).orElseThrow(() -> new OrderNotFoundException(order_id));
    if (order.getOrderStatus() == OrderStatus.CANCELLED) {
      throw new OrderAlreadyCancelledException(order_id);
    }
    if (order.getOrderStatus() == OrderStatus.FULFILLED) {
      throw new OrderAlreadyFulfilledException(order_id);
    }
    if (order.getUser().getId() == current_user.getId()){
      throw new CannotFulfillOwnOrderException();
    }
    walletService.purchaseCryptocurrency(current_user.getId(),order.getUser().getId(),order.getCryptocurrencyAmount(),order.getCryptocurrencyPrice());
    order.setOrderStatus(OrderStatus.FULFILLED);
    return orderMapper.toRestDto(order);
  }
}
