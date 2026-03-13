package cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.Period;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {
    private String id; 
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String phoneNumber;
    private String email;
    private String photoUrl;
    private LocalDate birthDate;

    public Customer() {
    }
    
    public Customer(String id, String name, String paternalSurname, String maternalSurname,
           String phoneNumber, String email, String photoUrl, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.paternalSurname = paternalSurname;
        this.maternalSurname = maternalSurname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.photoUrl = photoUrl;
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPaternalSurname() {
        return paternalSurname;
    }
    
    public void setPaternalSurname(String paternalSurname) {
        this.paternalSurname = paternalSurname;
    }
    
    public String getMaternalSurname() {
        return maternalSurname;
    }
    
    public void setMaternalSurname(String maternalSurname) {
        this.maternalSurname = maternalSurname;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhotoUrl() {
        return photoUrl;
    }
    
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    @JsonIgnore
    public String getFullName() {
        return name + paternalSurname + maternalSurname;
    }
    
    @JsonIgnore
    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Person{" +
               "Id=" + id +
               ", name=" + name +
               ", paternalSurname=" + paternalSurname +
               ", maternalSurname=" + maternalSurname +
               ", phoneNumber=" + phoneNumber +
               ", email=" + email +
               ", photoUrl=" + photoUrl +
               ", birthDate=" + birthDate +
               '}';
    }
}