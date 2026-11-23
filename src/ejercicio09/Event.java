package ejercicio09;

import java.util.Map;

/**
 * Evento estructurado con un identificador de tipo y un payload de datos.
 * El payload se copia de forma inmutable para que ningún servicio lo altere.
 */
public record Event(String type, Map<String, Object> payload) {

    public Event {
        payload = Map.copyOf(payload);
    }
}
