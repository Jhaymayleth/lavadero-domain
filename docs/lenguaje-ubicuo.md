# Lenguaje ubicuo — Dominio del lavadero

| Término | Definición | Clase |
|---|---|---|
| Producto | Ítem del catálogo que el lavadero vende. | `Product` |
| Código | Identificador legible y único. | `ProductCode` |
| Nombre | Nombre comercial. | `ProductName` |
| Marca | Fabricante; opcional. | `ProductBrand` |
| Descripción | Texto del catálogo. | `ProductDescription` |
| Imagen | Fotografía del producto; opcional. | `ProductImageUrl` |
| Categoría | Agrupación única del catálogo. | `ProductCategoryId` |
| Proveedor | Tercero que suministra el producto. | `ProductSupplierId` |
| Precio | Valor monetario en COP. | `ProductPrice` |
| Stock | Unidades disponibles. | `ProductStock` |
| Stock mínimo | Umbral de reposición. | `ProductMinimumStock` |
| Estado | Disponible, por agotarse, agotado o descontinuado. | `ProductStatus` |
| Precio recalculado | Precio de venta tras aplicar el 35 %. | `SalePriceCalculator` |
| Producto por agotarse | Producto con stock ≤ stock mínimo. | `LowStockSpecification` |

## Reglas de nombrado

- Documentación y comentarios en español.
- Clases y paquetes en inglés.
- Un término del glosario corresponde a un único concepto del código.
