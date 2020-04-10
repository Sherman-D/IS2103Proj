package is2103assignment2catclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import entity.PatientEntity;
import java.util.List;
import java.util.Scanner;
import util.exception.AppointmentNotFoundException;
import util.exception.EntityInstanceExistsInCollectionException;

public class RegistrationOperationModule 
{
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;

    public RegistrationOperationModule() 
    {
    }

    public RegistrationOperationModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote, PatientEntitySessionBeanRemote patientEntitySessionBeanRemote) 
    {
        this();
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
    }
    
    
    public void menuRegistrationOperation() 
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CARS :: Registration Operation ***\n");
            System.out.println("1: Register New Patient");
            System.out.println("2: Register Walk-In Consultation");
            System.out.println("3: Register Consultation By Appointment");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    registerNewPatient();
                }
                else if(response == 2)
                {
                    registerWalkInConsult();
                }
                else if(response == 3)
                {
                    registerAppointmentConsult();
                }
                else if(response == 4)
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
    private void registerNewPatient() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** *** CARS :: Registration Operation :: Register New Patient *** ***\n");
        
        System.out.println("Enter Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.println("Enter Password> ");
        String password = scanner.nextLine().trim();
        System.out.println("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.println("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();
        System.out.println("Enter Gender> ");
        String gender = scanner.nextLine().trim();
        System.out.println("Enter Age> ");
        Integer age = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Enter Phone> ");
        String phone = scanner.nextLine().trim();
        System.out.println("Enter Address> ");
        String address = scanner.nextLine().trim();
        
        PatientEntity newPatientEntity = new PatientEntity(identityNumber, password, firstName, lastName, gender, age, phone, address);
        try 
        {
            patientEntitySessionBeanRemote.createNewPatient(newPatientEntity);
            System.out.println("Patient has been registered successfully!");
       
        } catch (EntityInstanceExistsInCollectionException ex) 
        { 
            System.out.println("An error has occurred while creating the new patient: " + ex.getMessage() + "\n");
        }
    }
    
    //This will register a patient at the first available slot that the doctor has in the day. 
    private void registerWalkInConsult()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Doctor: ");
        System.out.println("Id  |  Name ");
        //Fetch list of doctors for the current day
        
        System.out.println("Availability: ");
        //Fetch timesheet for doctors today
        
        System.out.println("Enter Doctor Id> ");
        Integer doctorId = Integer.parseInt(scanner.nextLine().trim());
        //Fetch doctor entity based on ID
        System.out.println("Enter Patient Identity Number> ");
        String patientId = scanner.nextLine().trim();
        //Fetch patient entity based on ID
        //System.out.printf(%s%s appointment with Dr. %s%s has been booked at %tH:%tM, patientFirstName, patientLastName, doctorFirstName, doctorLastName, appointmentTIme, appointmentTIme);
        
        
        
       
    }
    
    //
     private void registerAppointmentConsult() 
    {
         Scanner scanner = new Scanner(System.in);
         
         System.out.println("Enter Patient Identity Number> ");
         String patientId = scanner.nextLine().trim();
         List<String> patientAppointments  = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(patientId);
         
         if (patientAppointments.isEmpty()) {
             System.out.println("There are no appointments associated with this identity number.");
             return;
         }
         //Fetch list of appointments. Throw error message if no appointments are logged with the ID number
         System.out.println("Appointments: ");
         System.out.printf("%s-1|%s-7|%s-3|%s", "Id", "Date", "Time", "Doctor");
         
         for (String appointmentDetails : patientAppointments) 
         {
             System.out.println(appointmentDetails);
         }
                 
         
         System.out.println("Enter Appointment Id> ");
         Integer appointmentId = Integer.parseInt(scanner.nextLine().trim());
         
         //appointmentEntitySessionBeanRemote.confirmAppointment(patientId, appointmentId);
         //Throw error if the appointment is not today?
         //Parse doctorId,
         //System.out.printf(%s%s appointment is confirmed with Dr. %s%s at %tH:%tM, patientFirstName, patientLastName, doctorFirstName, doctorLastName, appointmentTIme, appointmentTIme);
         
    }
}
