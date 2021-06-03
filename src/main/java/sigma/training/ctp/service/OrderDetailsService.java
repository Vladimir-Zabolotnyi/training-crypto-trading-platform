package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.enums.Status;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.enums.OrderType;
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

    @Transactional
  public OrderDetailsRestDto saveOrder(Status status, BigDecimal cryptocurrencyPrice, BigDecimal cryptocurrencyAmount, OrderType orderType, UserEntity user) throws InsufficientAmountCryptoException {
    OrderDetailsEntity orderToBeSaved = new OrderDetailsEntity(Instant.now(),user, status, orderType, cryptocurrencyPrice, cryptocurrencyAmount);
    walletService.reduceWalletCryptocurrencyBalanceByUserId(orderToBeSaved.getUser().getId(), orderToBeSaved.getCryptocurrencyAmount());
    OrderDetailsEntity savedOrder = orderDetailsRepository.save(orderToBeSaved);
    return new OrderDetailsRestDto(
      savedOrder.getId(), savedOrder.getCreationDate(),
      savedOrder.getUser().getId(), savedOrder.getStatus(),
      savedOrder.getOrderType(), savedOrder.getCryptocurrencyPrice(),
      savedOrder.getCryptocurrencyAmount());
  }
}
