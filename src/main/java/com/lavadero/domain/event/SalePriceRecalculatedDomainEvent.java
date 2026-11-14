package com.lavadero.domain.event;

import com.lavadero.domain.valueobject.ProductId;
import com.lavadero.domain.valueobject.ProductPrice;
import java.util.Map;

/**
 * Evento que se publica cuando se recalcula el precio de venta de un producto.
 *
 * <p>Refleja la regla del 35 % del CEA: al recibir mercancía con un nuevo precio de compra cambia
 * el precio de venta del catálogo.
 */
public final class SalePriceRecalculatedDomainEvent extends DomainEvent {

  private static final String EVENT_NAME = "sale.price.recalculated";

  private final ProductId productId;
  private final ProductPrice previousSalePrice;
  private final ProductPrice newSalePrice;

  public SalePriceRecalculatedDomainEvent(
      final ProductId productId,
      final ProductPrice previousSalePrice,
      final ProductPrice newSalePrice) {
    super(EVENT_NAME);
    this.productId = productId;
    this.previousSalePrice = previousSalePrice;
    this.newSalePrice = newSalePrice;
  }

  @Override
  public Map<String, String> payload() {
    return Map.of(
        "productId", productId.value(),
        "previousSalePrice", previousSalePrice.toString(),
        "newSalePrice", newSalePrice.toString());
  }
}
