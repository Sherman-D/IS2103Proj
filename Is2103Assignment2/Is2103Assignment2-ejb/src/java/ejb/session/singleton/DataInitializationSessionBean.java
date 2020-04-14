package ejb.session.singleton;

import ejb.session.stateless.DoctorEntitySessionBeanLocal;
import ejb.session.stateless.PatientEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.EntityInstanceExistsInCollectionException;


@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean
{
    @EJB
    private DoctorEntitySessionBeanLocal  doctorEntitySessionBeanLocal;
    @EJB
    private PatientEntitySessionBeanLocal patientEntitySessionBeanLocal;
    @EJB
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    
    
    public DataInitializationSessionBean()
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
         initializeData();

    }
    
    
    
    private void initializeData()
    {
        staffEntitySessionBeanLocal.createNewStaff(new StaffEntity("Eric", "Some", "manager", "password"));
        staffEntitySessionBeanLocal.createNewStaff(new StaffEntity("Victoria", "Newton", "nurse", "password"));
        
        doctorEntitySessionBeanLocal.createNewDoctor(new DoctorEntity("Tan", "Ming", "S10011", "BMBS"));
        doctorEntitySessionBeanLocal.createNewDoctor(new DoctorEntity("Clair", "Hahn", "S41221", "MBBCh"));
        doctorEntitySessionBeanLocal.createNewDoctor(new DoctorEntity("Robert", "Blake", "S58201", "MBBS"));
        
        try 
        {
        patientEntitySessionBeanLocal.createNewPatient(new PatientEntity("S9867027A", "1234567", "Sarah", "Yi", "F", 22, "93718799", "13, Clementi Road"));
        patientEntitySessionBeanLocal.createNewPatient(new PatientEntity("G1314207T", "ABCDEFG", "Rajesh", "Singh", "M", 36, "93506839", "15, Mountbatten Road"));
        
        }
        catch (EntityInstanceExistsInCollectionException ex) 
        {
            
        }
        
    }
}