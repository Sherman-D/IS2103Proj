package ejb.session.stateless;

import entity.DoctorEntity;
import java.time.LocalDateTime;
import java.util.List;
import util.exception.DoctorNotFoundException;
import util.exception.EntityMismatchException;


public interface DoctorEntitySessionBeanRemote
{

    Long createNewDoctor(DoctorEntity newDoctorEntity);
    
    List<DoctorEntity> retrieveAllDoctors();
    
    DoctorEntity retrieveDoctorByDoctorId(Long doctorId) throws DoctorNotFoundException;
    
    List<DoctorEntity> retrieveDoctorsAvailableOnDate(LocalDateTime searchDate) throws DoctorNotFoundException;

    void updateDoctor(DoctorEntity doctorEntity) throws EntityMismatchException;
    
    void deleteDoctor(Long doctorId) throws DoctorNotFoundException;
}
