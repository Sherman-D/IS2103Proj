package entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class LeaveEntity implements Serializable, Comparable<LeaveEntity>
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;
    
    @ManyToOne
    @JoinColumn(name = "doctorId", nullable = false)
    private DoctorEntity doctor;
    
    @Column(nullable = false)
    private LocalDate leaveDate;

    public LeaveEntity() {
    }

    public LeaveEntity(DoctorEntity doctor, LocalDate leaveDate) {
        this.doctor = doctor;
        this.leaveDate = leaveDate;
    }

    public Long getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Long leaveId){ this.leaveId = leaveId; }
    
    public Long getDoctorId() {
        return doctor.getDoctorId();
    }
    
    public DoctorEntity getDoctorEntity() 
    {
        return this.doctor;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.leaveId != null ? this.leaveId.hashCode() : 0);
        
        return hash;
    }
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof LeaveEntity)) 
        {
            return false;
        }
        
        LeaveEntity other = (LeaveEntity) object;
        
        if ((this.leaveId == null && other.getLeaveId() != null) || (this.leaveId != null && !this.leaveId.equals(other.getLeaveId()))) 
        {
            return false;
        }
        
        return true;
    }
    
    @Override
    public int compareTo(LeaveEntity anotherLeave) {
        return this.leaveId.compareTo(anotherLeave.getLeaveId());
    }
}
