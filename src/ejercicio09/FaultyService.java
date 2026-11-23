package ejercicio09;

/**
 * Servicio defectuoso, pensado para demostrar el aislamiento de errores:
 * siempre lanza una excepción al recibir un evento.
 */
public class FaultyService implements EventObserver {

    @Override
    public void onEvent(Event event) {
        throw new IllegalStateException("fallo simulado");
    }
}
