package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.CurrencyRestDto;
import sigma.training.ctp.mapper.CurrencyMapper;
import sigma.training.ctp.persistence.repository.CurrencyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

  @Autowired
  private CurrencyRepository currencyRepository;

  @Autowired
  private CurrencyMapper currencyMapper;

  public List<CurrencyRestDto> getAllCurrencies() {
    return currencyRepository.findAll().stream()
      .map(currencyMapper::toRestDto)
      .collect(Collectors.toList());
  }
}
