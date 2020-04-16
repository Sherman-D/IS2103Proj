package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity
public class AppointmentEntity implements Serializable, Comparable<AppointmentEntity>  
{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    
    @ManyToOne
    @JoinColumn(name = "patientId", nullable = false)
    private PatientEntity patient;
    
    @ManyToOne
    @JoinColumn(name = "doctorId", nullable = false)
    private DoctorEntity doctor; 
    
    @Column(nullable = false)
    private LocalDateTime appointmentTime;
    
    @Column(nullable = false)
    private String dateTime;

    
    private Boolean isCancelled = false;
    private Boolean isConfirmed = false;
    

    public AppointmentEntity() {
    }

    public AppointmentEntity(PatientEntity patient, DoctorEntity doctor, LocalDateTime appointmentTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentTime = appointmentTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dateTime = appointmentTime.format(formatter);

    }
    
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.appointmentId != null ? this.appointmentId.hashCode() : 0);
        
        return hash;
    }

    
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof AppointmentEntity)) 
        {
            return false;
        }
        
        AppointmentEntity other = (AppointmentEntity) object;
        
        if ((this.appointmentId == null && other.getAppointmentId() != null) || (this.appointmentId != null && !this.appointmentId.equals(other.getAppointmentId()))) 
        {
            return false;
        }
        
        return true;
    }
    
    public void cancelAppointment()
    {
        isCancelled = true;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
    }

    
    public Long getPatientId() {
        return patient.getPatientId();
    }

    public Long getDoctorId() {
        return doctor.getDoctorId();
    }


    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dateTime = appointmentTime.format(formatter);
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }
    
    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }
    
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
   
    
    @Override
    public int compareTo(AppointmentEntity anotherAppointment) {
        return this.appointmentId.compareTo(anotherAppointment.getAppointmentId());
    }
}

