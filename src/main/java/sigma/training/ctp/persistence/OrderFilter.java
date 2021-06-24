package sigma.training.ctp.persistence;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.training.ctp.dictionary.OrderStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderFilter {

  private OrderStatus orderStatus;

  private Long userId;

  private String sellCurrencyName;

  private String buyCurrencyName;
}
