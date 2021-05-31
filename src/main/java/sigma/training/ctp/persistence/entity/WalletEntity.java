package sigma.training.ctp.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet")
@Data
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "money_balance")
    private BigDecimal moneyBalance;

    @Column(name = "cryptocurrency_balance")
    private BigDecimal cryptocurrencyBalance;
}