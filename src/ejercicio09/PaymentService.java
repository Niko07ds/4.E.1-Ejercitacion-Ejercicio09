package ejercicio09;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Servicio de pagos: publica el evento PaymentApproved.
 */
public class PaymentService {

    private final EventBus bus;

    public PaymentService(EventBus bus) {
        this.bus = bus;
    }

    public CompletableFuture<Void> approve(String orderId, double amount) {
        System.out.println("Servicio de pagos: se aprobó el pago del pedido " + orderId);
        return bus.publish(new Event(EventTypes.PAYMENT_APPROVED, Map.of("orderId", orderId, "amount", amount)));
    }
}
