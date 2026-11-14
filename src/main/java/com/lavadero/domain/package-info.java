/**
 * Capa de dominio del CEA Lavadero.
 *
 * <p>Contiene el modelo de negocio del catálogo y no depende de frameworks, bases de datos ni
 * interfaces de usuario. Todas las reglas del producto (precios, stock, estado y proveedores) se
 * expresan aquí y se prueban de forma aislada.
 *
 * <p>Subpaquetes:
 *
 * <ul>
 *   <li>{@code model}: agregados y entidades.
 *   <li>{@code valueobject}: objetos de valor inmutables con validación.
 *   <li>{@code enums}: estados y clasificaciones.
 *   <li>{@code event}: eventos de dominio.
 *   <li>{@code exception}: excepciones de negocio.
 *   <li>{@code service}: servicios de dominio, Specification y Strategy.
 * </ul>
 */
package com.lavadero.domain;
