package is2103assignment2catclient;


import ejb.session.singleton.QueueGeneratorSessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.ClinicEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.LeaveEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import java.util.Scanner;
import ejb.session.stateless.StaffEntitySessionBeanRemote;
import entity.StaffEntity;
import java.security.NoSuchAlgorithmException;
import util.exception.InvalidLoginCredentialException;
import util.exception.EntityManagerException;

public class MainApp {
    
    private AdministrationOperationModule administrationOperationModule;
    private AppointmentOperationModule appointmentOperationModule;
    private RegistrationOperationModule registrationOperationModule;
    
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private ClinicEntitySessionBeanRemote clinicEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private QueueGeneratorSessionBeanRemote queueGeneratorSessionBeanRemote; 
    private StaffEntitySessionBeanRemote staffEntitySessionBeanRemote;

    private StaffEntity currentStaffEntity;


    public MainApp(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, ClinicEntitySessionBeanRemote clinicEntitySessionBeanRemote, DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote, LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote, PatientEntitySessionBeanRemote patientEntitySessionBeanRemote, QueueGeneratorSessionBeanRemote queueGeneratorSessionBeanRemote, StaffEntitySessionBeanRemote staffEntitySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.clinicEntitySessionBeanRemote = clinicEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.leaveEntitySessionBeanRemote = leaveEntitySessionBeanRemote;
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
        this.queueGeneratorSessionBeanRemote = queueGeneratorSessionBeanRemote;
        this.staffEntitySessionBeanRemote = staffEntitySessionBeanRemote;
    }

    
    public void runApp(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Clinic Appointment Registration System (CARS) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        administrationOperationModule = new AdministrationOperationModule(doctorEntitySessionBeanRemote, patientEntitySessionBeanRemote, leaveEntitySessionBeanRemote  , staffEntitySessionBeanRemote);
                        appointmentOperationModule = new AppointmentOperationModule(appointmentEntitySessionBeanRemote, doctorEntitySessionBeanRemote, patientEntitySessionBeanRemote);
                        registrationOperationModule = new RegistrationOperationModule(appointmentEntitySessionBeanRemote, clinicEntitySessionBeanRemote, doctorEntitySessionBeanRemote, patientEntitySessionBeanRemote, queueGeneratorSessionBeanRemote, leaveEntitySessionBeanRemote);
                        
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException | EntityManagerException | NoSuchAlgorithmException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException,  EntityManagerException, NoSuchAlgorithmException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** CARS :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
      
        if(username.length() > 0 && password.length() > 0)
        {
            currentStaffEntity = staffEntitySessionBeanRemote.staffLogin(username, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
       
    }
    
    private void menuMain() 
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CARS :: Main ***\n");
            
            System.out.printf("You are login as %s %s \n", currentStaffEntity.getFirstName(), currentStaffEntity.getLastName());
      
            System.out.println("1: Registration Operation");
            System.out.println("2: Appointment Operation");
            System.out.println("3: Adminstration Operation");
            System.out.println("4: Logout \n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    registrationOperationModule.menuRegistrationOperation();
                }
                else if(response == 2)
                {
                    
                    appointmentOperationModule.menuAppointmentOperation();
                    
                }
                else if (response == 3)
                {
                    administrationOperationModule.menuAdministrationOperation();
                }
                else if (response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
}
