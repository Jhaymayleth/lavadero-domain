package com.lavadero.domain.service;

import com.lavadero.domain.valueobject.ProductPrice;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Servicio de dominio que aplica la regla del 35 % del CEA Lavadero.
 *
 * <p>Cuando llega mercancía con un nuevo precio de compra, el precio de venta se recalcula
 * aumentando un 35 % al valor resultante de la diferencia entre el precio de compra actual y el
 * del pedido anterior:
 *
 * <pre>
 *   nuevoPrecioVenta = precioCompraActual + (precioCompraActual - precioCompraAnterior) * 0.35
 * </pre>
 *
 * <p>La fórmula vive aquí y no en el agregado para poder cambiarla sin tocar las invariantes del
 * producto. Si no existe precio anterior, se aplica un margen sobre el costo actual.
 */
public final class SalePriceCalculator {

  private static final BigDecimal INCREASE_RATE = new BigDecimal("0.35");
  private static final BigDecimal DEFAULT_INITIAL_MARGIN = new BigDecimal("0.35");
  private static final int MONEY_SCALE = 2;

  /**
   * Calcula el nuevo precio de venta a partir del precio de compra actual y el anterior.
   *
   * @param currentPurchasePrice precio de compra del pedido recibido
   * @param previousPurchasePrice precio de compra del pedido anterior
   * @return el precio de venta recalculado
   */
  public ProductPrice calculate(
      final ProductPrice currentPurchasePrice, final ProductPrice previousPurchasePrice) {
    Objects.requireNonNull(currentPurchasePrice, "currentPurchasePrice cannot be null");
    Objects.requireNonNull(previousPurchasePrice, "previousPurchasePrice cannot be null");
    final BigDecimal difference = currentPurchasePrice.difference(previousPurchasePrice);
    final BigDecimal increase = difference.multiply(INCREASE_RATE);
    final BigDecimal newSalePrice =
        currentPurchasePrice
            .value()
            .add(increase)
            .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    return new ProductPrice(newSalePrice);
  }

  /**
   * Calcula el precio de venta inicial cuando el producto no tiene pedido anterior.
   *
   * @param currentPurchasePrice precio de compra actual
   * @return el precio de venta con el margen inicial por defecto
   */
  public ProductPrice calculateInitial(final ProductPrice currentPurchasePrice) {
    Objects.requireNonNull(currentPurchasePrice, "currentPurchasePrice cannot be null");
    final BigDecimal margin = currentPurchasePrice.value().multiply(DEFAULT_INITIAL_MARGIN);
    return new ProductPrice(
        currentPurchasePrice
            .value()
            .add(margin)
            .setScale(MONEY_SCALE, RoundingMode.HALF_UP));
  }

  /** Expone la tasa aplicada para documentación y pruebas. */
  public BigDecimal increaseRate() {
    return INCREASE_RATE;
  }
}
