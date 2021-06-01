package sigma.training.ctp.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
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

  @Column(name = "money_balance")
  @NonNull
  private BigDecimal cryptocurrencyPrice;

  @Column(name = "cryptocurrency_balance")
  @NonNull
  private BigDecimal cryptocurrencyAmount;


  public OrderDetailsEntity(OrderDetailsEntity orderFromBody, OrderType orderType, UserEntity user) {
    this.creationDate = Instant.now();
    this.user = user;
    this.status = status;
    this.orderType = orderType;
    this.cryptocurrencyPrice = orderFromBody.getCryptocurrencyPrice();
    this.cryptocurrencyAmount = orderFromBody.getCryptocurrencyPrice();
  }
}
