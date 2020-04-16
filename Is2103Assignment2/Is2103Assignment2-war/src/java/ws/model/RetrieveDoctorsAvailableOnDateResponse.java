package ws.model;

import entity.DoctorEntity;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RetrieveDoctorsAvailableOnDateResponse 
{
    private List<DoctorEntity> doctorEntities;
    
    
    public RetrieveDoctorsAvailableOnDateResponse()
    {
        doctorEntities = new ArrayList<>();
    }
    
    
    public RetrieveDoctorsAvailableOnDateResponse(List<DoctorEntity> doctorEntities) 
    {
        this.doctorEntities = doctorEntities;
    }
    
    @XmlElements({
        @XmlElement(name="doctorEntity", type=DoctorEntity.class)
    })
    @XmlElementWrapper
    public List<DoctorEntity> getDoctorEntities() {
        return doctorEntities;
    }

    public void setDoctorEntities(List<DoctorEntity> doctorEntities) {
        this.doctorEntities = doctorEntities;
    }
}
