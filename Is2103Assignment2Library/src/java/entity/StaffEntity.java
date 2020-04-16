package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StaffEntity implements Serializable, Comparable<StaffEntity>
{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;
    private String firstName;
    private String lastName;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private String password;

    public StaffEntity() {
    }

    public StaffEntity(Long staffId, String firstName, String lastName, String userName, String password) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }
    
    public StaffEntity(String firstName, String lastName, String userName, String password) {
//        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }
    
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.staffId != null ? this.staffId.hashCode() : 0);
        
        return hash;
    }

    
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof StaffEntity)) 
        {
            return false;
        }
        
        StaffEntity other = (StaffEntity) object;
        
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) 
        {
            return false;
        }
        
        return true;
    }

    
    @Override
    public String toString() 
    {
        return "entity.StaffEntity[ doctorId=" + this.staffId  + " ]";
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public int compareTo(StaffEntity anotherStaff) {
        return this.staffId.compareTo(anotherStaff.getStaffId());
    }
}
