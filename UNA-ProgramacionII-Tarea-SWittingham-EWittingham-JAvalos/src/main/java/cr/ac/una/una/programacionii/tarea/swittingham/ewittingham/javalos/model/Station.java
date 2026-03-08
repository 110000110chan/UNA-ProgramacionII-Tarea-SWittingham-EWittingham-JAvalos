package cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.model;

import java.util.List;

public class Station {
    private Branch branch;
    private String name;
    private boolean isPriorityStation;
    private boolean isActive;
    private List<Procedure> assignedProcedures;

    public Station() {
    }

    public Station(Branch branch, String name, boolean isPriorityStation, boolean isActive, List<Procedure> assignedProcedures) {
        this.branch = branch;
        this.name = name;
        this.isPriorityStation = isPriorityStation;
        this.isActive = isActive;
        this.assignedProcedures = assignedProcedures;
    }

    public Branch getBranch() {
        return branch;
    }
    
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public boolean isPriorityStation() {
        return isPriorityStation;
    }
    
    public void setIsPriorityStation(boolean isPriorityStation) {
        this.isPriorityStation = isPriorityStation;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<Procedure> getAssignedProcedures() {
        return assignedProcedures;
    }
    
    public void setAssignedProcedures(List<Procedure> assignedProcedures) {
        this.assignedProcedures = assignedProcedures;
    }

    @Override
    public String toString() {
        return "Station{" +
                "branch=" + branch +
                ", name=" + name +
                ", isPriorityStation=" + isPriorityStation +
                ", assignedProcedures=" + assignedProcedures +
                '}';
    }
}