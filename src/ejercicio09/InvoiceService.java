package ejercicio09;

/**
 * Genera la factura cuando se aprueba un pago.
 */
public class InvoiceService implements EventObserver {

    @Override
    public void onEvent(Event event) {
        double amount = (double) event.payload().get("amount");
        System.out.println("Servicio de facturación: generando factura del pedido " + event.payload().get("orderId")
                + " por " + String.format("%.2f", amount));
    }
}
