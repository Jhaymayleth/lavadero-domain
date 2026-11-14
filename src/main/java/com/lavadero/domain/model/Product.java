package com.lavadero.domain.model;

import com.lavadero.domain.enums.ProductStatus;
import com.lavadero.domain.event.DomainEvent;
import com.lavadero.domain.event.ProductCreatedDomainEvent;
import com.lavadero.domain.event.ProductDeletedDomainEvent;
import com.lavadero.domain.event.ProductUpdatedDomainEvent;
import com.lavadero.domain.event.SalePriceRecalculatedDomainEvent;
import com.lavadero.domain.event.StockUpdatedDomainEvent;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Agregado raíz Product del catálogo del lavadero.
 *
 * <p>Es la única puerta de entrada al estado del producto: no expone setters y concentra las
 * invariantes que cruzan varios Value Objects. Los cambios de estado relevantes se registran como
 * eventos de dominio para que la aplicación decida qué hacer con ellos.
 *
 * <p>Invariantes:
 *
 * <ol>
 *   <li>El precio de compra y el de venta son mayores que cero (garantizado por {@link ProductPrice}).
 *   <li>El precio de venta es mayor o igual al de compra.
 *   <li>El stock nunca es negativo (garantizado por {@link ProductStock}).
 *   <li>Todo producto pertenece a una categoría.
 *   <li>El estado es coherente con el stock y el stock mínimo.
 *   <li>Un producto descontinuado no recibe stock.
 * </ol>
 */
public final class Product {

  private final ProductId id;
  private final ProductCode code;
  private ProductName name;
  private ProductBrand brand;
  private ProductDescription description;
  private ProductPrice purchasePrice;
  private ProductPrice salePrice;
  private ProductStock stock;
  private ProductMinimumStock minimumStock;
  private ProductCategoryId categoryId;
  private Set<ProductSupplierId> supplierIds;
  private ProductImageUrl imageUrl;
  private ProductStatus status;
  private final List<DomainEvent> domainEvents;

