package ejercicio09;

/**
 * Envía el correo de bienvenida cuando se registra un usuario.
 */
public class WelcomeEmailService implements EventObserver {

    @Override
    public void onEvent(Event event) {
        System.out.println("Servicio de correo: enviando bienvenida a " + event.payload().get("email"));
    }
}
