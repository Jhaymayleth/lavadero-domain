package com.lavadero.domain.service;

import com.lavadero.domain.model.Product;

/**
 * Especificación que determina si un producto está por agotarse.
 *
 * <p>Encapsula la regla del CEA «producto con stock menor o igual al stock mínimo». Al ser una
 * especificación, puede combinarse o reutilizarse sin duplicar la comparación en cada consulta.
 */
public final class LowStockSpecification {

  /**
   * Evalúa si el producto cumple la condición de reposición.
   *
   * @param product producto a evaluar
   * @return {@code true} si el stock es menor o igual al stock mínimo
   */
  public boolean isSatisfiedBy(final Product product) {
    return product.getStock().isLessThanOrEqualTo(product.getMinimumStock());
  }
}
