package com.lavadero.domain.event;

import com.lavadero.domain.model.Product;
import java.util.Map;
import java.util.Objects;

/**
 * Evento que se publica cuando cambian los datos editables del producto.
 *
 * <p>Refleja la regla «el catálogo debe mostrar siempre la información vigente».
 */
public final class ProductUpdatedDomainEvent extends DomainEvent {

  private static final String EVENT_NAME = "product.updated";

  private final String productId;
  private final String code;
  private final String name;
  private final String status;

  public ProductUpdatedDomainEvent(final Product product) {
    super(EVENT_NAME);
    final Product updatedProduct = Objects.requireNonNull(product, "product cannot be null");
    this.productId = updatedProduct.getId().value();
    this.code = updatedProduct.getCode().value();
    this.name = updatedProduct.getName().value();
    this.status = updatedProduct.getStatus().name();
  }

  @Override
  public Map<String, String> payload() {
    return Map.of("id", productId, "code", code, "name", name, "status", status);
  }
}
