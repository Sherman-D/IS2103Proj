package ejb.session.stateless;

import entity.StaffEntity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;




@Stateless
@Local(StaffEntitySessionBeanLocal.class)
@Remote(StaffEntitySessionBeanRemote.class)

public class StaffEntitySessionBean implements StaffEntitySessionBeanLocal, StaffEntitySessionBeanRemote
{
    @PersistenceContext(unitName = "Is2103Assignment2-ejbPU")
    private EntityManager entityManager;
    
    
    
    public StaffEntitySessionBean()
    {
    }
    
    
    
    @Override
    public Long createNewStaff(StaffEntity newStaffEntity)
    {
        entityManager.persist(newStaffEntity);
        entityManager.flush();
        
        return newStaffEntity.getStaffId();
    }
    
    
    
    @Override
    public List<StaffEntity> retrieveAllStaffs()
    {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException
    {
        StaffEntity staffEntity = entityManager.find(StaffEntity.class, staffId);
        
        if(staffEntity != null)
        {
            return staffEntity;
        }
        else
        {
            throw new StaffNotFoundException("Staff ID " + staffId + " does not exist!");
        }
    }
    
    
    
    @Override
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.userName = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (StaffEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new StaffNotFoundException("Staff Username " + username + " does not exist!");
        }
    }
    
    
    
    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException, NoSuchAlgorithmException
    {
        try
        {
            StaffEntity staffEntity = retrieveStaffByUsername(username);
            
            if(staffEntity.getPassword().equals(password))
            {        
                return staffEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username or password is incorrect!");
            }
        }
        catch(StaffNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username or password is incorrect!");
        }
    }
    
    
    
    @Override
    public void updateStaff(StaffEntity staffEntity) throws EntityMismatchException
    {
        StaffEntity existingStaffEntity = entityManager.find(StaffEntity.class, staffEntity.getStaffId());
        if (existingStaffEntity.getStaffId().equals(staffEntity.getStaffId())) 
        {
            entityManager.merge(staffEntity);
        } else 
        {
            throw new EntityMismatchException("The staff record being changed does not match the one stored!");
        }
    }
    
    
    
    @Override
    public void deleteStaff(Long staffId) throws StaffNotFoundException
    {
        StaffEntity staffEntityToRemove = retrieveStaffByStaffId(staffId);
        entityManager.remove(staffEntityToRemove);
    }
    
    @Override
    public String hashPassword(String password) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] b = md.digest();
        StringBuffer sb = new StringBuffer();
        for(byte b1 : b){
            sb.append(Integer.toHexString(b1&0xff).toString());
        }
        return sb.toString();
    }
}