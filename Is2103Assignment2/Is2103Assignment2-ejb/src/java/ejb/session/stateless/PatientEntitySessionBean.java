package ejb.session.stateless;

import entity.PatientEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
    public PatientEntity retrievePatientByIdentityNumber(String identityNo) throws PatientNotFoundException
    {
        Query query = entityManager.createQuery("SELECT p FROM PatientEntity p WHERE p.identityNumber = :identityNo");
        query.setParameter("identityNo",  identityNo);
        
        try
        {
            return (PatientEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new PatientNotFoundException("No patient record with given identity number exists!");
        }
    }
    
    
    @Override
    public PatientEntity patientLogin(String identityNo, String password) throws InvalidLoginCredentialException, PatientNotFoundException
    {
        try
        {
            PatientEntity patientEntity = retrievePatientByIdentityNumber(identityNo);
            
            if(patientEntity.getPassword().equals(password))
            {        
                return patientEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("The login details provided are incorrect!");
            }
        }
        catch(PatientNotFoundException ex)
        {
            throw new PatientNotFoundException("No patient record found with the given details!");
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