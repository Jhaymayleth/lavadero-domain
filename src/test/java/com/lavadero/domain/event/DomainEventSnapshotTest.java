package com.lavadero.domain.event;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lavadero.domain.model.Product;
import com.lavadero.domain.valueobject.ProductBrand;
import com.lavadero.domain.valueobject.ProductCategoryId;
import com.lavadero.domain.valueobject.ProductCode;
import com.lavadero.domain.valueobject.ProductDescription;
import com.lavadero.domain.valueobject.ProductId;
import com.lavadero.domain.valueobject.ProductImageUrl;
import com.lavadero.domain.valueobject.ProductMinimumStock;
import com.lavadero.domain.valueobject.ProductName;
import com.lavadero.domain.valueobject.ProductPrice;
import com.lavadero.domain.valueobject.ProductStock;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DomainEventSnapshotTest {

  @Test
  void createdEventKeepsTheOriginalProductDataAfterLaterUpdates() {
    final Product product = newProduct();
    final ProductCreatedDomainEvent event =
        (ProductCreatedDomainEvent) product.pullDomainEvents().get(0);

    updateName(product, "Jabon Premium");

    assertEquals("Jabon", event.payload().get("name"));
  }

  @Test
  void updatedEventKeepsTheDataFromTheUpdateThatCreatedIt() {
    final Product product = newProduct();
    product.pullDomainEvents();
    updateName(product, "Jabon Premium");
    final ProductUpdatedDomainEvent event =
        (ProductUpdatedDomainEvent) product.pullDomainEvents().get(0);

    updateName(product, "Jabon Familiar");

    assertEquals("Jabon Premium", event.payload().get("name"));
  }

  private static Product newProduct() {
    return Product.create(
        new ProductId(UUID.randomUUID().toString()),
        new ProductCode("JAB-001"),
        new ProductName("Jabon"),
        new ProductBrand("Lavadero"),
        new ProductDescription("Producto de prueba"),
        price("100.00"),
        price("150.00"),
        new ProductStock(10),
        new ProductMinimumStock(1),
        new ProductCategoryId(UUID.randomUUID().toString()),
        Set.of(),
        new ProductImageUrl(null));
  }

  private static void updateName(final Product product, final String name) {
    product.updateDetails(
        new ProductName(name),
        new ProductBrand("Lavadero"),
        new ProductDescription("Producto de prueba"),
        price("100.00"),
        price("150.00"),
        new ProductMinimumStock(1),
        new ProductCategoryId(UUID.randomUUID().toString()),
        Set.of(),
        new ProductImageUrl(null));
  }

  private static ProductPrice price(final String value) {
    return new ProductPrice(new BigDecimal(value));
  }
}
