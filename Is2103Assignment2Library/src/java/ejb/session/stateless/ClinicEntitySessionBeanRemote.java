package ejb.session.stateless;

import entity.ClinicEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface ClinicEntitySessionBeanRemote
{

    Long createNewClinicTiming(ClinicEntity newClinicEntity);
    
    List<ClinicEntity> retrieveClinicTimingsByDay(String day);
    
    List<LocalTime> retrieveConsultationSlots(LocalDate searchDate);
}
