package sigma.training.ctp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity, Long>, JpaSpecificationExecutor<OrderDetailsEntity> {


  List<OrderDetailsEntity> findAllByOrderStatusAndCreationDateLessThan(OrderStatus orderStatus, Instant toDate);
}
