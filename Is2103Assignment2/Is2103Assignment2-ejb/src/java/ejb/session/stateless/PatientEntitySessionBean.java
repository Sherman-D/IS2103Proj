package ejb.session.stateless;

import entity.PatientEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.PatientNotFoundException;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;




@Stateless
@Local(PatientEntitySessionBeanLocal.class)
@Remote(PatientEntitySessionBeanRemote.class)

public class PatientEntitySessionBean implements PatientEntitySessionBeanLocal, PatientEntitySessionBeanRemote
{
    @PersistenceContext(unitName = "Is2103Assignment2-ejbPU")
    private EntityManager entityManager;
    
    
    
    public PatientEntitySessionBean()
    {
    }
    
    
    
    @Override
    public Long createNewPatient(PatientEntity newPatientEntity) throws EntityInstanceExistsInCollectionException
    {
        PatientEntity existingPatientEntity = entityManager.find(PatientEntity.class, newPatientEntity.getIdentityNumber());
        
        if (existingPatientEntity == null) {
            entityManager.persist(newPatientEntity);
            entityManager.flush();
        } else {
            throw new EntityInstanceExistsInCollectionException("Patient with this Identity Number already exists");
        }
        
        return newPatientEntity.getPatientId();
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
                throw new InvalidLoginCredentialException("Username or password is incorrect!");
            }
        }
        catch(PatientNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Identity Number or password is incorrect!");
        }
    }
    
    
    
    @Override
    public List<PatientEntity> retrieveAllPatients()
    {
        Query query = entityManager.createQuery("SELECT p FROM PatientEntity p");
        
        return query.getResultList();
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

        if(patientEntity != null)
        {
            return patientEntity;
        }
        else
        {
            throw new PatientNotFoundException("Patient ID " + identityNumber + " does not exist!");
        }
    }
    
    
    @Override
    public void updatePatient(PatientEntity patientEntity) throws EntityMismatchException
    {
        PatientEntity existingPatientEntity = entityManager.find(PatientEntity.class, patientEntity.getPatientId());
        if (existingPatientEntity.getPatientId().equals(patientEntity.getPatientId())) 
        {
            entityManager.merge(patientEntity);
        } else 
        {
            throw new EntityMismatchException("The patient record being changed does not match the one stored!");
        }
    }
    
    
    
    @Override
    public void deletePatient(Long patientId) throws PatientNotFoundException
    {
        PatientEntity patientEntityToRemove = retrievePatientByPatientId(patientId);
        entityManager.remove(patientEntityToRemove);
    }
}