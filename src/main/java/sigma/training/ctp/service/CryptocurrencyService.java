package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.CryptocurrencyRestDto;
import sigma.training.ctp.mapper.CryptocurrencyMapper;
import sigma.training.ctp.persistence.repository.CryptocurrencyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CryptocurrencyService {

  @Autowired
  private CryptocurrencyRepository cryptocurrencyRepository;

  @Autowired
  private CryptocurrencyMapper cryptocurrencyMapper;

  public List<CryptocurrencyRestDto> findCurrencyEntitiesByBankCurrencyEquals(boolean isBankCurrency) {
    return cryptocurrencyRepository.findCurrencyEntitiesByBankCurrencyEquals(isBankCurrency).stream()
      .map(m -> cryptocurrencyMapper.toRestDto(m))
      .collect(Collectors.toList());
  }
}
