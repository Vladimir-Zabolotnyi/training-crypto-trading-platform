package sigma.training.ctp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sigma.training.ctp.persistence.entity.WalletEntity;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
  List<WalletEntity> findAllByUserId(Long id);

  WalletEntity findWalletEntityByUserIdAndCurrencyName(Long id,String name);

  WalletEntity findWalletEntityById(Long id);

}
