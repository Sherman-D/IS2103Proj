package ejb.session.stateless;

import entity.PatientEntity;
import java.security.NoSuchAlgorithmException;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PatientNotFoundException;



public interface PatientEntityControllerLocal
{
    PatientEntity createNewPatient(PatientEntity newPatientEntity) throws EntityInstanceExistsInCollectionException;
    
    PatientEntity retrievePatientByPatientId(Long patientId) throws PatientNotFoundException;
    
    PatientEntity retrievePatientByPatientIdentityNumber(String identityNumber) throws PatientNotFoundException;
    
    PatientEntity patientLogin(String identityNumber, String password) throws InvalidLoginCredentialException, NoSuchAlgorithmException;
    
}
