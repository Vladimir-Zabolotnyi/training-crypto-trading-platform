package sigma.training.ctp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.dto.CurrencyRestDto;
import sigma.training.ctp.service.CurrencyService;

import java.util.List;

@Tag(
  name = "Currency Controller",
  description = "Allows to obtain the data about all available currencies on the trade"
)
@RestController
public class CurrencyController {

  @Autowired
  private CurrencyService currencyService;

  @Operation(
    summary = "Returns the full list of the cryptocurrencies",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "The information about the currency id, name and acronym",
        content = @Content(schema = @Schema(implementation = CurrencyRestDto.class))
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Unauthorized"
      ),
      @ApiResponse(
        responseCode = "405",
        description = "Method not allowed"
      ),
    }
  )
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/currencies")
  public List<CurrencyRestDto> getAllCurrencies() {
    return currencyService.getAllCurrencies();
  }
}
