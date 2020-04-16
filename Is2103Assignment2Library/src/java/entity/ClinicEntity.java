package entity;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class ClinicEntity implements Serializable, Comparable<ClinicEntity>{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private DayOfWeek operationDay;
    
    @Column(nullable = false)
    private LocalTime startOfDay;
    @Column(nullable = false)
    private LocalTime endOfDay;

    public ClinicEntity() {
    }

    public ClinicEntity(String day, String startOfDay, String endOfDay) {
        this.operationDay = DayOfWeek.valueOf(day);
        this.startOfDay = LocalTime.parse(startOfDay);
        this.endOfDay = LocalTime.parse(endOfDay);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDay() {
        return operationDay;
    }

    public void setDay(DayOfWeek day) {
        this.operationDay = day;
    }

    public LocalTime getStartOfDay() {
        return startOfDay;
    }

    public void setStartOfDay(LocalTime startOfDay) {
        this.startOfDay = startOfDay;
    }

    public LocalTime getEndOfDay() {
        return endOfDay;
    }

    public void setEndOfDay(LocalTime endOfDay) {
        this.endOfDay = endOfDay;
    }

    

    @Override
    public int hashCode() 
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ClinicEntity)) {
            return false;
        }
        ClinicEntity other = (ClinicEntity) object;
        if ((this.id == null && other.getId() != null) || (this.id != null && !this.id.equals(other.getId()))) {
            return false;
        }
        return true;
    }
    
    @Override
    public int compareTo(ClinicEntity anotherClinic) {
        return this.id.compareTo(anotherClinic.getId());
    }

    //e.g. "Monday: 08:00 - 16:00" 
    @Override
    public String toString() {
        return String.format("%s: %d:%d - %d:%d", operationDay, startOfDay.getHour(), startOfDay.getMinute(), endOfDay.getHour(), endOfDay.getMinute());
    }
    
}
