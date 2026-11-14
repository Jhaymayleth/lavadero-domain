package com.lavadero.domain.service;

import com.lavadero.domain.valueobject.ProductPrice;
import java.math.BigDecimal;

/**
 * Estrategia de cálculo de impuestos sobre un precio de venta.
 *
 * <p>El CEA maneja IVA en cotizaciones y órdenes de venta. Aislar el cálculo en una estrategia
 * permite cambiar la tasa o el país sin tocar el agregado ni los casos de uso.
 */
public interface TaxCalculationStrategy {

  /**
   * Calcula el valor del impuesto para un precio base.
   *
   * @param basePrice precio antes de impuestos
   * @return valor del impuesto
   */
  ProductPrice calculate(ProductPrice basePrice);

  /** Tasa aplicada, expresada como fracción (por ejemplo 0.19 para el 19 %). */
  BigDecimal rate();
}
