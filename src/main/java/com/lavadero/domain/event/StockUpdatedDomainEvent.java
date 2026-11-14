package com.lavadero.domain.event;

import com.lavadero.domain.valueobject.ProductId;
import com.lavadero.domain.valueobject.ProductStock;
import java.util.Map;

/**
 * Evento que se publica cuando el stock de un producto cambia.
 *
 * <p>Refleja la regla «al recibir un pedido se actualiza el stock con las nuevas cantidades».
 */
public final class StockUpdatedDomainEvent extends DomainEvent {

  private static final String EVENT_NAME = "stock.updated";

  private final ProductId productId;
  private final ProductStock newStock;

  public StockUpdatedDomainEvent(final ProductId productId, final ProductStock newStock) {
    super(EVENT_NAME);
    this.productId = productId;
    this.newStock = newStock;
  }

  @Override
  public Map<String, String> payload() {
    return Map.of(
        "productId", productId.value(),
        "stock", newStock.toString());
  }
}
