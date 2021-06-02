package sigma.training.ctp.persistence.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import sigma.training.ctp.enums.OrderType;
import sigma.training.ctp.enums.Status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "creation_date")
  private Instant creationDate;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "order_type")
  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @Column(name = "cryptocurrency_price")
  @NonNull
  private BigDecimal cryptocurrencyPrice;

  @Column(name = "cryptocurrency_amount")
  @NonNull
  private BigDecimal cryptocurrencyAmount;


  public OrderDetailsEntity(Instant creationDate, UserEntity user, Status status, OrderType orderType, @NonNull BigDecimal cryptocurrencyPrice, @NonNull BigDecimal cryptocurrencyAmount) {
    this.creationDate = creationDate;
    this.user = user;
    this.status = status;
    this.orderType = orderType;
    this.cryptocurrencyPrice = cryptocurrencyPrice;
    this.cryptocurrencyAmount = cryptocurrencyAmount;
  }
}
