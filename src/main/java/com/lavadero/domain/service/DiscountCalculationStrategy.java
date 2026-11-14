package com.lavadero.domain.service;

import com.lavadero.domain.valueobject.ProductPrice;
import java.math.BigDecimal;

/**
 * Estrategia de descuento aplicable a un producto o a una línea de la orden de venta.
 *
 * <p>El CEA contempla descuentos variables por producto. Modelarlos como estrategia evita llenar
 * el agregado de condicionales y permite agregar promociones sin modificarlo.
 */
public interface DiscountCalculationStrategy {

  /**
   * Aplica el descuento a un precio.
   *
   * @param price precio base
   * @return precio con el descuento aplicado
   */
  ProductPrice apply(ProductPrice price);

  /** Porcentaje de descuento expresado como fracción (por ejemplo 0.10 para el 10 %). */
  BigDecimal percentage();
}
