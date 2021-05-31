package sigma.training.ctp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.training.ctp.persistence.entity.WalletEntity;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
    WalletEntity findUserWalletEntityByUserId(Long id);
}