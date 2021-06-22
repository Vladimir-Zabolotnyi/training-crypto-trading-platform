package sigma.training.ctp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.training.ctp.persistence.entity.CurrencyEntity;

import java.util.List;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
  List<CurrencyEntity> findCurrencyEntitiesByBankCurrencyEquals(boolean isBankCurrency);
}
