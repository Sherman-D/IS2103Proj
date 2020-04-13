package ejb.session.stateless;

import entity.AppointmentEntity;
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
    
}
