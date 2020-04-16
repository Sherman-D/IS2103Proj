package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.ClinicEntity;
import entity.DoctorEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AppointmentAlreadyCancelledException;
import util.exception.AppointmentNotFoundException;
import util.exception.EntityMismatchException;



@Stateless
@Local(AppointmentEntityControllerLocal.class)

public class AppointmentEntityController implements AppointmentEntityControllerLocal
{

    @PersistenceContext(unitName = "Is2103Assignment2-ejbPU")
    private EntityManager entityManager;
    
    
    
    
    public AppointmentEntityController()
    {
    }
    
    
    
    @Override
    public AppointmentEntity createNewAppointment(AppointmentEntity newAppointmentEntity)
    {
 
            entityManager.persist(newAppointmentEntity);
            entityManager.flush();

        return newAppointmentEntity;
    }
    
    
    @Override
    public AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId) throws AppointmentNotFoundException
    {
        AppointmentEntity appointmentEntity = entityManager.find(AppointmentEntity.class, appointmentId);
        
        if(appointmentEntity != null)
        {
            return appointmentEntity;
        }
        else
        {
            throw new AppointmentNotFoundException("Appointment ID " + appointmentId + " does not exist!");
        }
    }
    
     @Override
    public List<String> retrieveAppointmentByPatientId(Long patientId)
    {
        Query query = entityManager.createQuery("SELECT a FROM AppointmentEntity a , PatientEntity p WHERE p.patientId = :searchId AND p.patientId = a.patient ").setParameter("searchId", patientId);
        
        List<AppointmentEntity> patientAppointmentList = query.getResultList();
        
        
        ArrayList<String> appointmentDetails = new ArrayList<>();

        for (AppointmentEntity appointment : patientAppointmentList)
        {
            DoctorEntity servingDoctor = entityManager.find(DoctorEntity.class, appointment.getDoctorId());
            String appointmentLine = String.format("%s-1|%s-7|%s-3|%s", appointment.getAppointmentId(), appointment.getAppointmentTime().format(DateTimeFormatter.ISO_LOCAL_DATE), appointment.getAppointmentTime().format(DateTimeFormatter.ISO_LOCAL_TIME), servingDoctor.getFullName());
            appointmentDetails.add(appointmentLine);
        }
        
        return appointmentDetails;
    }
    
    
    @Override
    public void confirmAppointment(Long patientId,Long appointmentId) throws AppointmentNotFoundException
    {
        AppointmentEntity ae = retrieveAppointmentByAppointmentId(appointmentId);
        if(ae.getPatientId().equals(patientId)){
            ae.setIsConfirmed(true);
        }

    }
    
    
    @Override
    public AppointmentEntity cancelAppointment(AppointmentEntity appointmentEntity) throws EntityMismatchException, AppointmentAlreadyCancelledException
    {
        
        AppointmentEntity existingAppointmentEntity = entityManager.find(AppointmentEntity.class, appointmentEntity.getAppointmentId());
        
        if (existingAppointmentEntity.getIsCancelled())
        {
            throw new AppointmentAlreadyCancelledException("The specified appointment has already been cancelled!");
        }      
        
        if (existingAppointmentEntity.getAppointmentId().equals(appointmentEntity.getAppointmentId())) 
        {
            appointmentEntity.cancelAppointment();
            entityManager.merge(appointmentEntity);
            return appointmentEntity;
        } else 
        {
            throw new EntityMismatchException("The appointment record being cancelled does not match the one stored!");
        }
    } 
    
    @Override
    public List<LocalTime> retrieveDoctorAvailableSlotsOnDay(Long doctorId, LocalDate date)
    {
        Query query = entityManager.createQuery("SELECT a FROM AppointmentEntity a WHERE a.doctor= ?1 ");
        query.setParameter(1, doctorId);
        
        List<AppointmentEntity> appointments = query.getResultList();
        List<LocalTime> slotsOnDate = new ArrayList<>();
        for (AppointmentEntity appointment : appointments)
        {
            if (appointment.getAppointmentTime().toLocalDate().equals(date) && !(appointment.getIsCancelled()))
            {
                slotsOnDate.add(appointment.getAppointmentTime().toLocalTime());
            }
        }
        
        query = entityManager.createQuery("SELECT c FROM ClinicEntity c WHERE c.operationDay = :searchDay");
        query.setParameter("searchDay", date.getDayOfWeek().toString());
        List<ClinicEntity> clinicTimings = query.getResultList();
        
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
        
        List<LocalTime> availableSlots = new ArrayList<>();
        for (LocalTime slot : consultationSlots) {
            if (!(slotsOnDate.contains(slot)))
            {
                availableSlots.add(slot);
            }
        }
        
        return availableSlots;
    }
    
    @Override
    public List<String> retrieveAppointmentByPatientIdentityNo(String patientId)
    {
        Query query = entityManager.createQuery("SELECT a FROM AppointmentEntity a , PatientEntity p WHERE p.identityNumber = :searchId AND p.patientId = a.patient ").setParameter("searchId", patientId);
        
        List<AppointmentEntity> patientAppointmentList = query.getResultList();
        
        
        ArrayList<String> appointmentDetails = new ArrayList<>();

        for (AppointmentEntity appointment : patientAppointmentList)
        {
            DoctorEntity servingDoctor = entityManager.find(DoctorEntity.class, appointment.getDoctorId());
            String appointmentLine = String.format("%s-1|%s-7|%s-3|%s", appointment.getAppointmentId(), appointment.getAppointmentTime().format(DateTimeFormatter.ISO_LOCAL_DATE), appointment.getAppointmentTime().format(DateTimeFormatter.ISO_LOCAL_TIME), servingDoctor.getFullName());
            appointmentDetails.add(appointmentLine);
        }
        return appointmentDetails;
        
    }
}