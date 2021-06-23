package sigma.training.ctp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sigma.training.ctp.dto.CurrencyRestDto;
import sigma.training.ctp.mapper.CurrencyMapper;
import sigma.training.ctp.persistence.entity.CurrencyEntity;
import sigma.training.ctp.persistence.repository.CurrencyRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

  @Mock
  private CurrencyRepository repository;
  @Mock
  private CurrencyMapper mapper;

  @InjectMocks
  private CurrencyService service;

  private static final CurrencyEntity CURRENCY_ONE = new CurrencyEntity(1L, true, true, "USA_dollar", "USD");
  private static final CurrencyEntity CURRENCY_TWO = new CurrencyEntity(2L, false, true, "UA_hryvnia", "UAH");
  private static final CurrencyEntity CURRENCY_THREE = new CurrencyEntity(3L, false, false, "Bitcoin", "BTC");
  private static final CurrencyEntity CURRENCY_FOUR = new CurrencyEntity(4L, false, false, "Ethereum", "ETH");
  private static final CurrencyEntity CURRENCY_FIVE = new CurrencyEntity(5L, false, false, "Tron", "TRX");
  private static final CurrencyEntity CURRENCY_SIX = new CurrencyEntity(6L, false, false, "Ripple", "XRP");

  private static final List<CurrencyEntity> CURRENCIES = Stream.of(
    CURRENCY_ONE, CURRENCY_TWO, CURRENCY_THREE, CURRENCY_FOUR, CURRENCY_FIVE, CURRENCY_SIX
  ).collect(Collectors.toList());

  private static final List<CurrencyRestDto> CURRENCIES_REST_DTO = CURRENCIES.stream()
    .map(m -> new CurrencyRestDto(m.getId(), m.getName(), m.getAcronym()))
    .collect(Collectors.toList());

  @Test
  public void testFindAllCurrencies() {
    when(mapper.toRestDto(CURRENCY_ONE)).thenReturn(
      CURRENCIES_REST_DTO.stream()
        .filter(f -> f.getId().equals(CURRENCY_ONE.getId()))
        .findFirst()
        .orElse(CURRENCIES_REST_DTO.get(0))
    );
    when(mapper.toRestDto(CURRENCY_TWO)).thenReturn(
      CURRENCIES_REST_DTO.stream()
        .filter(f -> f.getId().equals(CURRENCY_TWO.getId()))
        .findFirst()
        .orElse(CURRENCIES_REST_DTO.get(1))
    );
    when(mapper.toRestDto(CURRENCY_THREE)).thenReturn(
      CURRENCIES_REST_DTO.stream()
        .filter(f -> f.getId().equals(CURRENCY_THREE.getId()))
        .findFirst()
        .orElse(CURRENCIES_REST_DTO.get(2))
    );
    when(mapper.toRestDto(CURRENCY_FOUR)).thenReturn(
      CURRENCIES_REST_DTO.stream()
        .filter(f -> f.getId().equals(CURRENCY_FOUR.getId()))
        .findFirst()
        .orElse(CURRENCIES_REST_DTO.get(3))
    );
    when(mapper.toRestDto(CURRENCY_FIVE)).thenReturn(
      CURRENCIES_REST_DTO.stream()
        .filter(f -> f.getId().equals(CURRENCY_FIVE.getId()))
        .findFirst()
        .orElse(CURRENCIES_REST_DTO.get(4))
    );
    when(mapper.toRestDto(CURRENCY_SIX)).thenReturn(
      CURRENCIES_REST_DTO.stream()
        .filter(f -> f.getId().equals(CURRENCY_SIX.getId()))
        .findFirst()
        .orElse(CURRENCIES_REST_DTO.get(5))
    );

    when(repository.findAll()).thenReturn(CURRENCIES);

    assertEquals(service.findAllCurrencies(), CURRENCIES_REST_DTO);
  }
}
