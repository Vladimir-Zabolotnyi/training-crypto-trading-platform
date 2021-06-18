package sigma.training.ctp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.training.ctp.persistence.entity.CurrencyEntity;


@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
}
