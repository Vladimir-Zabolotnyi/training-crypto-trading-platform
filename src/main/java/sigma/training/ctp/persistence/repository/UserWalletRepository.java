package sigma.training.ctp.persistence.repository;

import com.sun.tools.javac.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.UserWalletEntity;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWalletEntity, Long> {
    List<UserWalletEntity> findAllByUser(UserEntity user);
}
