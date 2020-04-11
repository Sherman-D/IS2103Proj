package is2103assignment2kioskclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import entity.PatientEntity;
import java.util.Scanner;
import util.exception.EntityManagerException;
import util.exception.InvalidLoginCredentialException;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.PatientNotFoundException;

public class MainApp {
   
    private AppointmentOperationModule appointmentOperationModule;
    private RegistrationOperationModule registrationOperationModule;
    
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    
    private PatientEntity  currentPatientEntity;
    
     public MainApp() 
    {        
    }
     
     public MainApp(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote,  PatientEntitySessionBeanRemote patientEntitySessionBeanRemote)
     {        
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
    }
    
    public void runApp(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Self-Service Kiosk ***\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1) 
                {
                    try 
                    {
                        doRegistration();
                        System.out.println("Registration is successful!");
                        
                        menuMain();
                    } catch (EntityInstanceExistsInCollectionException |EntityManagerException ex)
                    {
                        System.out.println("An error has occured: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException | PatientNotFoundException | EntityManagerException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 3)
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
    
    private void doRegistration() throws EntityInstanceExistsInCollectionException, EntityManagerException
    {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("*** Self-Service Kiosk :: Registration ***\n");
       
        System.out.print("Enter Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.print("Enter Password> ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Enter Gender> ");
        String gender = scanner.nextLine().trim();
        System.out.print("Enter Age> ");
        Integer age = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter Phone> ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter Address> ");
        String address = scanner.nextLine().trim();
        
        PatientEntity newPatientEntity = new PatientEntity(identityNumber, password, firstName, lastName, gender, age, phone, address);
        Long newPatientId = -1L;
        
        try
        {
            newPatientId= patientEntitySessionBeanRemote.createNewPatient(newPatientEntity);
        }
        catch (EntityInstanceExistsInCollectionException ex)
        {
            throw ex;
        }
       
        try 
        {
            newPatientEntity = patientEntitySessionBeanRemote.retrievePatientByPatientId(newPatientId);
            currentPatientEntity = newPatientEntity;
        } catch (PatientNotFoundException ex)
        {
           throw new EntityManagerException("Database error. Please approach one of our staff.");
        }
    }
    
    
    private void doLogin() throws InvalidLoginCredentialException, PatientNotFoundException, EntityManagerException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** Self-Service Kiosk :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentPatientEntity = patientEntitySessionBeanRemote.patientLogin(username, password);      
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
            System.out.println("*** Self-Service Kiosk :: Main ***\n");
            
            System.out.printf("You are login as %s %s", currentPatientEntity.getFirstName(), currentPatientEntity.getLastName());
      
            System.out.println("1: Register Walk-In Consultation");
            System.out.println("2: Register Consultation By Appointment");
            System.out.println("3: View Appointments");
            System.out.println("4: Add Appointment");
            System.out.println("5: Cancel Appointment");
            System.out.println("6: Logout \n");
            response = 0;
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    registrationOperationModule.registerWalkInConsult(currentPatientEntity);
                }
                else if(response == 2)
                {
                    registrationOperationModule.registerAppointmentConsult(currentPatientEntity);
                    
                }
                else if (response == 3)
                {
                    appointmentOperationModule.viewAppointments(currentPatientEntity);
                }
                else if (response == 4)
                {
                    appointmentOperationModule.viewAppointments(currentPatientEntity);
                }
                else if (response  == 5)
                {
                    appointmentOperationModule.cancelAppointment(currentPatientEntity);
                }
                else if (response == 6)
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
