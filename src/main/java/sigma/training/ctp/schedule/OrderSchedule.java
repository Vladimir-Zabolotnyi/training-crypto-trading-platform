package sigma.training.ctp.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sigma.training.ctp.dto.OrderFilterDto;
import sigma.training.ctp.exception.NoActiveOrdersFoundException;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;
import sigma.training.ctp.service.OrderDetailsService;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.stream.Collectors;

@Component
public class OrderSchedule {
  @Autowired
  OrderDetailsService orderDetailsService;

  @Scheduled(fixedDelay = 90000L,initialDelay = 60000L)
  public void scheduleOrderTtl() throws NoActiveOrdersFoundException {
    OrderFilterDto orderFilterDto = new OrderFilterDto();
//    orderFilterDto.setOrderType("sell");
    orderDetailsService.getAllOrders(orderFilterDto).stream().peek(orderDetailsRestDto -> {
      if (Instant.now().compareTo(orderDetailsRestDto.getCreationDate().plusMillis(600000000L)) > 0) {
        try {
          orderDetailsService.cancelOrder(orderDetailsRestDto.getId());
        } catch (OrderNotFoundException | OrderAlreadyCancelledException | OrderAlreadyFulfilledException e) {
          e.printStackTrace();
        }
      }
      }
    ).collect(Collectors.toList());

  }

//  public static void main(String[] args) {
//
//    System.out.println(Instant.now());
//    System.out.println(Instant.ofEpochMilli(1623000000000L));
//    System.out.println(Instant.ofEpochMilli(1623000000000L).plusMillis(600000000L));
//
//  }
}
