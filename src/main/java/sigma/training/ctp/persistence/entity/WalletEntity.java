package sigma.training.ctp.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class WalletEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @NonNull
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "currency_id", referencedColumnName = "id")
  @NonNull
  private CurrencyEntity currency;

  @Column(name = "cryptocurrency_balance")
  @NonNull
  @DecimalMin(value = "0.00")
  private BigDecimal amount;


}
