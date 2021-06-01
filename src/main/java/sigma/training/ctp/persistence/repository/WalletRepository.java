package sigma.training.ctp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sigma.training.ctp.persistence.entity.WalletEntity;

import java.math.BigDecimal;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
  WalletEntity findWalletEntityByUserId(Long id);

  @Modifying
  @Query(
    value = "update wallet set cryptocurrency_balance= :cryptocurrencyBalance where user_id= :user_id",
    nativeQuery = true)
  void updateWalletEntityCryptocurrencyBalanceByUserId(@Param("user_id") Long id,
                                                       @Param("cryptocurrencyBalance") BigDecimal cryptocurrencyBalance);
}
