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

import java.util.Arrays;
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

  private static final CurrencyEntity CURRENCY_ONE = new CurrencyEntity(1L, true, "USA_dollar", "USD");
  private static final CurrencyRestDto CURRENCY_ONE_REST_DTO = new CurrencyRestDto(CURRENCY_ONE.getId(), CURRENCY_ONE.getName(), CURRENCY_ONE.getAcronym());

  private static final List<CurrencyEntity> CURRENCIES = Arrays.asList(CURRENCY_ONE);
  private static final List<CurrencyRestDto> CURRENCIES_REST_DTO = Arrays.asList(CURRENCY_ONE_REST_DTO);

  @Test
  public void testFindAllCurrencies() {
    when(mapper.toRestDto(CURRENCY_ONE)).thenReturn(CURRENCY_ONE_REST_DTO);
    when(repository.findAll()).thenReturn(CURRENCIES);

    assertEquals(service.getAllCurrencies(), CURRENCIES_REST_DTO);
  }
}
