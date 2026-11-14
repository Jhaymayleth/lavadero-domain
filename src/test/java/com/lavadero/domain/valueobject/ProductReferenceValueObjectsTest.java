package com.lavadero.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.lavadero.domain.exception.InvalidProductException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProductReferenceValueObjectsTest {

  @Test
  void productIdTrimsWhitespaceAndRejectsAnEmptyValue() {
    assertEquals("product-1", new ProductId(" product-1 ").value());
    assertThrows(InvalidProductException.class, () -> new ProductId("  "));
  }

  @Test
  void productNameTrimsWhitespaceAndRejectsNamesOutsideItsLengthBounds() {
    assertEquals("Soap", new ProductName(" Soap ").value());
    assertThrows(InvalidProductException.class, () -> new ProductName("ab"));
    assertThrows(InvalidProductException.class, () -> new ProductName("n".repeat(151)));
  }

  @Test
  void productDescriptionValidatesItsLengthBounds() {
    assertEquals("Product", new ProductDescription(" Product ").value());
    assertThrows(InvalidProductException.class, () -> new ProductDescription("four"));
    assertThrows(InvalidProductException.class, () -> new ProductDescription("d".repeat(501)));
  }

  @Test
  void minimumStockHasAValidatedDefaultThreshold() {
    assertEquals(5, ProductMinimumStock.defaultThreshold().value());
    assertThrows(InvalidProductException.class, () -> new ProductMinimumStock(-1));
  }

  @Test
  void categoryAndSupplierIdsAcceptUuidReferencesAndRejectMalformedValues() {
    final UUID categoryId = UUID.randomUUID();
    final UUID supplierId = UUID.randomUUID();

    assertEquals(categoryId.toString(), ProductCategoryId.from(categoryId).value());
    assertEquals(supplierId.toString(), ProductSupplierId.from(supplierId).value());
    assertThrows(InvalidProductException.class, () -> new ProductCategoryId("not-a-uuid"));
    assertThrows(InvalidProductException.class, () -> new ProductSupplierId("not-a-uuid"));
  }
}
