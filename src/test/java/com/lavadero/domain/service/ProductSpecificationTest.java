package com.lavadero.domain.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lavadero.domain.enums.ProductStatus;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Especificaciones de producto")
class ProductSpecificationTest {

  private final LowStockSpecification lowStock = new LowStockSpecification();
  private final AvailableProductSpecification available = new AvailableProductSpecification();

  @Test
  @DisplayName("un producto en el umbral está por agotarse")
  void shouldMatchLowStock() {
    assertTrue(lowStock.isSatisfiedBy(buildProduct(3, 5)));
  }

  @Test
  @DisplayName("un producto con stock suficiente no está por agotarse")
  void shouldNotMatchLowStock() {
    assertFalse(lowStock.isSatisfiedBy(buildProduct(10, 5)));
  }

  @Test
  @DisplayName("solo un producto disponible cumple la especificación")
  void shouldMatchAvailable() {
    assertTrue(available.isSatisfiedBy(buildProduct(10, 5)));
    assertFalse(available.isSatisfiedBy(buildProduct(0, 5)));
  }

  private Product buildProduct(final int stock, final int minimumStock) {
    return Product.create(
        new ProductId("11111111-1111-1111-1111-111111111111"),
        new ProductCode("SH-001"),
        new ProductName("Shampoo automotriz"),
        new ProductBrand("Brilla"),
        new ProductDescription("Shampoo para carroceria"),
        new ProductPrice(new BigDecimal("10000.00")),
        new ProductPrice(new BigDecimal("15000.00")),
        new ProductStock(stock),
        new ProductMinimumStock(minimumStock),
        new ProductCategoryId("22222222-2222-2222-2222-222222222222"),
        Set.of(),
        new ProductImageUrl(null));
  }
}
