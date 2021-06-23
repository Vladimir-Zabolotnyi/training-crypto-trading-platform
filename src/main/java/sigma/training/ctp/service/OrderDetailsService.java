package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.dto.OrderFilterDto;
import sigma.training.ctp.exception.CannotFulfillOwnOrderException;
import sigma.training.ctp.exception.InsufficientCurrencyAmountException;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;
import sigma.training.ctp.exception.NoActiveOrdersFoundException;
import sigma.training.ctp.exception.WalletNotFoundException;
import sigma.training.ctp.mapper.OrderFilterMapper;
import sigma.training.ctp.mapper.OrderMapper;
import sigma.training.ctp.persistence.OrderFilter;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.repository.CurrencyRepository;
import sigma.training.ctp.persistence.repository.OrderDetailsRepository;
import sigma.training.ctp.persistence.repository.specification.OrderSpecification;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

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
  CurrencyRepository currencyRepository;

  @Autowired
  FeeService feeService;

  @Transactional
  public OrderDetailsRestDto postOrder(OrderDetailsRestDto orderDto, UserEntity user) throws InsufficientCurrencyAmountException, WalletNotFoundException {
    UserEntity rootUser = userService.getRootUser();
    OrderDetailsEntity order = orderMapper.toEntity(orderDto, user);
    order.setSellCurrency(currencyRepository.findByName(orderDto.getSellCurrencyName()).get());
    order.setBuyCurrency(currencyRepository.findByName(orderDto.getBuyCurrencyName()).get());
    BigDecimal fee = feeService.getOrderFee(orderDto.getSellCurrencyAmount());
    walletService.addWalletCurrencyAmountByWalletId(rootUser.getId(),fee);
    walletService.subtractWalletCurrencyAmountByWalletId(order.getUser().getId(), orderDto.getSellCurrencyName(), order.getSellCurrencyAmount().add(fee));
    return orderMapper.toRestDto(orderDetailsRepository.save(order));
  }

  @Transactional
  public OrderDetailsRestDto fulfillOrder(Long orderId, UserEntity currentUser) throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException, CannotFulfillOwnOrderException, InsufficientCurrencyAmountException, WalletNotFoundException {
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
    walletService.addWalletCurrencyAmountByWalletId(
      order.getUser().getId(), order.getBuyCurrency().getName(), order.getBuyCurrencyAmount());
    walletService.subtractWalletCurrencyAmountByWalletId(
      currentUser.getId(), order.getBuyCurrency().getName(), order.getBuyCurrencyAmount());
    walletService.addWalletCurrencyAmountByWalletId(
      currentUser.getId(), order.getSellCurrency().getName(), order.getSellCurrencyAmount());

    order.setOrderStatus(OrderStatus.FULFILLED);
    return orderMapper.toRestDto(order);
  }

  @Transactional
  public OrderDetailsRestDto cancelOrder(Long orderId)
    throws OrderNotFoundException, OrderAlreadyFulfilledException, OrderAlreadyCancelledException, WalletNotFoundException {
    OrderDetailsEntity order = orderDetailsRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));

    if (order.getOrderStatus().equals(OrderStatus.FULFILLED)) {
      throw new OrderAlreadyFulfilledException(orderId);
    }
    if (order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
      throw new OrderAlreadyCancelledException(orderId);
    }
    walletService.addWalletCurrencyAmountByWalletId(
      order.getUser().getId(), order.getSellCurrency().getName(), order.getSellCurrencyAmount());

    order.setOrderStatus(OrderStatus.CANCELLED);
    return orderMapper.toRestDto(orderDetailsRepository.save(order));
  }


  @Transactional
  public List<OrderDetailsRestDto> getAllOrders(OrderFilterDto orderFilterDto) throws NoActiveOrdersFoundException {
    OrderFilter orderFilter = orderFilterMapper.toEntity(orderFilterDto);
    List<OrderDetailsEntity> orderList = orderDetailsRepository.findAll(
      orderFilterToCriteria(orderFilter));
    if (orderList.isEmpty()) {
      throw new NoActiveOrdersFoundException();
    }

    return orderMapper.toRestDto(orderList);
  }

  @Transactional
  public List<OrderDetailsRestDto> cancelOutdatedOrders(Instant toDate) throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException, WalletNotFoundException {
    List<OrderDetailsEntity> orderToCancelList = orderDetailsRepository.findAllByOrderStatusAndCreationDateLessThan(OrderStatus.CREATED, toDate);
    for (OrderDetailsEntity order : orderToCancelList) {
      cancelOrder(order.getId());
    }
    return orderMapper.toRestDto(orderToCancelList);
  }


  private Specification<OrderDetailsEntity> orderFilterToCriteria(OrderFilter orderFilter) {


    if (orderFilter.getUserId() == null) {
      return OrderSpecification.byOrderStatus(OrderStatus.CREATED);
    }
    if (orderFilter.getOrderStatus() == null) {
      return OrderSpecification.byUser(orderFilter.getUserId());
    }
    return OrderSpecification.byUser(orderFilter.getUserId())
      .and(OrderSpecification.byOrderStatus(orderFilter.getOrderStatus()));
  }
}