  private Product(
      final ProductId id,
      final ProductCode code,
      final ProductName name,
      final ProductBrand brand,
      final ProductDescription description,
      final ProductPrice purchasePrice,
      final ProductPrice salePrice,
      final ProductStock stock,
      final ProductMinimumStock minimumStock,
      final ProductCategoryId categoryId,
      final Set<ProductSupplierId> supplierIds,
      final ProductImageUrl imageUrl) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.brand = brand;
    this.description = description;
    this.purchasePrice = purchasePrice;
    this.salePrice = salePrice;
    this.stock = stock;
    this.minimumStock = minimumStock;
    this.categoryId = categoryId;
    this.supplierIds = new LinkedHashSet<>(supplierIds);
    this.imageUrl = imageUrl;
    this.domainEvents = new ArrayList<>();
    this.status = ProductStatus.OUT_OF_STOCK;
    refreshStatus();
  }

  /**
   * Crea un producto nuevo y registra el evento de creación.
   *
   * @throws InvalidProductException si el precio de venta es menor que el de compra o si falta un
   *     dato obligatorio
   */
  public static Product create(
      final ProductId id,
      final ProductCode code,
      final ProductName name,
      final ProductBrand brand,
      final ProductDescription description,
      final ProductPrice purchasePrice,
      final ProductPrice salePrice,
      final ProductStock stock,
      final ProductMinimumStock minimumStock,
      final ProductCategoryId categoryId,
      final Set<ProductSupplierId> supplierIds,
      final ProductImageUrl imageUrl) {
    validateSalePrice(purchasePrice, salePrice);
    final Product product =
        new Product(
            id,
            code,
            name,
            brand,
            description,
            purchasePrice,
            salePrice,
            stock,
            minimumStock,
            categoryId,
            supplierIds,
            imageUrl);
    product.record(new ProductCreatedDomainEvent(product));
    return product;
  }

  /** Actualiza los datos editables del catálogo y registra el evento de actualización. */
  public void updateDetails(
      final ProductName name,
      final ProductBrand brand,
      final ProductDescription description,
      final ProductPrice purchasePrice,
      final ProductPrice salePrice,
      final ProductMinimumStock minimumStock,
      final ProductCategoryId categoryId,
      final Set<ProductSupplierId> supplierIds,
      final ProductImageUrl imageUrl) {
    validateSalePrice(purchasePrice, salePrice);
    this.name = name;
    this.brand = brand;
    this.description = description;
    this.purchasePrice = purchasePrice;
    this.salePrice = salePrice;
    this.minimumStock = minimumStock;
    this.categoryId = categoryId;
    this.supplierIds = new LinkedHashSet<>(supplierIds);
    this.imageUrl = imageUrl;
    refreshStatus();
    record(new ProductUpdatedDomainEvent(this));
  }

  /**
   * Recibe mercancía: incrementa el stock y registra el evento correspondiente.
   *
   * @throws InvalidProductException si el producto está descontinuado o las unidades son negativas
   */
  public void receiveStock(final int units) {
    ensureNotDiscontinued();
    stock = stock.increase(units);
    refreshStatus();
    record(new StockUpdatedDomainEvent(id, stock));
  }

  /**
   * Aplica un nuevo precio de compra y el precio de venta recalculado por el servicio de dominio.
   *
   * @throws InvalidProductException si el precio de venta queda por debajo del de compra
   */
  public void applyPurchasePrice(
      final ProductPrice newPurchasePrice, final ProductPrice newSalePrice) {
    validateSalePrice(newPurchasePrice, newSalePrice);
    final ProductPrice previousSalePrice = salePrice;
    purchasePrice = newPurchasePrice;
    salePrice = newSalePrice;
    record(new SalePriceRecalculatedDomainEvent(id, previousSalePrice, newSalePrice));
  }

  /**
   * Descuenta unidades vendidas o aplicadas.
   *
   * @throws InvalidProductException si no hay unidades suficientes
   */
  public void sell(final int units) {
    stock = stock.decrease(units);
    refreshStatus();
    record(new StockUpdatedDomainEvent(id, stock));
  }

  /** Marca el producto como descontinuado y lo retira del catálogo. */
  public void discontinue() {
    status = ProductStatus.DISCONTINUED;
    record(new ProductUpdatedDomainEvent(this));
  }

  /** Registra la eliminación del producto del catálogo. */
  public void markAsDeleted() {
    record(new ProductDeletedDomainEvent(id));
  }

  /** Indica si el producto está en o por debajo del stock mínimo. */
  public boolean isLowStock() {
    return stock.isLessThanOrEqualTo(minimumStock);
  }

  /** Devuelve los eventos acumulados y limpia la lista interna. */
  public List<DomainEvent> pullDomainEvents() {
    final List<DomainEvent> events = List.copyOf(domainEvents);
    domainEvents.clear();
    return events;
  }

  /** Devuelve una vista inmutable de los eventos sin limpiarlos. */
  public List<DomainEvent> domainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }

  private void refreshStatus() {
    if (status == ProductStatus.DISCONTINUED) {
      return;
    }
    if (stock.isZero()) {
      status = ProductStatus.OUT_OF_STOCK;
    } else if (stock.isLessThanOrEqualTo(minimumStock)) {
      status = ProductStatus.LOW_STOCK;
    } else {
      status = ProductStatus.AVAILABLE;
    }
  }

  private void ensureNotDiscontinued() {
    if (status == ProductStatus.DISCONTINUED) {
      throw InvalidProductException.becauseStatusIsInvalid(ProductStatus.DISCONTINUED.name());
    }
  }

  private void record(final DomainEvent event) {
    domainEvents.add(event);
  }

  private static void validateSalePrice(
      final ProductPrice purchasePrice, final ProductPrice salePrice) {
    if (salePrice.isLessThan(purchasePrice)) {
      throw InvalidProductException.becauseFormatIsInvalid(
          "sale price", "must be greater than or equal to purchase price");
    }
  }

  public ProductId getId() {
    return id;
  }

  public ProductCode getCode() {
    return code;
  }

  public ProductName getName() {
    return name;
  }

  public ProductBrand getBrand() {
    return brand;
  }

  public ProductDescription getDescription() {
    return description;
  }

  public ProductPrice getPurchasePrice() {
    return purchasePrice;
  }

  public ProductPrice getSalePrice() {
    return salePrice;
  }

  public ProductStock getStock() {
    return stock;
  }

  public ProductMinimumStock getMinimumStock() {
    return minimumStock;
  }

  public ProductCategoryId getCategoryId() {
    return categoryId;
  }

  public Set<ProductSupplierId> getSupplierIds() {
    return Collections.unmodifiableSet(supplierIds);
  }

  public ProductImageUrl getImageUrl() {
    return imageUrl;
  }

  public ProductStatus getStatus() {
    return status;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Product product)) {
      return false;
    }
    return Objects.equals(id, product.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return code.value() + " - " + name.value();
  }
}
