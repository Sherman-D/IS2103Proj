package is2103assignment2kioskclient;

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
    
    //This will register a patient at the first available slot that the doctor has in the day. 
    protected void registerWalkInConsult(PatientEntity currentPatientEntity)
    {
        System.out.println("*** Self-Service Kiosk :: Register Walk-In Consultation ***");
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Doctor: ");
        System.out.println("Id  |  Name ");
        //Fetch list of doctors for the current day
        
        System.out.println("Availability: ");
        //Fetch timesheet for doctors today
        
        System.out.println("Enter Doctor Id> ");
        Integer doctorId = Integer.parseInt(scanner.nextLine().trim());
        //Fetch doctor entity based on ID
        //System.out.printf(%s%s appointment with Dr. %s%s has been booked at %tH:%tM, patientFirstName, patientLastName, doctorFirstName, doctorLastName, appointmentTIme, appointmentTIme);  
       
    }
    
    //
    protected void registerAppointmentConsult(PatientEntity currentPatientEntity) 
    {
         System.out.println("*** Self-Service Kiosk :: Register Consultation By Appointment ***");
         Scanner scanner = new Scanner(System.in);
         
         List<String> patientAppointments  = appointmentEntitySessionBeanRemote. retrieveAppointmentByPatientIdentityNo(currentPatientEntity.getIdentityNumber());
         
         if (patientAppointments.isEmpty()) {
             System.out.println("There are no appointments registered.");
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
