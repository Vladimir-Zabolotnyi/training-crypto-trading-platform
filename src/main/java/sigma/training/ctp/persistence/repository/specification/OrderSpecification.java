package sigma.training.ctp.persistence.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.dictionary.OrderType;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;

import javax.persistence.criteria.JoinType;

public interface OrderSpecification {

  static Specification<OrderDetailsEntity> byOrderStatus(OrderStatus orderStatus) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orderStatus"), criteriaBuilder.literal(orderStatus));
  }


  static Specification<OrderDetailsEntity> byOrderType(OrderType orderType) {
    return (root, query, criteriaBuilder) ->
      criteriaBuilder.equal(root.get("orderType"), criteriaBuilder.literal(orderType));
  }

  static Specification<OrderDetailsEntity> byUserNot(Long id) {
    return (root, query, criteriaBuilder) ->
      criteriaBuilder.notEqual(
        root.join("user", JoinType.INNER).get("id"),
        criteriaBuilder.literal(id));
  }
}
