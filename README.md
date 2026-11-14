# Lavadero — Capa de dominio

Capa de dominio del **CEA Ejercicio 28 — Lavadero (Grupo ABC Ltda.)**, desarrollada para el
**Protocolo Individual** de la Unidad 2 de Desarrollo de Software.

El proyecto implementa únicamente el modelo de dominio y sus pruebas unitarias. No contiene
adaptadores, base de datos ni interfaz de usuario: esa es precisamente la idea de un dominio
aislado.

---

## 1. Alcance

Entidad de trabajo: **`Product` (Producto)** del catálogo del lavadero.

El dominio cubre:

- Value Objects inmutables con validación.
- Agregado `Product` con sus invariantes.
- Eventos de dominio.
- Servicios de dominio.
- Especificaciones y estrategias de cálculo.
- Excepciones de negocio.
- Pruebas unitarias con JUnit 5.

---

## 2. Estructura

```text
src/main/java/com/lavadero/domain/
├─ model/         Agregados y entidades.
├─ valueobject/   Objetos de valor inmutables.
├─ enums/         Estados del negocio.
├─ event/         Eventos de dominio.
├─ exception/     Errores de negocio.
└─ service/       Servicios de dominio, Specification y Strategy.

src/test/java/com/lavadero/domain/
├─ model/         Pruebas del agregado.
├─ valueobject/   Pruebas de los Value Objects.
└─ service/       Pruebas de los servicios.
```

---

## 3. Reglas del dominio

1. El precio de compra y el precio de venta son mayores que cero.
2. El precio de venta es mayor o igual al precio de compra.
3. El stock nunca es negativo.
4. Todo producto pertenece a una categoría.
5. El stock mínimo es mayor o igual a cero.
6. El estado del producto es coherente con el stock.
7. Un producto descontinuado no recibe mercancía.
8. La unicidad del código se comprueba al consultar el catálogo, fuera de una instancia aislada.

La regla provisional del 35 % se encapsula en `SalePriceCalculator`:

```text
nuevoPrecioVenta = compraActual + (compraActual - compraAnterior) * 0.35
```

El resultado se redondea a dos decimales (`HALF_UP`). La interpretación debe confirmarse con el
docente; al aplicar el precio, el agregado conserva la regla de que la venta no quede por debajo
del costo.

---

## 4. Compilación y pruebas

```bash
mvn test
mvn verify   # genera el reporte JaCoCo
```

Última verificación local: **59 pruebas** aprobadas y **83,2 % de cobertura de líneas** en
`com.lavadero.domain`. El informe completo queda en `target/site/jacoco/index.html`.

---

## 5. Documentación

- [`docs/arquitectura.md`](docs/arquitectura.md): agregado, eventos, servicios y UML conceptual.
- [`docs/lenguaje-ubicuo.md`](docs/lenguaje-ubicuo.md): glosario del negocio.
