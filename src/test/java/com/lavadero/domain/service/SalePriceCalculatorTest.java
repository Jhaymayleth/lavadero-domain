package com.lavadero.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lavadero.domain.valueobject.ProductPrice;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SalePriceCalculator")
class SalePriceCalculatorTest {

  private final SalePriceCalculator calculator = new SalePriceCalculator();

  @Test
  @DisplayName("aplica el 35 % a la diferencia entre precio actual y anterior")
  void shouldApplyThirtyFivePercentToDifference() {
    final ProductPrice current = new ProductPrice(new BigDecimal("12000.00"));
    final ProductPrice previous = new ProductPrice(new BigDecimal("10000.00"));

    final ProductPrice result = calculator.calculate(current, previous);

    assertEquals(new BigDecimal("12700.00"), result.value());
  }

  @Test
  @DisplayName("conserva el precio actual cuando no hay variación")
  void shouldKeepPriceWhenNoDifference() {
    final ProductPrice current = new ProductPrice(new BigDecimal("12000.00"));

    final ProductPrice result = calculator.calculate(current, current);

    assertEquals(new BigDecimal("12000.00"), result.value());
  }

  @Test
  @DisplayName("redondea a dos decimales cuando el aumento produce fracciones de centavo")
  void shouldRoundCalculatedPriceToTwoDecimalPlaces() {
    final ProductPrice current = new ProductPrice(new BigDecimal("100.02"));
    final ProductPrice previous = new ProductPrice(new BigDecimal("100.00"));

    final ProductPrice result = calculator.calculate(current, previous);

    assertEquals(new BigDecimal("100.03"), result.value());
  }

  @Test
  @DisplayName("calcula el precio inicial con margen sobre el costo")
  void shouldCalculateInitialPrice() {
    final ProductPrice result =
        calculator.calculateInitial(new ProductPrice(new BigDecimal("10000.00")));

    assertEquals(new BigDecimal("13500.00"), result.value());
  }

  @Test
  @DisplayName("redondea el precio inicial a dos decimales")
  void shouldRoundInitialPriceToTwoDecimalPlaces() {
    final ProductPrice result =
        calculator.calculateInitial(new ProductPrice(new BigDecimal("100.02")));

    assertEquals(new BigDecimal("135.03"), result.value());
  }
}
