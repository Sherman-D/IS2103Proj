/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DoctorEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DoctorNotFoundException;
import util.exception.EntityMismatchException;


@Stateless
@Local(DoctorEntityControllerLocal.class)
public class DoctorEntityController implements DoctorEntityControllerLocal 
{
    @PersistenceContext(unitName = "Is2103Assignment2-ejbPU")
    private EntityManager entityManager;
    
    
    
    public DoctorEntityController()
    {
    }
    
    
    
    @Override
    public Long createNewDoctor(DoctorEntity newDoctorEntity)
    {
        entityManager.persist(newDoctorEntity);
        entityManager.flush();
        
        return newDoctorEntity.getDoctorId();
    }
    
    
    
    @Override
    public List<DoctorEntity> retrieveAllDoctors()
    {
        Query query = entityManager.createQuery("SELECT d FROM DoctorEntity d");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public DoctorEntity retrieveDoctorByDoctorId(Long doctorId) throws DoctorNotFoundException
    {
        DoctorEntity doctorEntity = entityManager.find(DoctorEntity.class, doctorId);
        
        if(doctorEntity != null)
        {
            return doctorEntity;
        }
        else
        {
            throw new DoctorNotFoundException("Doctor ID " + doctorId + " does not exist!");
        }
    }

    @Override
    public DoctorEntity retrieveDoctorByDoctorName(String firstName, String lastName) throws DoctorNotFoundException
    {
        DoctorEntity doctorEntity = entityManager.find(DoctorEntity.class, firstName);
        DoctorEntity doctorEntity1 = entityManager.find(DoctorEntity.class, lastName);

        if(doctorEntity != null && doctorEntity1.equals(doctorEntity))
        {
            return doctorEntity;
        }
        else
        {
            throw new DoctorNotFoundException("Doctor Name " + firstName+" "+lastName + " does not exist!");
        }
    }
    
    @Override
    public List<DoctorEntity> retrieveDoctorsAvailableOnDate(LocalDate searchDate) throws DoctorNotFoundException
    {
        Query query1 = entityManager.createQuery("select d from DoctorEntity d");
        Query query2 = entityManager.createQuery("select d from DoctorEntity d, LeaveEntity l where l.leaveDate = :searchDate").setParameter("searchDate", searchDate);
        
        List<DoctorEntity> availableDoctorList = new ArrayList<>();
        try {
          List<DoctorEntity> fullDoctorList = query1.getResultList();
          List<DoctorEntity> doctorLeaveList = query2.getResultList();
          
          for (DoctorEntity doctor : fullDoctorList)
          {
              if (!doctorLeaveList.contains(doctor)) {
                  availableDoctorList.add(doctor);
              }
          }
        } 
        catch (NoResultException ex)
        {
            
        }
 
        
        if(!availableDoctorList.isEmpty())
        {
            return availableDoctorList;
        }
        else
        {
            throw new DoctorNotFoundException("The date given " + searchDate + " does not have any available doctors!");
        }
    }
    
    
    @Override
    public void updateDoctor(DoctorEntity doctorEntity) throws EntityMismatchException
    {
        DoctorEntity existingDoctorEntity = entityManager.find(DoctorEntity.class, doctorEntity.getDoctorId());
        if (existingDoctorEntity.getDoctorId().equals(doctorEntity.getDoctorId())) 
        {
            entityManager.merge(doctorEntity);
        } else 
        {
            throw new EntityMismatchException("The doctor record being changed does not match the one stored!");
        }
    }
    
    
    
    @Override
    public void deleteDoctor(Long doctorId) throws DoctorNotFoundException
    {
        DoctorEntity doctorEntityToRemove = retrieveDoctorByDoctorId(doctorId);
        entityManager.remove(doctorEntityToRemove);
    }
}