package ws.model;

import entity.AppointmentEntity;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RetrieveAppointmentByPatientIdResponse 
{
    private List<String> appointmentEntities;
    
    
    public RetrieveAppointmentByPatientIdResponse()
    {
        appointmentEntities = new ArrayList<>();
    }
    
    
    public RetrieveAppointmentByPatientIdResponse(List<String> appointmentEntities) 
    {
        this.appointmentEntities = appointmentEntities;
    }
    
    @XmlElements({
        @XmlElement(name="appointmentEntity", type=AppointmentEntity.class)
    })
    @XmlElementWrapper
    public List<String> getAppointmentEntities() {
        return appointmentEntities;
    }

    public void setAppointmentEntities(List<String> appointmentEntities) {
        this.appointmentEntities = appointmentEntities;
    }
}
