package ejb.session.stateless;

import entity.LeaveEntity;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.LeaveNotFoundException;
import util.exception.EntityMismatchException;




@Stateless
@Local(LeaveEntitySessionBeanLocal.class)
@Remote(LeaveEntitySessionBeanRemote.class)

public class LeaveEntitySessionBean implements LeaveEntitySessionBeanLocal, LeaveEntitySessionBeanRemote
{
    @PersistenceContext(unitName = "Is2103Assignment2-ejbPU")
    private EntityManager entityManager;
    
    
    
    public LeaveEntitySessionBean()
    {
    }
    
    
    @Override
    public Long createNewLeave(LeaveEntity newLeaveEntity)
    {
        entityManager.persist(newLeaveEntity);
        entityManager.flush();
        
        return newLeaveEntity.getLeaveId();
    }
    
    
    
    @Override
    public List<LeaveEntity> retrieveAllLeaves()
    {
        Query query = entityManager.createQuery("SELECT l FROM LeaveEntity l");
        
        return query.getResultList();
    }
   
    
    @Override
    public LeaveEntity retrieveLeaveByLeaveId(Long leaveId) throws LeaveNotFoundException
    {
        LeaveEntity leaveEntity = entityManager.find(LeaveEntity.class, leaveId);
        
        if(leaveEntity != null)
        {
            return leaveEntity;
        }
        else
        {
            throw new LeaveNotFoundException("Leave ID " + leaveId + " does not exist!");
        }
    }
    
    
    @Override
    public List<LeaveEntity> retrieveLeaveByDoctorId(Long doctorId) throws LeaveNotFoundException
    {
        Query query = entityManager.createQuery("SELECT l FROM LeaveEntity l WHERE l.doctorId = :searchId").setParameter("searchId", doctorId);
        List<LeaveEntity> leaveList = query.getResultList();
        
        if(!leaveList.isEmpty())
        {
            return leaveList;
        }
        else
        {
            throw new LeaveNotFoundException("The doctor associate with ID " + doctorId + " does not have any registered leaves!");
        }
    }
    
    
    @Override
    public List<LeaveEntity> retrieveLeaveByDate(LocalDateTime searchDate) throws LeaveNotFoundException
    {
        Query query = entityManager.createQuery("SELECT l FROM LeaveEntity l WHERE l.leaveDate = :searchDate").setParameter("searchDate", searchDate);
        List<LeaveEntity> leaveList = query.getResultList();
        
        if(!leaveList.isEmpty())
        {
            return leaveList;
        }
        else
        {
            throw new LeaveNotFoundException("The date given " + searchDate + " does not have any registered leaves!");
        }
    }
    
    
    @Override
    public void cancelLeave(LeaveEntity leaveEntity) throws EntityMismatchException
    {
        LeaveEntity existingLeaveEntity = entityManager.find(LeaveEntity.class, leaveEntity.getLeaveId());
        if (existingLeaveEntity.getLeaveId().equals(leaveEntity.getLeaveId())) 
        {
            entityManager.merge(leaveEntity);
        } else 
        {
            throw new EntityMismatchException("The leave record being changed does not match the one stored!");
        }
    }
    
}