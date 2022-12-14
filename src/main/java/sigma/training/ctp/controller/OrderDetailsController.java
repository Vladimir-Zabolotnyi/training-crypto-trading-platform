package sigma.training.ctp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.dto.OrderFilterDto;
import sigma.training.ctp.exception.CannotFulfillOwnOrderException;
import sigma.training.ctp.exception.InsufficientCurrencyAmountException;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;
import sigma.training.ctp.exception.NoActiveOrdersFoundException;
import sigma.training.ctp.exception.WalletNotFoundException;
import sigma.training.ctp.service.AuditTrailService;
import sigma.training.ctp.service.OrderDetailsService;
import sigma.training.ctp.service.UserService;
import sigma.training.ctp.view.OrderDetailsViewModel;

import java.util.List;


@RestController
@RequestMapping("/orders")
@Tag(name = "Order Controller", description = "Allows to create/cancel/fulfill orders(buy/sell)")
public class OrderDetailsController {

  @Autowired
  UserService userService;
  @Autowired
  OrderDetailsService orderDetailsService;

  @Autowired
  AuditTrailService auditTrailService;

  @Operation(summary = "Create Order", description = "Allows to create order")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "order created",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailsRestDto.class))),
    @ApiResponse(responseCode = "400", description = "Insufficient amount of currency in the wallet",
      content = @Content(mediaType = "text/plain"))
  })
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public @ResponseBody
  OrderDetailsRestDto postOrder(@RequestBody
                                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "currency amount and name",
                                  content = @Content(schema = @Schema(implementation = OrderDetailsViewModel.class)))
                                  OrderDetailsRestDto order) throws InsufficientCurrencyAmountException, WalletNotFoundException{

    OrderDetailsRestDto orderDto = orderDetailsService.postOrder(order, userService.getCurrentUser());
    auditTrailService.postAuditTrail("User created the order (id: " + orderDto.getId() + ")");
    return orderDto;
  }


  @Operation(summary = "Fulfill Order", description = "Allows to fulfill order")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Order fulfilled",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailsRestDto.class))),
    @ApiResponse(responseCode = "400", description = "Insufficient amount of  currency in the wallet",
      content = @Content(mediaType = "text/plain")),
    @ApiResponse(responseCode = "403", description = "Order already fulfilled or cancelled",
      content = @Content(mediaType = "text/plain")),
    @ApiResponse(responseCode = "404", description = "Order not found",
      content = @Content(mediaType = "text/plain")),
    @ApiResponse(responseCode = "405", description = "You are not able to fulfill own order",
      content = @Content(mediaType = "text/plain"))
  })
  @ResponseStatus(HttpStatus.OK)
  @PostMapping(path = "/{id}/fulfill")
  public @ResponseBody
  OrderDetailsRestDto fulfillOrder(
    @PathVariable("id") @Parameter(
      description = "id of the order",
      content = @Content(schema = @Schema(implementation = Long.class))) Long id)
    throws OrderNotFoundException, OrderAlreadyCancelledException, OrderAlreadyFulfilledException,
    CannotFulfillOwnOrderException, InsufficientCurrencyAmountException, WalletNotFoundException {


    OrderDetailsRestDto orderDto = orderDetailsService.fulfillOrder(id, userService.getCurrentUser());
    auditTrailService.postAuditTrail("User fulfilled the order(id: " + orderDto.getId() + ")");
    return orderDto;
  }

  @Operation(summary = "Allows to cancel the order",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "The order has been successfully cancelled",
        content = @Content
          (schema = @Schema(
            description = "A model for the order details information", implementation = OrderDetailsRestDto.class)
          )
      ),
      @ApiResponse(
        responseCode = "403",
        description = "The order was fulfilled or cancelled"
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Unauthorized"
      ),
      @ApiResponse(
        responseCode = "404",
        description = "The proposed order's id can not be found"
      ),
    })
  @DeleteMapping(path = "/{id}")
  public @ResponseBody
  OrderDetailsRestDto cancelOrder(
    @PathVariable("id")
    @Parameter(
      description = "the id of the order",
      content = @Content(schema = @Schema(implementation = Long.class))) Long orderId)
    throws OrderNotFoundException, OrderAlreadyFulfilledException, OrderAlreadyCancelledException, WalletNotFoundException {

    OrderDetailsRestDto orderDto = orderDetailsService.cancelOrder(orderId);
    auditTrailService.postAuditTrail("User cancelled the order (id: " + orderDto.getId() + ")");
    return orderDto;
  }

  @Operation(summary = "Get all orders", description = "Allows to obtain information about all sell//buy  orders")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "orders are obtained",
      content = @Content(mediaType = "application/json", array = @ArraySchema(
        schema = @Schema(implementation = OrderDetailsRestDto.class)))),
    @ApiResponse(responseCode = "404", description = "No  orders were found",
      content = @Content(mediaType = "text/plain"))
  })
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public @ResponseBody
  List<OrderDetailsRestDto> getAllOrders(@Parameter(in = ParameterIn.QUERY,
    description = "order filter",
    schema = @Schema(implementation = OrderFilterDto.class)) OrderFilterDto orderFilterDto) throws NoActiveOrdersFoundException {
    List<OrderDetailsRestDto> orderDtoList = orderDetailsService.getAllOrders(orderFilterDto);
    auditTrailService.postAuditTrail("User read list of the orders");
    return orderDtoList;
  }
}
