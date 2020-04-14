package is2103assignment2kioskclient;


import java.util.List;
import java.util.Scanner;
import entity.PatientEntity;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import util.exception.EntityManagerException;


public class AppointmentOperationModule {
 
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    
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
    { //can only be booked in adv by at least 2 days
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Self-Service Kiosk :: Add Appointment ***");
        
        System.out.println("Doctor: ");
        System.out.println("Id | Name");
        //Parse list of all doctors
        
        System.out.println("Enter Doctor Id> ");
        String did = scanner.nextLine().trim();
        Long doctorId = Long.valueOf(did);
        DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(doctorId);
        System.out.println("Enter Date> ");
        String d = scanner.nextLine().trim();
        LocalDate date = LocalDate.parse(date);
        LocalDate now = LocalDate.now();
        LocalDate twoDays = DateUtil.addDays(now, 2);
        if(date.before(twoDays)){
            throw new EntityManagerExceotion();
        }
        
        try
        {
        System.out.println("Availability for "+de.getFullName()+" on "+d+":");
        System.out.println("Enter Time> ");
        //Error checking if time entered is not on the list of available timings
        String t = scanner.nextLine().trim();
        LocalTime time = LocalTime.parse(t);
  
        //print avail timings
        
        //Create appointment with date and timing given.
        LocalDateTime appointmentTime = time.atDate(date);
        AppointmentEntity ae = new AppointmentEntity(currentPatientEntity.getPatientId(),doctorId, appointmentTime);
        appointmentEntitySessionBeanRemote.createNewAppointment(ae);
        
        System.out.printf(currentPatientEntity.getFirstName()+" "+currentPatientEntity.getLastName()
                +" appointment with Dr. "+de.getFullName()+" at "+t+" on "+d+" has been added");
        }
        catch(EntityManagerException ex)
        {
            System.out.println("Invalid Date entered! Please book at least 2 days in advance!");
        }
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
