package sigma.training.ctp.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import sigma.training.ctp.persistence.entity.enums.OrderType;
import sigma.training.ctp.persistence.entity.enums.Status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor

public class OrderDetailsEntity {
  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private Instant creationDate;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  @Column(name = "status")
  private Status status;

  @Column(name = "order_type")
  private OrderType orderType;

  @Column(name = "cryptocurrency_price")
  @NonNull
  private BigDecimal cryptocurrencyPrice;

  @Column(name = "cryptocurrency_amount")
  @NonNull
  private BigDecimal cryptocurrencyAmount;

  public OrderDetailsEntity(UserEntity user, Status status, OrderType orderType, @NonNull BigDecimal cryptocurrencyPrice, @NonNull BigDecimal cryptocurrencyAmount) {
    this.creationDate = Instant.now();
    this.user = user;
    this.status = status;
    this.orderType = orderType;
    this.cryptocurrencyPrice = cryptocurrencyPrice;
    this.cryptocurrencyAmount = cryptocurrencyAmount;
  }

  public OrderDetailsEntity(OrderDetailsEntity orderFromBody, OrderType orderType, UserEntity user) {
    this.creationDate = orderFromBody.getCreationDate();
    this.user = user;
    this.status = orderFromBody.status;
    this.orderType = orderType;
    this.cryptocurrencyPrice = orderFromBody.getCryptocurrencyPrice();
    this.cryptocurrencyAmount = orderFromBody.getCryptocurrencyAmount();
  }
}
