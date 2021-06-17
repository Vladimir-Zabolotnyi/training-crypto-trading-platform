package sigma.training.ctp.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FeeServiceTest {

  private static final String rateString = "0.05";
  private static final String minimalString = "10";

  private static final BigDecimal rate = new BigDecimal(rateString);
  private static final BigDecimal minimal = new BigDecimal(minimalString);

  private static FeeService service;

  @BeforeAll
  public static void setUp() {
    service = new FeeService(rateString, minimalString);
  }

  @Test
  public void testCalculateFeeLessThanMinimal() {
    BigDecimal cryptocurrencyPrice = new BigDecimal("10");
    BigDecimal cryptocurrencyAmount = new BigDecimal("10");

    BigDecimal result = service.calculateFee(cryptocurrencyPrice.multiply(cryptocurrencyAmount));

    assertEquals(minimal, result);
  }

  @Test
  public void testCalculateFeeBiggerThanMinimal() {
    BigDecimal cryptocurrencyPrice = new BigDecimal("100");
    BigDecimal cryptocurrencyAmount = new BigDecimal("10");

    BigDecimal expected = new BigDecimal("50");
    BigDecimal result = service.calculateFee(cryptocurrencyPrice.multiply(cryptocurrencyAmount));

    assertEquals(expected.doubleValue(), result.doubleValue(), 2);
  }
}
