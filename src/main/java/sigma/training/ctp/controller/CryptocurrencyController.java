package sigma.training.ctp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.dto.CryptocurrencyRestDto;
import sigma.training.ctp.service.CryptocurrencyService;

import java.util.List;

@RestController
public class CryptocurrencyController {

  @Autowired
  private CryptocurrencyService cryptocurrencyService;

  @GetMapping(path = "/currencies")
  public List<CryptocurrencyRestDto> getAllCryptocurrencies() {
    return cryptocurrencyService.findCurrencyEntitiesByBankCurrencyEquals(false);
  }
}
