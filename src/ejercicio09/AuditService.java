package ejercicio09;

/**
 * Registra todos los eventos a los que esté suscrito.
 */
public class AuditService implements EventObserver {

    @Override
    public void onEvent(Event event) {
        System.out.println("Servicio de auditoría: evento " + event.type() + " registrado");
    }
}
