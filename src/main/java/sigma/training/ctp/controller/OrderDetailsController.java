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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import sigma.training.ctp.dictionary.OrderType;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.exception.InsufficientAmountBankCurrencyException;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.service.OrderDetailsService;
import sigma.training.ctp.service.UserService;
import sigma.training.ctp.view.OrderDetailsViewModel;

import java.util.Locale;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Controller", description = "Allows to create/cancel/fulfill orders(buy/sell)")
public class OrderDetailsController {

  @Autowired
  UserService userService;
  @Autowired
  OrderDetailsService orderDetailsService;


  @Operation(summary = "Create Order", description = "Allows to create order")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "order created",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailsRestDto.class))),
    @ApiResponse(responseCode = "400", description = "Insufficient amount of cryptocurrency in the wallet",
      content = @Content(mediaType = "text/plain"))
  })
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = "/{orderType}")
  public @ResponseBody
  OrderDetailsRestDto postOrder(@RequestBody
                                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "cryptocurrency amount and price",
                                  content = @Content(schema = @Schema(implementation = OrderDetailsViewModel.class))) OrderDetailsRestDto order,
                                @PathVariable("orderType") @Parameter(
                                  description = "type of the order",
                                  schema = @Schema(allowableValues = {"buy", "sell", "SELL", "BUY"})) String orderType) throws InsufficientAmountCryptoException {

    order.setOrderType(OrderType.valueOf(orderType.toUpperCase(Locale.ROOT)));
    return orderDetailsService.postOrder(order, userService.getCurrentUser());

  }

  @Operation(summary = "Fulfill Order", description = "Allows to fulfill order")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Order fulfilled",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailsRestDto.class))),
    @ApiResponse(responseCode = "400", description = "Insufficient amount of bank currency in the wallet",
      content = @Content(mediaType = "text/plain")),
    @ApiResponse(responseCode = "400", description = "Order already fulfilled by another user",
      content = @Content(mediaType = "text/plain")),
    @ApiResponse(responseCode = "400", description = "Order already cancelled",
      content = @Content(mediaType = "text/plain")),
    @ApiResponse(responseCode = "401", description = "Order not found",
      content = @Content(mediaType = "text/plain"))
  })
  @ResponseStatus(HttpStatus.OK)
  @PostMapping(path = "/{id}/fulfill")
  public @ResponseBody
  OrderDetailsRestDto fulfillOrder(
    @PathVariable("id") @Parameter(
      description = "id of the order",
      content = @Content(schema = @Schema(implementation = Long.class))) Long id)
    throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException, InsufficientAmountBankCurrencyException {
    UserEntity user = (UserEntity) SecurityContextHolder.getContext()
      .getAuthentication()
      .getPrincipal();
    return orderDetailsService.fulfillOrder(id, user);

  }
}
