package ejb.session.stateless;

import entity.ClinicEntity;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
@Remote
public class ClinicEntitySessionBean implements ClinicEntitySessionBeanLocal, ClinicEntitySessionBeanRemote {

    @PersistenceContext(unitName = "Is2103Assignment2-ejbPU")
    private EntityManager entityManager;

    public ClinicEntitySessionBean() {
    }
    
    @Override
    public Long createNewClinicTiming(ClinicEntity newClinicEntity)
    {
        entityManager.persist(newClinicEntity);
        entityManager.flush();
        
        return newClinicEntity.getId();
    } 
    
    @Override
    public List<ClinicEntity> retrieveClinicTimingsByDay(String day)
    {
        Query query = entityManager.createQuery("SELECT c FROM ClinicEntity c WHERE c.day = :searchDay");
        query.setParameter("searchDay", day);
        
        
        List<ClinicEntity> clinicTimings = query.getResultList();
        return clinicTimings;
    }
    
    @Override
    public List<LocalTime> retrieveConsultationSlots(LocalDate searchDate)
    {
        List<ClinicEntity> clinicTimings = retrieveClinicTimingsByDay(searchDate.getDayOfWeek().toString());
        List<LocalTime> consultationSlots = new ArrayList<>();
        
        for (ClinicEntity clinic : clinicTimings)
        {
            LocalTime time = clinic.getStartOfDay();
            consultationSlots.add(time);
            
            while (time.isBefore(clinic.getEndOfDay()))
            {
                time.plusMinutes(30);
                consultationSlots.add(time);
            }
        }
        
        return consultationSlots;
    }
}
