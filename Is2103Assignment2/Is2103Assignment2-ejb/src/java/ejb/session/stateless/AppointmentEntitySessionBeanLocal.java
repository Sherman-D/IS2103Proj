package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.DoctorEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import util.exception.AppointmentAlreadyCancelledException;
import util.exception.EntityMismatchException;
import util.exception.AppointmentNotFoundException;



public interface AppointmentEntitySessionBeanLocal
{

    Long createNewAppointment(AppointmentEntity newAppointmentEntity);
    
    List<AppointmentEntity> retrieveAllAppointments();
    
    AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId) throws AppointmentNotFoundException;
    
    List<String> retrieveAppointmentByPatientIdentityNo(String patientId);
    
    List<LocalTime> retrieveDoctorAvailableSlotsOnDay(DoctorEntity doctorEntity, LocalDate date);
    
    String hasAppointment(DoctorEntity doctorEntity, LocalDateTime appointmentTime);
    
   AppointmentEntity confirmAppointment(Long patientId,Long appointmentId) throws AppointmentNotFoundException;

   void cancelAppointment(AppointmentEntity appointmentEntity) throws EntityMismatchException, AppointmentAlreadyCancelledException;
    
}
