package cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.model;

import java.time.LocalDateTime;

public class Ticket {
    private String id;
    private Customer customer;
    private Procedure procedure;
    private Station station;
    private boolean isPriority;
    private LocalDateTime arrivalTime;

    public Ticket() {
    }

    public Ticket(String id, Customer customer, Procedure procedure, Station station, 
                  boolean isPriority, LocalDateTime arrivalTime) {
        this.id = id;
        this.customer = customer;
        this.procedure = procedure;
        this.station = station;
        this.isPriority = isPriority;
        this.arrivalTime = arrivalTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public boolean isIsPriority() {
        return isPriority;
    }

    public void setIsPriority(boolean isPriority) {
        this.isPriority = isPriority;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "Ticket{" + "id=" + id +
                ", customer=" + customer +
                ", procedure=" + procedure +
                ", station=" + station +
                ", isPriority=" + isPriority +
                ", arrivalTime=" + arrivalTime +
                '}';
    } 
}