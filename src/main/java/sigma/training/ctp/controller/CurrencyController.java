package sigma.training.ctp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sigma.training.ctp.persistence.entity.CurrencyEntity;
import sigma.training.ctp.service.CurrencyService;

import java.util.List;

@RestController
public class CurrencyController {

  @Autowired
  private CurrencyService currencyService;

  @GetMapping(path = "/currencies")
  public List<CurrencyEntity> getAllCryptocurrencies() {
    return currencyService.findCurrencyEntitiesByBankCurrencyEquals(false);
  }
}
