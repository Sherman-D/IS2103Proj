package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.DoctorEntity;
import java.time.LocalDateTime;
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
    
    boolean hasAppointment(DoctorEntity doctorEntity, LocalDateTime appointmentTime);
    
    void confirmAppointment(Long patientId,Long appointmentId) throws AppointmentNotFoundException;

   void cancelAppointment(AppointmentEntity appointmentEntity) throws EntityMismatchException, AppointmentAlreadyCancelledException;
    
}
