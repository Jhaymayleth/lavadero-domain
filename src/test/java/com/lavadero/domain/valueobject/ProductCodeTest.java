package com.lavadero.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.lavadero.domain.exception.InvalidProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProductCode")
class ProductCodeTest {

  @Test
  @DisplayName("normaliza el código a mayúsculas")
  void shouldNormalizeToUpperCase() {
    assertEquals("SH-001", new ProductCode(" sh-001 ").value());
  }

  @Test
  @DisplayName("rechaza un código vacío")
  void shouldRejectEmptyCode() {
    assertThrows(InvalidProductException.class, () -> new ProductCode("   "));
  }

  @Test
  @DisplayName("rechaza un código con caracteres inválidos")
  void shouldRejectInvalidCharacters() {
    assertThrows(InvalidProductException.class, () -> new ProductCode("SH 001!"));
  }

  @Test
  @DisplayName("rechaza un código demasiado corto")
  void shouldRejectShortCode() {
    assertThrows(InvalidProductException.class, () -> new ProductCode("AB"));
  }
}
