package com.lavadero.domain.event;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Base de todos los eventos de dominio del lavadero.
 *
 * <p>Un evento representa un hecho relevante que ya ocurrió. Expone un nombre estable y una fecha,
 * y transporta sus datos en un mapa de valores simples para que la infraestructura pueda
 * publicarlos sin acoplarse al modelo.
 */
public abstract class DomainEvent {

  private final String eventName;
  private final Instant occurredOn;

  protected DomainEvent(final String eventName) {
    this.eventName = Objects.requireNonNull(eventName, "eventName cannot be null");
    this.occurredOn = Instant.now();
  }

  public String eventName() {
    return eventName;
  }

  public Instant occurredOn() {
    return occurredOn;
  }

  /** Datos del evento en formato simple, apto para registros o mensajería. */
  public abstract Map<String, String> payload();

  @Override
  public String toString() {
    return eventName + " " + payload();
  }
}
