package sigma.training.ctp.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;
import sigma.training.ctp.service.OrderDetailsService;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
public class OrderCancellingJob {
  @Autowired
  OrderDetailsService orderDetailsService;


  @Value("${order.time-to-live-days}")
  private String ttl;

  @Scheduled(cron = "${order-cancelling-job.scheduled-cron-expression}")
  public void cancelExpiredOrders() throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException {
    orderDetailsService.cancelOutdatedOrders(Instant.now().minusMillis(TimeUnit.DAYS.toMillis(Long.parseLong(ttl))));

  }
}
