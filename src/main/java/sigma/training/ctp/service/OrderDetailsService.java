package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.enums.OrderType;
import sigma.training.ctp.persistence.repository.OrderDetailsRepository;

@Service
public class OrderDetailsService {
  @Autowired
  OrderDetailsRepository orderDetailsRepository;

  @Autowired
  WalletService walletService;


  public OrderDetailsRestDto saveOrder(OrderDetailsEntity orderFromBody, OrderType orderType, UserEntity user) {
    OrderDetailsEntity orderToBeSaved = new OrderDetailsEntity(orderFromBody, orderType, user);
    walletService.reduceWalletCryptocurrencyBalanceByUserId(orderToBeSaved.getUser().getId(), orderToBeSaved.getCryptocurrencyAmount());
    OrderDetailsEntity savedOrder = orderDetailsRepository.save(orderToBeSaved);
    return new OrderDetailsRestDto(savedOrder);

  }

}
