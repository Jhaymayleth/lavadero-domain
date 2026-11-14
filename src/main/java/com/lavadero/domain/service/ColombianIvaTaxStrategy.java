package com.lavadero.domain.service;

import com.lavadero.domain.valueobject.ProductPrice;
import java.math.BigDecimal;

/**
 * Estrategia de IVA colombiano aplicada al catálogo del lavadero.
 *
 * <p>La tasa general es del 19 %. El cálculo se hace sobre el precio de venta y se redondea a dos
 * decimales a través de {@link ProductPrice}.
 */
public final class ColombianIvaTaxStrategy implements TaxCalculationStrategy {

  private static final BigDecimal IVA_RATE = new BigDecimal("0.19");

  @Override
  public ProductPrice calculate(final ProductPrice basePrice) {
    return basePrice.multiply(IVA_RATE);
  }

  @Override
  public BigDecimal rate() {
    return IVA_RATE;
  }
}
