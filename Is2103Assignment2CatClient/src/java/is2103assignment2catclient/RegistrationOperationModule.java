package is2103assignment2catclient;

import java.util.Scanner;

public class RegistrationOperationModule {
    
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
        String patientId = scanner.nextLine().trim();
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
        
        //Insert Patient Entity Creation
        try 
        {
            System.out.println("Patient has been registered successfully!");
       
        //Catch entity manager exceptions, and if the patient already exists in system
        } catch () 
        {}
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
         
         System.out.println("Appointments: ");
         //Fetch list of appointments. Throw error message if no appointments are logged with the ID number
         
         System.out.println("Enter Appointment Id> ");
         Integer appointmentId = Integer.parseInt(scanner.nextLine().trim());
         //Throw error if the appointment is not today?
         //Parse doctorId,
         //System.out.printf(%s%s appointment is confirmed with Dr. %s%s at %tH:%tM, patientFirstName, patientLastName, doctorFirstName, doctorLastName, appointmentTIme, appointmentTIme);
         
    }
}
