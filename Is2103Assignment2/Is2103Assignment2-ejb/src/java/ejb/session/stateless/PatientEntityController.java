package ejb.session.stateless;

import entity.PatientEntity;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PatientNotFoundException;



@Stateless
@Local(PatientEntityControllerLocal.class)

public class PatientEntityController implements PatientEntityControllerLocal
{

    @PersistenceContext(unitName = "Is2103Assignment2-ejbPU")
    private EntityManager entityManager;
    
    
    
    
    public PatientEntityController()
    {
    }
    
    
    
    @Override
    public PatientEntity createNewPatient(PatientEntity newPatientEntity) throws EntityInstanceExistsInCollectionException
    {
        PatientEntity existingPatientEntity = entityManager.find(PatientEntity.class, newPatientEntity.getIdentityNumber());
        
        if (existingPatientEntity == null) {
            entityManager.persist(newPatientEntity);
            entityManager.flush();
        } else {
            throw new EntityInstanceExistsInCollectionException("Patient with this Identity Number already exists");
        }
        
        return newPatientEntity;
    }
    
    
    @Override
    public PatientEntity retrievePatientByPatientId(Long patientId) throws PatientNotFoundException
    {
        PatientEntity patientEntity = entityManager.find(PatientEntity.class, patientId);
        
        if(patientEntity != null)
        {
            return patientEntity;
        }
        else
        {
            throw new PatientNotFoundException("Patient ID " + patientId + " does not exist!");
        }
    }
    
     @Override
    public PatientEntity retrievePatientByPatientIdentityNumber(String identityNumber) throws PatientNotFoundException
    {
        PatientEntity patientEntity = entityManager.find(PatientEntity.class, identityNumber);
        
        if (patientEntity != null)
        {
            return patientEntity;
        }
        else
        {
            throw new PatientNotFoundException("No record exists with the given identity number!");
        }
    }
    
    
    @Override
    public PatientEntity patientLogin(String identityNumber, String password) throws InvalidLoginCredentialException
    {
        try
        {
            PatientEntity patientEntity = retrievePatientByPatientIdentityNumber(identityNumber);
            
            if(patientEntity.getPassword().equals(password))
            {        
                return patientEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Identity Number or password is incorrect!");
            }
        }
        catch(PatientNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Identity Number or password is incorrect!");
        }
    }
}