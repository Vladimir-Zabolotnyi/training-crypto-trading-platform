package sigma.training.ctp.persistence.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;

import javax.persistence.criteria.JoinType;

public interface OrderSpecification {

  static Specification<OrderDetailsEntity> byOrderStatus(OrderStatus orderStatus) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orderStatus"), criteriaBuilder.literal(orderStatus));
  }
//todo
  static Specification<OrderDetailsEntity> byCurrencyName(String currencyName){
    return (root, query, criteriaBuilder) ->
      criteriaBuilder.equal(
        root.join("currency",JoinType.INNER).get("c.name"),criteriaBuilder.literal(currencyName));
  }


  static Specification<OrderDetailsEntity> byUser(Long id) {
    return (root, query, criteriaBuilder) ->
      criteriaBuilder.equal(
        root.join("user", JoinType.INNER).get("id"), criteriaBuilder.literal(id));
  }

}
