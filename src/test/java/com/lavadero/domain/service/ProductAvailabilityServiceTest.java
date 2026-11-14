package com.lavadero.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.lavadero.domain.enums.ProductStatus;
import com.lavadero.domain.valueobject.ProductMinimumStock;
import com.lavadero.domain.valueobject.ProductStock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProductAvailabilityService")
class ProductAvailabilityServiceTest {

  private final ProductAvailabilityService service = new ProductAvailabilityService();

  @Test
  @DisplayName("sin unidades el estado es agotado")
  void shouldReturnOutOfStock() {
    final ProductStatus status =
        service.resolve(new ProductStock(0), new ProductMinimumStock(5), ProductStatus.AVAILABLE);

    assertEquals(ProductStatus.OUT_OF_STOCK, status);
  }

  @Test
  @DisplayName("en el umbral el estado es por agotarse")
  void shouldReturnLowStock() {
    final ProductStatus status =
        service.resolve(new ProductStock(5), new ProductMinimumStock(5), ProductStatus.AVAILABLE);

    assertEquals(ProductStatus.LOW_STOCK, status);
  }

  @Test
  @DisplayName("por encima del umbral el estado es disponible")
  void shouldReturnAvailable() {
    final ProductStatus status =
        service.resolve(new ProductStock(6), new ProductMinimumStock(5), ProductStatus.AVAILABLE);

    assertEquals(ProductStatus.AVAILABLE, status);
  }

  @Test
  @DisplayName("un producto descontinuado conserva su estado")
  void shouldKeepDiscontinued() {
    final ProductStatus status =
        service.resolve(
            new ProductStock(10), new ProductMinimumStock(5), ProductStatus.DISCONTINUED);

    assertEquals(ProductStatus.DISCONTINUED, status);
  }

  @Test
  @DisplayName("rechaza un stock nulo")
  void shouldRejectNullStock() {
    assertThrows(
        NullPointerException.class,
        () -> service.resolve(null, new ProductMinimumStock(5), ProductStatus.AVAILABLE));
  }
}
