package sigma.training.ctp.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import sigma.training.ctp.dictionary.OrderType;
import sigma.training.ctp.dictionary.OrderStatus;

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
import javax.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "creation_date")
  @Generated(GenerationTime.INSERT)
  private Instant creationDate;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  @Column(name = "order_status")
  @Enumerated(EnumType.STRING)
  @Generated(GenerationTime.INSERT)
  private OrderStatus orderStatus;

  @Column(name = "order_type")
  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @Column(name = "cryptocurrency_price")
  @NonNull
  @DecimalMin(value = "0.00")
  private BigDecimal cryptocurrencyPrice;

  @Column(name = "cryptocurrency_amount")
  @NonNull
  @DecimalMin(value = "0.00")
  private BigDecimal cryptocurrencyAmount;


  public OrderDetailsEntity(UserEntity user, OrderType orderType, @NonNull BigDecimal cryptocurrencyPrice, @NonNull BigDecimal cryptocurrencyAmount) {
    this.user = user;
    this.orderType = orderType;
    this.cryptocurrencyPrice = cryptocurrencyPrice;
    this.cryptocurrencyAmount = cryptocurrencyAmount;
  }
}
