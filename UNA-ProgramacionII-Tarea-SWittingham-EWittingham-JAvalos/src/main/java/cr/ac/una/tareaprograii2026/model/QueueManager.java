package cr.ac.una.tareaprograii2026.model;

import java.util.LinkedList;
import java.util.Queue;

public class QueueManager {
    private static QueueManager INSTANCE=null;
    private Queue<Ticket> normalQueue; //en java ya existe clase Queue asi que usamos esa
    private Queue<Ticket> priorityQueue;
    private QueueManager() {
        this.normalQueue = new LinkedList<>();
        this.priorityQueue = new LinkedList<>();
    }
    
    
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (QueueManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new QueueManager();
                }
            }
        }
    }
    
    public static QueueManager getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    
    // Método para insertar una ficha
    public void enqueueTicket(Ticket ticket) {
        if (ticket.isIsPriority()) {
            priorityQueue.add(ticket);
        } else {
            normalQueue.add(ticket);
        }
    }
    
    // Método para llamar al siguiente
    public Ticket dequeueNext() {
        if (!priorityQueue.isEmpty()) {
            return priorityQueue.poll();
        }
        return normalQueue.poll();
    }
}
