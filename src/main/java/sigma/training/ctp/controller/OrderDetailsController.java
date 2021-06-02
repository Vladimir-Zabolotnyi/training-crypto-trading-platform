package sigma.training.ctp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.entity.enums.OrderType;
import sigma.training.ctp.service.OrderDetailsService;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Controller", description = "Allows to create/cancel/fulfill orders(buy/sell)")
public class OrderDetailsController {

  @Autowired
  OrderDetailsService orderDetailsService;


  @Operation(summary = "Create Order", description = "Allows to create order")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "order created",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailsRestDto.class)))
  })
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = "/{orderType}")
  public @ResponseBody
  OrderDetailsRestDto postOrder(@RequestBody
                                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "cryptocurrency amount and price",
                                  content = @Content(schema = @Schema(implementation = OrderDetailsEntity.class))) OrderDetailsEntity orderFromBody,
                                @PathVariable("orderType") @Parameter(description = "type of the order") OrderType orderType) {
    UserEntity user = (UserEntity) SecurityContextHolder.getContext()
      .getAuthentication()
      .getPrincipal();
    OrderDetailsEntity order = new OrderDetailsEntity(orderFromBody.getStatus(),orderFromBody.getCryptocurrencyPrice(), orderFromBody.getCryptocurrencyAmount());
    return orderDetailsService.saveOrder(order, orderType, user);
  }
}
