package ws.model;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RetrieveDoctorAvailableSlotsOnDayResponse 
{
    private List<LocalTime> slots;
    
    
    public RetrieveDoctorAvailableSlotsOnDayResponse()
    {
        slots = new ArrayList<>();
    }
    
    
    public RetrieveDoctorAvailableSlotsOnDayResponse(List<LocalTime> slots) 
    {
        this.slots = slots;
    }
    
    @XmlElements({
        @XmlElement(name="appointmentEntity", type=LocalTime.class)
    })
    @XmlElementWrapper
    public List<LocalTime>  getSlots() {
        return slots;
    }

    public void setSlots(List<LocalTime> slots) {
        this.slots = slots;
    }
}
