package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.persistence.entity.CurrencyEntity;
import sigma.training.ctp.persistence.repository.CryptocurrencyRepository;

import java.util.List;

@Service
public class CurrencyService {

  @Autowired
  private CryptocurrencyRepository cryptocurrencyRepository;

  public List<CurrencyEntity> findCurrencyEntitiesByBankCurrencyEquals(boolean isBankCurrency) {
    return cryptocurrencyRepository.findCurrencyEntitiesByBankCurrencyEquals(isBankCurrency);
  }
}
