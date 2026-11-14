package com.lavadero.domain.service;

import com.lavadero.domain.valueobject.ProductPrice;
import java.math.BigDecimal;

/**
 * Descuento porcentual simple para líneas del catálogo.
 *
 * <p>El porcentaje se expresa como fracción entre 0 y 1. Un descuento del 100 % no es válido
 * porque el precio resultante dejaría de ser un precio de venta positivo.
 */
public final class PercentageDiscountStrategy implements DiscountCalculationStrategy {

  private final BigDecimal percentage;

  public PercentageDiscountStrategy(final BigDecimal percentage) {
    if (percentage == null
        || percentage.signum() < 0
        || percentage.compareTo(BigDecimal.ONE) >= 0) {
      throw new IllegalArgumentException("Discount percentage must be between 0 and 1");
    }
    this.percentage = percentage;
  }

  @Override
  public ProductPrice apply(final ProductPrice price) {
    return new ProductPrice(price.value().multiply(BigDecimal.ONE.subtract(percentage)));
  }

  @Override
  public BigDecimal percentage() {
    return percentage;
  }
}
