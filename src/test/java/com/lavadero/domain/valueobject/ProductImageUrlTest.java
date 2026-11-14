package com.lavadero.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lavadero.domain.exception.InvalidProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProductImageUrl")
class ProductImageUrlTest {

  @Test
  @DisplayName("permite la imagen ausente")
  void shouldAllowNullUrl() {
    final ProductImageUrl imageUrl = assertDoesNotThrow(() -> new ProductImageUrl(null));

    assertFalse(imageUrl.isPresent());
  }

  @Test
  @DisplayName("acepta una URL HTTP válida")
  void shouldAcceptHttpUrl() {
    final ProductImageUrl imageUrl = new ProductImageUrl("https://cdn.lavadero.com/shampoo.png");

    assertTrue(imageUrl.isPresent());
  }

  @Test
  @DisplayName("rechaza una URL sin esquema HTTP")
  void shouldRejectInvalidScheme() {
    assertThrows(InvalidProductException.class, () -> new ProductImageUrl("ftp://archivo.png"));
  }

  @Test
  @DisplayName("rechaza una URL relativa")
  void shouldRejectRelativeUrl() {
    assertThrows(InvalidProductException.class, () -> new ProductImageUrl("/imagenes/shampoo.png"));
  }
}
