package ejb.session.stateless;

import entity.PatientEntity;
import java.util.List;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PatientNotFoundException;



public interface PatientEntitySessionBeanLocal
{

    public Long createNewPatient(PatientEntity newPatientEntity) throws EntityInstanceExistsInCollectionException;
    
    List<PatientEntity> retrieveAllPatients();
    
    PatientEntity retrievePatientByPatientId(Long patientId) throws PatientNotFoundException;

    PatientEntity retrievePatientByPatientIdentityNumber(String identityNumber) throws PatientNotFoundException;

    PatientEntity patientLogin(String identityNumber, String password) throws InvalidLoginCredentialException;

    void updatePatient(PatientEntity patientEntity) throws EntityMismatchException;
    
    void deletePatient(Long patientId) throws PatientNotFoundException;
}
