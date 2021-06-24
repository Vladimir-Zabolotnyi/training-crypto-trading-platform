package sigma.training.ctp.persistence.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;

import javax.persistence.criteria.JoinType;

public interface OrderSpecification {

  static Specification<OrderDetailsEntity> byOrderStatus(OrderStatus orderStatus) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orderStatus"), criteriaBuilder.literal(orderStatus));
  }

  static Specification<OrderDetailsEntity> bySellCurrencyName(String sellCurrencyName){
    return (root, query, criteriaBuilder) ->
      criteriaBuilder.equal(
        root.join("sellCurrency",JoinType.INNER).get("name"),criteriaBuilder.literal(sellCurrencyName));
  }

  static Specification<OrderDetailsEntity> byBuyCurrencyName(String buyCurrencyName){
    return (root, query, criteriaBuilder) ->
      criteriaBuilder.equal(
        root.join("buyCurrency",JoinType.INNER).get("name"),criteriaBuilder.literal(buyCurrencyName));
  }


  static Specification<OrderDetailsEntity> byUser(Long id) {
    return (root, query, criteriaBuilder) ->
      criteriaBuilder.equal(
        root.join("user", JoinType.INNER).get("id"), criteriaBuilder.literal(id));
  }

}
