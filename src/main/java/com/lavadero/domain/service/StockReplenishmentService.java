package com.lavadero.domain.service;

import com.lavadero.domain.valueobject.ProductStock;

/**
 * Servicio de dominio que aplica la reposición de stock tras recibir un pedido.
 *
 * <p>Se usa cuando la recepción de mercancía es un proceso propio y no una simple operación del
 * agregado: valida las unidades recibidas y devuelve el nuevo stock sin tocar la persistencia.
 */
public final class StockReplenishmentService {

  /**
   * Calcula el stock resultante de una recepción.
   *
   * @param currentStock stock actual
   * @param receivedUnits unidades recibidas
   * @return el nuevo stock
   */
  public ProductStock replenish(final ProductStock currentStock, final int receivedUnits) {
    if (receivedUnits <= 0) {
      throw new IllegalArgumentException("Received units must be greater than zero");
    }
    return currentStock.increase(receivedUnits);
  }
}
