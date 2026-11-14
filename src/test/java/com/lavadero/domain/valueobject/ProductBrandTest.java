package com.lavadero.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lavadero.domain.exception.InvalidProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProductBrand")
class ProductBrandTest {

  @Test
  @DisplayName("permite la marca ausente")
  void shouldAllowNullBrand() {
    final ProductBrand brand = assertDoesNotThrow(() -> new ProductBrand(null));

    assertFalse(brand.isPresent());
  }

  @Test
  @DisplayName("rechaza una marca vacía")
  void shouldRejectEmptyBrand() {
    assertThrows(InvalidProductException.class, () -> new ProductBrand("   "));
  }

  @Test
  @DisplayName("conserva una marca válida")
  void shouldKeepValidBrand() {
    final ProductBrand brand = new ProductBrand("  Brilla  ");

    assertTrue(brand.isPresent());
    assertTrue("Brilla".equals(brand.value()));
  }
}
