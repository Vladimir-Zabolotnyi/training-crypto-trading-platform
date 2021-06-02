package sigma.training.ctp.persistence.entity;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import sigma.training.ctp.persistence.entity.enums.OrderType;
import sigma.training.ctp.persistence.entity.enums.Status;

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
@Schema
public class OrderDetailsEntity {

  @Hidden
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Hidden
  @Column(name = "creation_date")
  private Instant creationDate;

  @Hidden
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  @Schema(description = "status of the order", enumAsRef = true,example = "created")
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Hidden
  @Column(name = "order_type")
  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @Schema(description = "price of the cryptocurrency", example = "400.00")
  @Column(name = "cryptocurrency_price")
  @NonNull
  private BigDecimal cryptocurrencyPrice;

  @Schema(description = "amount of the cryptocurrency", example = "20.00")
  @Column(name = "cryptocurrency_amount")
  @NonNull
  private BigDecimal cryptocurrencyAmount;

  public OrderDetailsEntity(Status status, @NonNull BigDecimal cryptocurrencyPrice, @NonNull BigDecimal cryptocurrencyAmount) {
    this.creationDate = Instant.now();
    this.status = status;
    this.cryptocurrencyPrice = cryptocurrencyPrice;
    this.cryptocurrencyAmount = cryptocurrencyAmount;
  }

  public OrderDetailsEntity(Instant creationDate, UserEntity user, Status status, OrderType orderType, @NonNull BigDecimal cryptocurrencyPrice, @NonNull BigDecimal cryptocurrencyAmount) {
    this.creationDate = creationDate;
    this.user = user;
    this.status = status;
    this.orderType = orderType;
    this.cryptocurrencyPrice = cryptocurrencyPrice;
    this.cryptocurrencyAmount = cryptocurrencyAmount;
  }

  public OrderDetailsEntity(Instant creationDate, OrderDetailsEntity orderFromBody, OrderType orderType, UserEntity user) {
    this.creationDate = creationDate;
    this.user = user;
    this.status = orderFromBody.status;
    this.orderType = orderType;
    this.cryptocurrencyPrice = orderFromBody.getCryptocurrencyPrice();
    this.cryptocurrencyAmount = orderFromBody.getCryptocurrencyAmount();
  }
}
