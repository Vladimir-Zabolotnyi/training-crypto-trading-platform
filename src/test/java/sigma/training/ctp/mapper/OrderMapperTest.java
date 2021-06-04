package sigma.training.ctp.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.dictionary.OrderType;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {
  private static final Long ID = 1L;
  private static final OrderType ORDER_TYPE = OrderType.SELL;
  private static final BigDecimal CRYPTOCURRENCY_AMOUNT = new BigDecimal("20");
  private static final BigDecimal CRYPTOCURRENCY_PRICE = new BigDecimal("400");
  private static final OrderStatus ORDER_STATUS = OrderStatus.CREATED;
  private static final UserEntity USER = new UserEntity();
  private static final Instant CREATION_DATE = Instant.ofEpochMilli(1000);

  private static final OrderDetailsEntity ORDER = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, ORDER_STATUS,
    ORDER_TYPE,
    CRYPTOCURRENCY_PRICE,
    CRYPTOCURRENCY_AMOUNT);
  private static final OrderDetailsRestDto ORDER_DTO = new OrderDetailsRestDto(
    ID, CREATION_DATE,
    USER.getId(), ORDER_STATUS,
    ORDER_TYPE,
    CRYPTOCURRENCY_PRICE,
    CRYPTOCURRENCY_AMOUNT);

  private static final OrderDetailsRestDto ORDER_DTO_WITHOUT_USER = new OrderDetailsRestDto(
    ID, CREATION_DATE,
    null, ORDER_STATUS,
    ORDER_TYPE,
    CRYPTOCURRENCY_PRICE,
    CRYPTOCURRENCY_AMOUNT);

  OrderMapper orderMapper;

  @BeforeEach
  void setUp() {
    orderMapper = new OrderMapper();
  }

  @Test
  void toRestDto() {
    OrderDetailsRestDto orderDtoActual = orderMapper.toRestDto(ORDER);
    assertEquals(ORDER_DTO, orderDtoActual);
  }

  @Test
  void toEntity() {
    OrderDetailsEntity orderActual = orderMapper.toEntity(ORDER_DTO);
    assertEquals(ORDER, orderActual);
  }

  @Test
  void toEntityWithUser() {
    OrderDetailsEntity orderActual = orderMapper.toEntity(ORDER_DTO_WITHOUT_USER,USER);
    assertEquals(ORDER, orderActual);
  }
}
