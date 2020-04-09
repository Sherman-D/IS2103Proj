package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class LeaveEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;
    
    @ManyToOne
    @Column(nullable = false)
    private Long doctorId;
    
    @Column(nullable = false)
    private Timestamp leaveDate;

    public LeaveEntity() {
    }

    public LeaveEntity(Long doctorId, Timestamp leaveDate) {
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

    public Timestamp getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Timestamp leaveDate) {
        this.leaveDate = leaveDate;
    }
    
    
}
