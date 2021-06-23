package sigma.training.ctp.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
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

  @ManyToOne
  @JoinColumn(name = "sell_currency_id", referencedColumnName = "id")
  private CurrencyEntity sellCurrency;

  @ManyToOne
  @JoinColumn(name = "buy_currency_id", referencedColumnName = "id")
  private CurrencyEntity buyCurrency;

  @Column(name = "sell_currency_amount")
  @NonNull
  @DecimalMin(value = "0.00")
  private BigDecimal sellCurrencyAmount;

  @Column(name = "buy_currency_amount")
  @NonNull
  @DecimalMin(value = "0.00")
  private BigDecimal buyCurrencyAmount;

}
