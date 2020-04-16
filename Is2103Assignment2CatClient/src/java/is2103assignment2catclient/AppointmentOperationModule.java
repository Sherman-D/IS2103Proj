package is2103assignment2catclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import util.exception.AppointmentAlreadyCancelledException;
import util.exception.AppointmentNotFoundException;
import util.exception.DoctorNotFoundException;
import util.exception.EntityMismatchException;
import util.exception.PatientNotFoundException;


public class AppointmentOperationModule {
    
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;

    public AppointmentOperationModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote, PatientEntitySessionBeanRemote patientEntitySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
    }
    

    
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
        
        System.out.print("Enter Patient Identity Number> ");
        String patientIdentityNumber = scanner.nextLine().trim();
        
        try
        {
        PatientEntity patient = patientEntitySessionBeanRemote.retrievePatientByIdentityNumber(patientIdentityNumber);
        
        System.out.println("Appointments: ");
        System.out.printf("%-1s|%-7s|%-3s|%s\n", "Id", "Date", "Time", "Doctor");
        List<String> appointments = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(patient.getIdentityNumber());
        
        for (String appointment : appointments)
        {
            System.out.println(appointment);
        }
        
        }
        catch (PatientNotFoundException ex)
        {
            System.out.println("Patient not found!");
        }
    }
    
    private void addAppointment()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CARS :: Appointment Operation :: Add Appointment ***");
        
        System.out.println("Doctor: ");
        System.out.println("Id | Name");
        List<DoctorEntity> doctors = doctorEntitySessionBeanRemote.retrieveAllDoctors();
        for (DoctorEntity doctor : doctors)
        {
            System.out.printf("%d | %s \n", doctor.getDoctorId(), doctor.getFullName());
        }
        
        System.out.print("Enter Doctor Id> ");
        String did = scanner.nextLine().trim();
        Long doctorId = Long.valueOf(did);
        
        System.out.print("Enter Date> ");
        String d = scanner.nextLine().trim();
        LocalDate date = LocalDate.parse(d);
        LocalDate twoDays = LocalDate.now().plusDays(2);

        if (date.isBefore(twoDays))
           {
               System.out.println("Error: Appointments cannot be made less than two days to the consultation date.");
               return;
            }
        
        try{
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(doctorId);

            System.out.println("Availability for "+de.getFullName()+" on "+d+":" );
            
            StringBuilder slots = new StringBuilder();
           
            List<LocalTime> availableSlots = appointmentEntitySessionBeanRemote.retrieveDoctorAvailableSlotsOnDay(de, date);
            
            if (availableSlots.isEmpty())
            {
                System.out.println("This doctor is has no open consultation sessions on the selected date.");
                return;
            }
            
            for (LocalTime time : availableSlots) 
            {
                slots.append(time.toString());
                slots.append(" ");
            }
            System.out.println(slots.toString());
           
            LocalTime time = LocalTime.MIDNIGHT;
            
            while (true) {
                System.out.print("Enter Time> ");
                String t = scanner.nextLine().trim();
                if (t.equals("cancel")) {
                    return;
                }
                time = LocalTime.parse(t);

                if (!availableSlots.contains(time)) {
                    System.out.println("That timing is not available. Please choose another time slot. Otherwise, enter \" cancel \" to cancel.");
                    continue;
                }
                break;
            }

            System.out.print("Enter Patient Identity Number> ");
            //Error checking if the ID entered does not belong to a registered patient
            String patientId = scanner.nextLine().trim();
            PatientEntity patient = patientEntitySessionBeanRemote.retrievePatientByIdentityNumber(patientId);
        
            appointmentEntitySessionBeanRemote.createNewAppointment(new AppointmentEntity(patient, de, date.atTime(time)));
        
            System.out.printf("%s %s appointment with Dr. %s at %01d:%01d on %d-%01d-%01d has been added. \n", patient.getFirstName(), patient.getLastName(), de.getFullName(), time.getHour(), time.getMinute(), date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            }
        catch(PatientNotFoundException | DoctorNotFoundException ex)
        {
            System.out.println("Invalid Input: "+ex.getMessage());
        }
    }
    
    private void cancelAppointment()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CARS :: Appointment Operation :: Cancel Appointment ***");
        
        try 
        {
            System.out.print("Enter Patient Identity Number> ");
            String patientId = scanner.nextLine().trim();
            PatientEntity patient = patientEntitySessionBeanRemote.retrievePatientByIdentityNumber(patientId);
            
            System.out.println("Appointments: ");
            System.out.printf("%-1s|%-7s|%-3s|%s\n", "Id", "Date", "Time", "Doctor");
            List<String> appointments = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(patientId);
            if (appointments.isEmpty())
            {
                System.out.println("No appointments with the associated patient were found.");
                return;
            }
            
            for (String appointment : appointments)
            {
                System.out.println(appointment);
            }
            
            System.out.print("Enter Appointment Id> ");
            Long appointmentId = Long.parseLong(scanner.nextLine().trim());
            AppointmentEntity appointment = appointmentEntitySessionBeanRemote.retrieveAppointmentByAppointmentId(appointmentId);
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(appointment.getDoctorId());
            LocalDateTime appointmentTime = appointment.getAppointmentTime();
            
            appointmentEntitySessionBeanRemote.cancelAppointment(appointment);
            //Send cancel status to database entry. 
           System.out.printf("%s %s appointment with Dr. %s at %01d:%01d on %d-%01d-%01d has been cancelled. \n", patient.getFirstName(), patient.getLastName(), de.getFullName(), appointmentTime.getHour(), appointmentTime.getMinute(), appointmentTime.getYear(), appointmentTime.getMonthValue(), appointmentTime.getDayOfMonth());
        } catch(AppointmentNotFoundException | AppointmentAlreadyCancelledException |EntityMismatchException | PatientNotFoundException |DoctorNotFoundException ex)
        {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
