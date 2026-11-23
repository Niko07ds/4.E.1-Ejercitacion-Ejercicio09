package ejercicio09;

/**
 * Interfaz común de los servicios que escuchan eventos del bus.
 */
public interface EventObserver {

    void onEvent(Event event);
}
