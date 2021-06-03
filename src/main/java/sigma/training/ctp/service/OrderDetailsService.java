package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.dictionary.OrderType;
import sigma.training.ctp.persistence.repository.OrderDetailsRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class OrderDetailsService {
  @Autowired
  OrderDetailsRepository orderDetailsRepository;

  @Autowired
  WalletService walletService;

  public OrderDetailsRestDto saveOrder(OrderStatus orderStatus, BigDecimal cryptocurrencyPrice, BigDecimal cryptocurrencyAmount, OrderType orderType, UserEntity user) throws InsufficientAmountCryptoException {
    OrderDetailsEntity order = new OrderDetailsEntity(user, orderStatus, orderType, cryptocurrencyPrice, cryptocurrencyAmount);
    OrderDetailsEntity savedOrder = saveOrder(order);
    return new OrderDetailsRestDto(
      savedOrder.getId(), savedOrder.getCreationDate(),
      savedOrder.getUser().getId(), savedOrder.getOrderStatus(),
      savedOrder.getOrderType(), savedOrder.getCryptocurrencyPrice(),
      savedOrder.getCryptocurrencyAmount());
  }

  @Transactional
  public OrderDetailsEntity saveOrder(OrderDetailsEntity orderToBeSaved) throws InsufficientAmountCryptoException {
    walletService.reduceWalletCryptocurrencyBalanceByUserId(orderToBeSaved.getUser().getId(), orderToBeSaved.getCryptocurrencyAmount());
    return orderDetailsRepository.save(orderToBeSaved);
  }
}
