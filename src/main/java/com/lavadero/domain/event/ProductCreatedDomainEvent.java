package com.lavadero.domain.event;

import com.lavadero.domain.model.Product;
import java.util.Map;
import java.util.Objects;

/**
 * Evento que se publica cuando un producto se registra en el catálogo.
 *
 * <p>Refleja la regla «todo producto nuevo nace con su estado coherente al stock inicial».
 */
public final class ProductCreatedDomainEvent extends DomainEvent {

  private static final String EVENT_NAME = "product.created";

  private final String productId;
  private final String code;
  private final String name;
  private final String salePrice;
  private final String status;

  public ProductCreatedDomainEvent(final Product product) {
    super(EVENT_NAME);
    final Product createdProduct = Objects.requireNonNull(product, "product cannot be null");
    this.productId = createdProduct.getId().value();
    this.code = createdProduct.getCode().value();
    this.name = createdProduct.getName().value();
    this.salePrice = createdProduct.getSalePrice().toString();
    this.status = createdProduct.getStatus().name();
  }

  @Override
  public Map<String, String> payload() {
    return Map.of(
        "id", productId, "code", code, "name", name, "salePrice", salePrice, "status", status);
  }
}
