# Modelo de dominio — Producto del lavadero

Este repositorio implementa el agregado `Product` del catálogo del CEA **Ejercicio 28 — Lavadero**.
Compras, cotizaciones, ventas, vehículos, empleados y cubículos permanecen como contexto del CEA;
no son agregados implementados aquí.

## UML conceptual

```text
                         ┌────────────────────────────┐
                         │ Product (Aggregate Root)   │
                         ├────────────────────────────┤
                         │ id: ProductId              │
                         │ code: ProductCode          │
                         │ name: ProductName          │
                         │ brand: ProductBrand        │
                         │ description: ProductDescription
                         │ purchasePrice: ProductPrice│
                         │ salePrice: ProductPrice    │
                         │ stock: ProductStock        │
                         │ minimumStock: ProductMinimumStock
                         │ categoryId: ProductCategoryId
                         │ supplierIds: Set<ProductSupplierId>
                         │ imageUrl: ProductImageUrl  │
                         │ status: ProductStatus      │
                         └─────────────┬──────────────┘
                                       │ records
             ┌─────────────────────────┼──────────────────────────┐
             ▼                         ▼                          ▼
  ProductCreatedDomainEvent  ProductUpdatedDomainEvent  StockUpdatedDomainEvent
                                       │
                          SalePriceRecalculatedDomainEvent

  SalePriceCalculator ─────── calculates ───────> ProductPrice
  ProductAvailabilityService ─ resolves ────────> ProductStatus
  LowStockSpecification ───── evaluates ────────> Product
  AvailableProductSpecification ─ evaluates ────> Product
  PercentageDiscountStrategy ── calculates ────> ProductPrice
  ColombianIvaTaxStrategy ───── calculates ────> ProductPrice
```

`ProductId`, `ProductCode` y los demás Value Objects son inmutables. `Product` es mutable dentro
del agregado y controla las operaciones que pueden cambiar estado, stock y precios. Los
identificadores de categoría y proveedores son referencias; sus agregados no se cargan dentro de
`Product`.

## Operaciones e invariantes

- `Product.create(...)` verifica que el precio de venta no sea menor que el de compra, deriva el
  estado inicial del stock y registra el evento de creación.
- `updateDetails(...)` reemplaza datos editables y recalcula el estado.
- `receiveStock(...)` rechaza productos descontinuados; `sell(...)` no permite stock negativo.
- `applyPurchasePrice(...)` mantiene el precio de venta mayor o igual al costo.
- `discontinue()` marca el producto; `markAsDeleted()` registra el retiro del catálogo.
- `pullDomainEvents()` entrega una copia inmutable de los eventos y limpia el acumulador.

La unicidad de `ProductCode` depende de todo el catálogo y se valida desde la aplicación mediante
un puerto de salida; no puede comprobarse con una sola instancia de `Product`.

## Organización de paquetes

| Paquete | Responsabilidad |
|---|---|
| `domain.model` | Agregado raíz `Product`. |
| `domain.valueobject` | Identidad, código, nombre, precios, inventario y referencias. |
| `domain.enums` | Disponibilidad del producto. |
| `domain.event` | Hechos ocurridos en el ciclo de vida del producto. |
| `domain.exception` | Errores de negocio independientes de la persistencia. |
| `domain.service` | Cálculos, políticas y Specifications reutilizables. |

La capa no importa JDBC, frameworks web ni UI. Sus pruebas usan JUnit y Value Objects reales; el
adaptador en memoria y el adaptador PostgreSQL pertenecen a los repositorios de las actividades.
