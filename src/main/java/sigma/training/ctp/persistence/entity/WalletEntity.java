package sigma.training.ctp.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NonNull
    private UserEntity user;

    @Column(name = "money_balance")
    @NonNull
    private BigDecimal moneyBalance;

    @Column(name = "cryptocurrency_balance")
    @NonNull
    private BigDecimal cryptocurrencyBalance;
}