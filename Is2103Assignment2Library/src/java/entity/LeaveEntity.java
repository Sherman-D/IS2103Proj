package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class LeaveEntity implements Serializable, Comparable<LeaveEntity>
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;
    
    @ManyToOne
    @Column(nullable = false)
    private Long doctorId;
    
    @Column(nullable = false)
//    private LocalDateTime leaveDate;
    private LocalDate leaveDate;

    public LeaveEntity() {
    }

    public LeaveEntity(Long doctorId, /*LocalDateTime*/LocalDate leaveDate) {
        this.doctorId = doctorId;
        this.leaveDate = leaveDate;
    }

    public Long getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Long leaveId){ this.leaveId = leaveId; }
    
    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public /*LocalDateTime*/LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }
    
    @Override
    public int compareTo(LeaveEntity anotherLeave) {
        return this.leaveId.compareTo(anotherLeave.getLeaveId());
    }
}
