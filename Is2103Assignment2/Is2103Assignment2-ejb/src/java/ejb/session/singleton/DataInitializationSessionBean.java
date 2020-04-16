package ejb.session.singleton;

import ejb.session.stateless.ClinicEntitySessionBeanLocal;
import ejb.session.stateless.DoctorEntitySessionBeanLocal;
import ejb.session.stateless.PatientEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.ClinicEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.security.NoSuchAlgorithmException;
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
    @EJB
    private ClinicEntitySessionBeanLocal clinicEntitySessionBeanLocal;
    
    
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
        
        try 
        {
            String passwordS1 = staffEntitySessionBeanLocal.hashPassword("password");

            staffEntitySessionBeanLocal.createNewStaff(new StaffEntity("Eric", "Some", "manager", passwordS1));
            staffEntitySessionBeanLocal.createNewStaff(new StaffEntity("Victoria", "Newton", "nurse", passwordS1));
        } catch (NoSuchAlgorithmException ex) {

        }
        
        
        doctorEntitySessionBeanLocal.createNewDoctor(new DoctorEntity("Tan", "Ming", "S10011", "BMBS"));
        doctorEntitySessionBeanLocal.createNewDoctor(new DoctorEntity("Clair", "Hahn", "S41221", "MBBCh"));
        doctorEntitySessionBeanLocal.createNewDoctor(new DoctorEntity("Robert", "Blake", "S58201", "MBBS"));
        
        clinicEntitySessionBeanLocal.createNewClinicTiming(new ClinicEntity("MONDAY", "08:30", "12:30"));
        clinicEntitySessionBeanLocal.createNewClinicTiming(new ClinicEntity("MONDAY", "13:30", "18:00"));
        clinicEntitySessionBeanLocal.createNewClinicTiming(new ClinicEntity("TUESDAY", "08:30", "12:30"));
        clinicEntitySessionBeanLocal.createNewClinicTiming(new ClinicEntity("TUESDAY", "13:30", "18:00"));
        clinicEntitySessionBeanLocal.createNewClinicTiming(new ClinicEntity("WEDNESDAY", "08:30", "12:30"));
        clinicEntitySessionBeanLocal.createNewClinicTiming(new ClinicEntity("WEDNESDAY", "13:30", "18:00"));
        clinicEntitySessionBeanLocal.createNewClinicTiming(new ClinicEntity("THURSDAY", "08:30", "12:30"));
        clinicEntitySessionBeanLocal.createNewClinicTiming(new ClinicEntity("THURSDAY", "13:30", "17:00"));
        clinicEntitySessionBeanLocal.createNewClinicTiming(new ClinicEntity("FRIDAY", "08:30", "12:30"));
        clinicEntitySessionBeanLocal.createNewClinicTiming(new ClinicEntity("FRIDAY", "13:30", "17:30"));
        
        try 
        {
        String password1 = patientEntitySessionBeanLocal.hashPassword("123456");
        String password2 = patientEntitySessionBeanLocal.hashPassword("654321");
        
        patientEntitySessionBeanLocal.createNewPatient(new PatientEntity("S9867027A", password1, "Sarah", "Yi", "F", 22, "93718799", "13, Clementi Road"));
        patientEntitySessionBeanLocal.createNewPatient(new PatientEntity("G1314207T", password2, "Rajesh", "Singh", "M", 36, "93506839", "15, Mountbatten Road"));
        
        }
        catch (EntityInstanceExistsInCollectionException | NoSuchAlgorithmException ex) 
        {
            
        }
        
    }
}