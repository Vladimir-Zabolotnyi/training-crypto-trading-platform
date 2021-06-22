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
import sigma.training.ctp.dto.CryptocurrencyRestDto;
import sigma.training.ctp.service.CryptocurrencyService;

import java.util.List;

@Tag(
  name = "Cryptocurrency Controller",
  description = "Allows to obtain the data about all available cryptocurrencies on the trade"
)
@RestController
public class CryptocurrencyController {

  @Autowired
  private CryptocurrencyService cryptocurrencyService;

  @Operation(
    summary = "Returns the list of all user's cryptocurrencies",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "The information about the currency id, name and acronym",
        content = @Content(schema = @Schema(implementation = CryptocurrencyRestDto.class))
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
  public List<CryptocurrencyRestDto> getAllCryptocurrencies() {
    return cryptocurrencyService.findCurrencyEntitiesByBankCurrencyEquals(false);
  }
}
