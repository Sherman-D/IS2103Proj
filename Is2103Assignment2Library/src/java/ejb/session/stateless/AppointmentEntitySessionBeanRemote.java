package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.List;
import util.exception.AppointmentAlreadyCancelledException;
import util.exception.AppointmentNotFoundException;
import util.exception.EntityMismatchException;


public interface AppointmentEntitySessionBeanRemote
{

     Long createNewAppointment(AppointmentEntity newAppointmentEntity);
    
    List<AppointmentEntity> retrieveAllAppointments();
    
    AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId) throws AppointmentNotFoundException;
    
    List<String> retrieveAppointmentByPatientIdentityNo(String patientId);
    
    void cancelAppointment(AppointmentEntity appointmentEntity) throws EntityMismatchException, AppointmentAlreadyCancelledException;
}
