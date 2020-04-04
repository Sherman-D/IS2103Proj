package is2103assignment2catclient;

import java.util.Scanner;


public class AppointmentOperationModule {
    
    public void menuAppointmentOperation() 
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CARS :: Appointment Operation ***\n");
            System.out.println("1: View Patient Appointments");
            System.out.println("2: Add Appointment");
            System.out.println("3: Cancel Appointment");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    viewAppointments();
                }
                else if(response == 2)
                {
                    addAppointment();
                }
                else if(response == 3)
                {
                    cancelAppointment();
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
    
    private void viewAppointments() 
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CARS :: Appointment Operation :: View Patient Appointments ***");
        
        System.out.println("Enter Patient Identity Number> ");
        String patientId = scanner.nextLine().trim();
        
        System.out.println("Appointments: ");
        //Can view cancelled appointments
        //Parse list of appointments with Id | Date | Time | Doctor
    }
    
    private void addAppointment()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CARS :: Appointment Operation :: Add Appointment ***");
        
        System.out.println("Doctor: ");
        System.out.println("Id | Name");
        //Parse list of all doctors
        
        System.out.println("Enter Doctor Id> ");
        Integer doctorId = scanner.nextInt();
        System.out.println("Enter Date> ");
        String date = scanner.nextLine().trim();
        
        
        //System.out.println("Availability for %s%s on %tY-%tM-%D:", );
        
        System.out.println("Enter Time> ");
        //Error checking if time entered is not on the list of available timings
        String time = scanner.nextLine().trim();
        
        
        System.out.println("Enter Patient Identity Number> ");
        //Error checking if the ID entered does not belong to a registered patient
        String patientId = scanner.nextLine().trim();
        
        //Create appointment with date and timing given.
        
        //System.out.printf(%s%s appointment with Dr. %s%s at %tH:%tM on %tY-%tM-%tD has been added, patientFirstName, patientLastName, doctorFirstName, doctorLastName, appointmentTIme, appointmentTIme, appointmentTIme, appointmentTIme, appointmentTIme);
        
    }
    
    private void cancelAppointment()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CARS :: Appointment Operation :: Cancel Appointment ***");
        
        System.out.println("Enter Patient Identity Number> ");
        String patientId = scanner.nextLine().trim();
        
        System.out.println("Appointments: ");
        
        //Parse list of appointments with Id | Date | Time | Doctor
        
        System.out.println("Enter Appointment Id> ");
        Integer appointmentId = Integer.parseInt(scanner.nextLine().trim());
        //Send cancel status to database entry. 
         //System.out.printf(%s%s appointment with Dr. %s%s at %tH:%tM on %tY-%tM-%tD has been cancelled, patientFirstName, patientLastName, doctorFirstName, doctorLastName, appointmentTIme, appointmentTIme, appointmentTIme, appointmentTIme, appointmentTIme);
    }
}
