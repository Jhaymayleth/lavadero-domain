package com.lavadero.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lavadero.domain.enums.ProductStatus;
import com.lavadero.domain.exception.InvalidProductException;
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
import com.lavadero.domain.valueobject.ProductSupplierId;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Product")
class ProductTest {

  private static final ProductId ID =
      new ProductId("11111111-1111-1111-1111-111111111111");
  private static final ProductCategoryId CATEGORY =
      new ProductCategoryId("22222222-2222-2222-2222-222222222222");
  private static final ProductSupplierId SUPPLIER =
      new ProductSupplierId("33333333-3333-3333-3333-333333333333");

  @Test
  @DisplayName("nace agotado cuando el stock inicial es cero")
  void shouldBeOutOfStockWhenCreatedWithoutUnits() {
    final Product product = buildProduct(0, 5);

    assertEquals(ProductStatus.OUT_OF_STOCK, product.getStatus());
  }

  @Test
  @DisplayName("nace por agotarse cuando el stock está en el umbral")
  void shouldBeLowStockWhenAtThreshold() {
    final Product product = buildProduct(5, 5);

    assertEquals(ProductStatus.LOW_STOCK, product.getStatus());
    assertTrue(product.isLowStock());
  }

  @Test
  @DisplayName("nace disponible cuando supera el stock mínimo")
  void shouldBeAvailableAboveThreshold() {
    final Product product = buildProduct(10, 5);

    assertEquals(ProductStatus.AVAILABLE, product.getStatus());
  }

  @Test
  @DisplayName("registra el evento de creación")
  void shouldRecordCreatedEvent() {
    final Product product = buildProduct(10, 5);

    assertEquals("product.created", product.domainEvents().get(0).eventName());
  }

  @Test
  @DisplayName("incrementa el stock y publica el evento")
  void shouldReceiveStock() {
    final Product product = buildProduct(1, 5);

    product.receiveStock(4);

    assertEquals(5, product.getStock().value());
    assertEquals(ProductStatus.LOW_STOCK, product.getStatus());
  }

  @Test
  @DisplayName("descuenta unidades vendidas")
  void shouldSellUnits() {
    final Product product = buildProduct(10, 5);

    product.sell(3);

    assertEquals(7, product.getStock().value());
  }

  @Test
  @DisplayName("rechaza vender más unidades de las disponibles")
  void shouldRejectSellAboveStock() {
    final Product product = buildProduct(1, 5);

    assertThrows(InvalidProductException.class, () -> product.sell(2));
  }

  @Test
  @DisplayName("rechaza recibir stock cuando está descontinuado")
  void shouldRejectStockWhenDiscontinued() {
    final Product product = buildProduct(10, 5);
    product.discontinue();

    assertThrows(InvalidProductException.class, () -> product.receiveStock(1));
  }

  @Test
  @DisplayName("rechaza un precio de venta menor que el de compra")
  void shouldRejectSalePriceBelowPurchasePrice() {
    assertThrows(
        InvalidProductException.class,
        () ->
            Product.create(
                ID,
                new ProductCode("SH-001"),
                new ProductName("Shampoo automotriz"),
                new ProductBrand("Brilla"),
                new ProductDescription("Shampoo para carroceria"),
                new ProductPrice(new BigDecimal("20000.00")),
                new ProductPrice(new BigDecimal("15000.00")),
                new ProductStock(10),
                new ProductMinimumStock(5),
                CATEGORY,
                Set.of(SUPPLIER),
                new ProductImageUrl("https://cdn.lavadero.com/shampoo.png")));
  }

  @Test
  @DisplayName("los productos se comparan por identidad")
  void shouldCompareByIdentity() {
    final Product first = buildProduct(10, 5);
    final Product second = buildProduct(1, 1);

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  @DisplayName("extrae los eventos acumulados y limpia la lista")
  void shouldPullDomainEvents() {
    final Product product = buildProduct(10, 5);
    product.receiveStock(1);

    assertEquals(2, product.pullDomainEvents().size());
    assertTrue(product.domainEvents().isEmpty());
  }

  private Product buildProduct(final int stock, final int minimumStock) {
    return Product.create(
        ID,
        new ProductCode("SH-001"),
        new ProductName("Shampoo automotriz"),
        new ProductBrand("Brilla"),
        new ProductDescription("Shampoo para carroceria"),
        new ProductPrice(new BigDecimal("10000.00")),
        new ProductPrice(new BigDecimal("15000.00")),
        new ProductStock(stock),
        new ProductMinimumStock(minimumStock),
        CATEGORY,
        Set.of(SUPPLIER),
        new ProductImageUrl("https://cdn.lavadero.com/shampoo.png"));
  }
}
