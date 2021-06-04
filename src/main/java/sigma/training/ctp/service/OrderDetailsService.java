package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
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
    OrderDetailsEntity order = orderMapper.toEntity(orderDto,user);
    walletService.reduceWalletCryptocurrencyBalanceByUserId(order.getUser().getId(), order.getCryptocurrencyAmount());
    return orderMapper.toRestDto(orderDetailsRepository.save(order)) ;
  }
}
