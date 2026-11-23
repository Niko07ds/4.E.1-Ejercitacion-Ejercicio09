package ejercicio09;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Servicio de usuarios: publica el evento UserRegistered.
 */
public class UserService {

    private final EventBus bus;

    public UserService(EventBus bus) {
        this.bus = bus;
    }

    public CompletableFuture<Void> register(String name, String email) {
        System.out.println("Servicio de usuarios: se registró a " + name);
        return bus.publish(new Event(EventTypes.USER_REGISTERED, Map.of("name", name, "email", email)));
    }
}
