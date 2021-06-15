package sigma.training.ctp.persistence;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.dictionary.OrderType;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderFilter {
  private OrderStatus orderStatus;

  private OrderType orderType;

  private Long userId;
}
