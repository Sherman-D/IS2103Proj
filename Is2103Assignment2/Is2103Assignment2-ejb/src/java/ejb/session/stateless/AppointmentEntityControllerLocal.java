package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.DoctorEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import util.exception.AppointmentAlreadyCancelledException;
import util.exception.AppointmentNotFoundException;
import util.exception.EntityMismatchException;



public interface AppointmentEntityControllerLocal
{
    AppointmentEntity createNewAppointment(AppointmentEntity newAppointmentEntity);
    
    AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId) throws AppointmentNotFoundException;
    
   List<String> retrieveAppointmentByPatientId(Long patientId);
    
    void confirmAppointment(Long patientId,Long appointmentId) throws AppointmentNotFoundException;
    
    void cancelAppointment(AppointmentEntity appointmentEntity) throws EntityMismatchException, AppointmentAlreadyCancelledException;

    List<LocalTime> retrieveDoctorAvailableSlotsOnDay(DoctorEntity doctorEntity, LocalDate date);
    
    List<String> retrieveAppointmentByPatientIdentityNo(String patientId);

    
}
