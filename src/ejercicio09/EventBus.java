package ejercicio09;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Bus de eventos centralizado.
 * - Indexa los observadores por tipo de evento para enrutar rápido.
 * - Usa colecciones concurrentes para soportar suscripciones desde varios hilos.
 * - Despacha de forma asíncrona y aísla los errores de cada observador.
 */
public class EventBus {

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<EventObserver>> subscribers = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public void subscribe(String eventType, EventObserver observer) {
        subscribers.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>()).addIfAbsent(observer);
    }

    public void unsubscribe(String eventType, EventObserver observer) {
        CopyOnWriteArrayList<EventObserver> list = subscribers.get(eventType);
        if (list != null) {
            list.remove(observer);
        }
    }

    /**
     * Publica un evento y lo despacha solo a los observadores registrados para ese tipo.
     *
     * @return un futuro que se completa cuando todos los observadores terminaron
     */
    public CompletableFuture<Void> publish(Event event) {
        List<EventObserver> targets = subscribers.getOrDefault(event.type(), new CopyOnWriteArrayList<>());
        if (targets.isEmpty()) {
            System.out.println("Bus de eventos: el evento " + event.type() + " no tiene suscriptores");
            return CompletableFuture.completedFuture(null);
        }

        List<CompletableFuture<Void>> deliveries = new ArrayList<>();
        for (EventObserver observer : targets) {
            deliveries.add(CompletableFuture.runAsync(() -> deliver(observer, event), executor));
        }
        return CompletableFuture.allOf(deliveries.toArray(new CompletableFuture[0]));
    }

    private void deliver(EventObserver observer, Event event) {
        try {
            observer.onEvent(event);
        } catch (RuntimeException e) {
            // Un fallo en un oyente no interrumpe la notificación a los demás
            System.out.println("Bus de eventos: " + observer.getClass().getSimpleName() + " falló al procesar "
                    + event.type() + " (" + e.getMessage() + "), se continúa con los demás servicios");
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
