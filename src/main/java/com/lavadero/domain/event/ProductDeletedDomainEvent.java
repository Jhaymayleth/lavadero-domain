package com.lavadero.domain.event;

import com.lavadero.domain.valueobject.ProductId;
import java.util.Map;

/**
 * Evento que se publica cuando un producto se retira del catálogo.
 *
 * <p>Refleja la regla «un producto eliminado deja de ofrecerse a los clientes».
 */
public final class ProductDeletedDomainEvent extends DomainEvent {

  private static final String EVENT_NAME = "product.deleted";

  private final ProductId productId;

  public ProductDeletedDomainEvent(final ProductId productId) {
    super(EVENT_NAME);
    this.productId = productId;
  }

  @Override
  public Map<String, String> payload() {
    return Map.of("productId", productId.value());
  }
}
