package sigma.training.ctp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.training.ctp.persistence.entity.UserWalletEntity;

import java.util.List;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWalletEntity, Long> {
    List<UserWalletEntity> findUserWalletEntitiesByUserId(Long id);
}