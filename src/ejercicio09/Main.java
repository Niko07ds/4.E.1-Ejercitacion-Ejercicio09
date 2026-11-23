package ejercicio09;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        EventBus bus = new EventBus();

        EventObserver welcomeEmail = new WelcomeEmailService();
        EventObserver invoice = new InvoiceService();
        EventObserver audit = new AuditService();
        EventObserver faulty = new FaultyService();

        try {
            System.out.println("Registrando servicios en el bus de eventos");
            bus.subscribe(EventTypes.USER_REGISTERED, welcomeEmail);
            bus.subscribe(EventTypes.PAYMENT_APPROVED, invoice);
            bus.subscribe(EventTypes.USER_REGISTERED, audit);
            bus.subscribe(EventTypes.PAYMENT_APPROVED, audit);
            bus.subscribe(EventTypes.USER_REGISTERED, faulty);
            bus.subscribe(EventTypes.PAYMENT_APPROVED, faulty);

            UserService userService = new UserService(bus);
            PaymentService paymentService = new PaymentService(bus);

            System.out.println();
            System.out.println("Evento 1: usuario registrado (un servicio falla, los demás continúan)");
            userService.register("Sofía Romero", "sofia@example.com").join();

            System.out.println();
            System.out.println("Evento 2: pago aprobado");
            paymentService.approve("PED-1001", 25990.50).join();

            System.out.println();
            System.out.println("Se desvincula el servicio defectuoso");
            bus.unsubscribe(EventTypes.USER_REGISTERED, faulty);
            bus.unsubscribe(EventTypes.PAYMENT_APPROVED, faulty);

            System.out.println();
            System.out.println("Evento 3: otro usuario registrado");
            userService.register("Mateo Díaz", "mateo@example.com").join();

            System.out.println();
            System.out.println("Evento 4: un tipo de evento sin suscriptores");
            bus.publish(new Event("OrderCancelled", Map.of("orderId", "PED-1001"))).join();
        } finally {
            bus.shutdown();
        }
    }
}
