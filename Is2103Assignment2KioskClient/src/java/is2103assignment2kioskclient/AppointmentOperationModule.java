package is2103assignment2kioskclient;


import java.util.List;
import java.util.Scanner;
import entity.PatientEntity;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;


public class AppointmentOperationModule {
 
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    
    protected void viewAppointments(PatientEntity currentPatientEntity) 
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Self-Service Kiosk :: View Appointments ***");
        
        List<String> appointmentList = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(currentPatientEntity.getIdentityNumber());
        System.out.println("Appointments: ");
        System.out.printf("%s-1|%s-7|%s-3|%s", "Id", "Date", "Time", "Doctor");
        for (String appointment : appointmentList)
        {
            System.out.println(appointment);
        }
        //Can view cancelled appointments
        //Parse list of appointments with Id | Date | Time | Doctor
    }
    
    protected void addAppointment(PatientEntity currentPatientEntity)
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Self-Service Kiosk :: Add Appointment ***");
        
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
        
        
      //Patient ID is in current entity
        
        //Create appointment with date and timing given.
        
        //System.out.printf(%s%s appointment with Dr. %s%s at %tH:%tM on %tY-%tM-%tD has been added, patientFirstName, patientLastName, doctorFirstName, doctorLastName, appointmentTIme, appointmentTIme, appointmentTIme, appointmentTIme, appointmentTIme);
        
    }
    
    protected void cancelAppointment(PatientEntity currentPatientEntity)
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Self-Service Kiosk :: Cancel Appointment ***");
        
       List<String> appointmentList = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(currentPatientEntity.getIdentityNumber());
        System.out.println("Appointments: ");
        System.out.printf("%s-1|%s-7|%s-3|%s", "Id", "Date", "Time", "Doctor");
        for (String appointment : appointmentList)
        {
            System.out.println(appointment);
        }
        
        System.out.println("Enter Appointment Id> ");
        Integer appointmentId = Integer.parseInt(scanner.nextLine().trim());
        //Send cancel status to database entry. 
         //System.out.printf(%s%s appointment with Dr. %s%s at %tH:%tM on %tY-%tM-%tD has been cancelled, patientFirstName, patientLastName, doctorFirstName, doctorLastName, appointmentTIme, appointmentTIme, appointmentTIme, appointmentTIme, appointmentTIme);
    }
}
