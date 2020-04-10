package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class AppointmentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    
    @OneToOne
    @Column(nullable = false)
    private Long patientId;
    
    @OneToOne
    @Column(nullable = false)
    private Long doctorId; 
    
    @Column(nullable = false)
    private LocalDateTime appointmentTime;
    private Boolean isCancelled = false;
    

    public AppointmentEntity() {
    }

    public AppointmentEntity(Long patientId, Long doctorId, LocalDateTime appointmentTime) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentTime = appointmentTime;
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
        
        if ((this.appointmentId == null && other.appointmentId != null) || (this.appointmentId != null && !this.appointmentId.equals(other.appointmentId))) 
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

    public Long getPatientId() {
        return patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }
}
