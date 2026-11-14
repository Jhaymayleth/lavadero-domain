package com.lavadero.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.lavadero.domain.valueobject.ProductStock;
import org.junit.jupiter.api.Test;

class StockReplenishmentServiceTest {

  private final StockReplenishmentService service = new StockReplenishmentService();

  @Test
  void replenishesStockByTheReceivedUnits() {
    assertEquals(7, service.replenish(new ProductStock(3), 4).value());
  }

  @Test
  void rejectsNonPositiveReceivedUnits() {
    assertThrows(
        IllegalArgumentException.class, () -> service.replenish(new ProductStock(3), 0));
    assertThrows(
        IllegalArgumentException.class, () -> service.replenish(new ProductStock(3), -1));
  }
}
