package cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.model;

import java.awt.Image;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 *
 * @author michw
 */
public class Cliente {
    
    private String cedula;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String urlFoto;
    private LocalDateTime fechaRegistro;


    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }
    

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Cliente() {
    }

    public Cliente(String cedula, String nombre, String apellidos, LocalDate fechaNacimiento, String telefono, String email, String urlFoto, LocalDateTime fechaRegistro) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.urlFoto = urlFoto;
        this.fechaRegistro = fechaRegistro;
    }

    public int getEdad() {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
