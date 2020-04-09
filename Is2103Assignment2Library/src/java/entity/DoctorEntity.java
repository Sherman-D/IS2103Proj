package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class DoctorEntity implements Serializable 
{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;
    private String firstName;
    private String lastName;     
    private String registration;    
    //What if a doctor has more than one qualification?
    private String qualification;

    public DoctorEntity() {
    }

    public DoctorEntity(Long doctorId, String firstName, String lastName, String registration, String qualification) {
        this.doctorId = doctorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registration = registration;
        this.qualification = qualification;
    }

    public DoctorEntity(String firstName, String lastName, String registration, String qualification) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.registration = registration;
        this.qualification = qualification;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.doctorId != null ? this.doctorId.hashCode() : 0);
        
        return hash;
    }

    
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof DoctorEntity)) 
        {
            return false;
        }
        
        DoctorEntity other = (DoctorEntity) object;
        
        if ((this.doctorId == null && other.doctorId != null) || (this.doctorId != null && !this.doctorId.equals(other.doctorId))) 
        {
            return false;
        }
        
        return true;
    }

    
    @Override
    public String toString() 
    {
        return "entity.DoctorEntity[ doctorId=" + this.doctorId  + " ]";
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    
    
}
