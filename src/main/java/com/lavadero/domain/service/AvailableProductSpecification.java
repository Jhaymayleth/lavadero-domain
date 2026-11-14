package com.lavadero.domain.service;

import com.lavadero.domain.enums.ProductStatus;
import com.lavadero.domain.model.Product;

/**
 * Especificación que determina si un producto está disponible para la venta.
 *
 * <p>Un producto solo se ofrece cuando su estado es {@link ProductStatus#AVAILABLE}. Los productos
 * agotados, por agotarse o descontinuados no deberían aparecer en el catálogo de venta.
 */
public final class AvailableProductSpecification {

  /**
   * Evalúa si el producto puede venderse.
   *
   * @param product producto a evaluar
   * @return {@code true} si el estado es {@code AVAILABLE}
   */
  public boolean isSatisfiedBy(final Product product) {
    return product.getStatus() == ProductStatus.AVAILABLE;
  }
}
