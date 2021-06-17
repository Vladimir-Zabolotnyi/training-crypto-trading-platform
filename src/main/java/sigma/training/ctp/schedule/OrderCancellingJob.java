package sigma.training.ctp.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sigma.training.ctp.dto.OrderFilterDto;
import sigma.training.ctp.exception.NoActiveOrdersFoundException;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;
import sigma.training.ctp.service.OrderDetailsService;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class OrderCancellingJob {
  @Autowired
  OrderDetailsService orderDetailsService;

  @Value("${order.time_to_live}")
  private String ttl;

  @Scheduled(cron = "${order_cancelling_job.scheduled_cron_expression}")
  public void expiredOrderCancelling() throws NoActiveOrdersFoundException {
    OrderFilterDto orderFilterDto = new OrderFilterDto();
    orderDetailsService.getAllOrders(orderFilterDto).stream().peek(orderDetailsRestDto -> {

        if (Instant.now().compareTo(
          orderDetailsRestDto.getCreationDate()
            .plusMillis(TimeUnit.DAYS.toMillis(Long.parseLong(ttl)))) > 0) {
          try {
            orderDetailsService.cancelOrder(orderDetailsRestDto.getId());
          } catch (OrderNotFoundException | OrderAlreadyCancelledException | OrderAlreadyFulfilledException e) {
            e.printStackTrace();
          }
        }
      }
    ).collect(Collectors.toList());

  }
}
