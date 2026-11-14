package com.lavadero.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.lavadero.domain.valueobject.ProductPrice;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PricingStrategiesTest {

  @Test
  void percentageDiscountReturnsTheDiscountedPriceAndRate() {
    final PercentageDiscountStrategy strategy =
        new PercentageDiscountStrategy(new BigDecimal("0.10"));

    assertEquals(new BigDecimal("90.00"), strategy.apply(price("100.00")).value());
    assertEquals(new BigDecimal("0.10"), strategy.percentage());
  }

  @Test
  void percentageDiscountRejectsValuesOutsideItsSupportedRange() {
    assertThrows(
        IllegalArgumentException.class, () -> new PercentageDiscountStrategy(new BigDecimal("-0.1")));
    assertThrows(
        IllegalArgumentException.class, () -> new PercentageDiscountStrategy(BigDecimal.ONE));
    assertThrows(IllegalArgumentException.class, () -> new PercentageDiscountStrategy(null));
  }

  @Test
  void colombianIvaCalculatesNineteenPercentOfTheBasePrice() {
    final ColombianIvaTaxStrategy strategy = new ColombianIvaTaxStrategy();

    assertEquals(new BigDecimal("19.00"), strategy.calculate(price("100.00")).value());
    assertEquals(new BigDecimal("0.19"), strategy.rate());
  }

  private static ProductPrice price(final String value) {
    return new ProductPrice(new BigDecimal(value));
  }
}
