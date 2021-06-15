package sigma.training.ctp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.training.ctp.persistence.entity.AuditTrail;

@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrail,Long> {
}
