package com.lavadero.domain.service;

import com.lavadero.domain.enums.ProductStatus;
import com.lavadero.domain.valueobject.ProductMinimumStock;
import com.lavadero.domain.valueobject.ProductStock;
import java.util.Objects;

/**
 * Servicio de dominio que determina la disponibilidad de un producto.
 *
 * <p>Centraliza la regla «el estado debe ser coherente con el stock» para que el agregado, la
 * consola y cualquier futura interfaz muestren el mismo resultado.
 */
public final class ProductAvailabilityService {

  /**
   * Calcula el estado que corresponde a un stock y un umbral dados.
   *
   * <p>Un producto descontinuado conserva su estado: la regla de stock no lo reactiva.
   *
   * @param stock unidades disponibles
   * @param minimumStock umbral de reposición
   * @param currentStatus estado actual del producto
   * @return el estado coherente con el inventario
   */
  public ProductStatus resolve(
      final ProductStock stock,
      final ProductMinimumStock minimumStock,
      final ProductStatus currentStatus) {
    Objects.requireNonNull(stock, "stock cannot be null");
    Objects.requireNonNull(minimumStock, "minimumStock cannot be null");
    Objects.requireNonNull(currentStatus, "currentStatus cannot be null");
    if (currentStatus == ProductStatus.DISCONTINUED) {
      return ProductStatus.DISCONTINUED;
    }
    if (stock.isZero()) {
      return ProductStatus.OUT_OF_STOCK;
    }
    if (stock.isLessThanOrEqualTo(minimumStock)) {
      return ProductStatus.LOW_STOCK;
    }
    return ProductStatus.AVAILABLE;
  }
}
