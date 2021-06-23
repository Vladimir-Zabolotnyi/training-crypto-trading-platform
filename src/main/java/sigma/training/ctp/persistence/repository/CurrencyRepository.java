package sigma.training.ctp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.training.ctp.persistence.entity.CurrencyEntity;
import sigma.training.ctp.persistence.entity.WalletEntity;

import java.util.Optional;


@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
  Optional<CurrencyEntity> findByName(String name);
}
