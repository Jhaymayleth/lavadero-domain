package com.lavadero.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lavadero.domain.exception.InvalidProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProductStock")
class ProductStockTest {

  @Test
  @DisplayName("rechaza un stock negativo")
  void shouldRejectNegativeStock() {
    assertThrows(InvalidProductException.class, () -> new ProductStock(-1));
  }

  @Test
  @DisplayName("incrementa las unidades disponibles")
  void shouldIncrease() {
    final ProductStock stock = new ProductStock(5).increase(3);

    assertEquals(8, stock.value());
  }

  @Test
  @DisplayName("decrementa las unidades disponibles")
  void shouldDecrease() {
    final ProductStock stock = new ProductStock(10).decrease(4);

    assertEquals(6, stock.value());
  }

  @Test
  @DisplayName("no permite dejar el stock en negativo")
  void shouldRejectDecreaseBelowZero() {
    final ProductStock stock = new ProductStock(2);

    assertThrows(InvalidProductException.class, () -> stock.decrease(3));
  }

  @Test
  @DisplayName("identifica el stock vacío")
  void shouldIdentifyEmptyStock() {
    assertTrue(ProductStock.empty().isZero());
  }

  @Test
  @DisplayName("compara contra el stock mínimo")
  void shouldCompareWithMinimumStock() {
    final ProductStock stock = new ProductStock(4);
    final ProductMinimumStock minimum = new ProductMinimumStock(5);

    assertTrue(stock.isLessThanOrEqualTo(minimum));
  }
}
