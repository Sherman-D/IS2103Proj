package ejb.session.stateless;

import entity.PatientEntity;
import java.util.List;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.PatientNotFoundException;
import util.exception.EntityMismatchException;


public interface PatientEntitySessionBeanRemote
{

    public Long createNewPatient(PatientEntity newPatientEntity) throws EntityInstanceExistsInCollectionException;
    
    List<PatientEntity> retrieveAllPatients();
    
    PatientEntity retrievePatientByPatientId(Long patientId) throws PatientNotFoundException;

    void updatePatient(PatientEntity patientEntity) throws EntityMismatchException;
    
    void deletePatient(Long patientId) throws PatientNotFoundException;
}
