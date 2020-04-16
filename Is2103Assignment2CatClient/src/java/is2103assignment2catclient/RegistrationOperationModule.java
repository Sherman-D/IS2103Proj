package is2103assignment2catclient;

import ejb.session.singleton.QueueGeneratorSessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.ClinicEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.LeaveEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.LeaveEntity;
import entity.PatientEntity;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.exception.AppointmentNotFoundException;
import util.exception.DoctorNotFoundException;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.PatientNotFoundException;

public class RegistrationOperationModule 
{
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private ClinicEntitySessionBeanRemote clinicEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private QueueGeneratorSessionBeanRemote queueGeneratorSessionBeanRemote; 
    private LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote;


    public RegistrationOperationModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, ClinicEntitySessionBeanRemote clinicEntitySessionBeanRemote, DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote, PatientEntitySessionBeanRemote patientEntitySessionBeanRemote, QueueGeneratorSessionBeanRemote queueGeneratorSessionBeanRemote, LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.clinicEntitySessionBeanRemote = clinicEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
        this.queueGeneratorSessionBeanRemote = queueGeneratorSessionBeanRemote;
        this.leaveEntitySessionBeanRemote = leaveEntitySessionBeanRemote;
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
        
        System.out.print("Enter Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        
        String password = "";
        while (true) {
            System.out.print("Enter Password> ");
            password = scanner.nextLine().trim();
            if (password.length() != 6 || Pattern.compile("[^0-9]").matcher(password).matches()) 
            {
                System.out.println("Your password must be a sequence of 6 digit numbers.");
                continue;
            }
            break;
        }
        
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
        
        try 
        {
            String passwordHash = patientEntitySessionBeanRemote.hashPassword(password);
            PatientEntity newPatientEntity = new PatientEntity(identityNumber, passwordHash, firstName, lastName, gender, age, phone, address);
            patientEntitySessionBeanRemote.createNewPatient(newPatientEntity);
            System.out.println("Patient has been registered successfully!");
       
        } catch (EntityInstanceExistsInCollectionException | NoSuchAlgorithmException ex) 
        { 
            System.out.println("An error has occurred while creating the new patient: " + ex.getMessage() + "\n");
        }
    }
 
    
    
    //This will register a patient at the first available slot that the doctor has in the day. 
        private void registerWalkInConsult()
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** CARS :: Registration Operation :: Register Walk-In Consultation ***\n");

            System.out.println("Doctor:");
            System.out.println("Id |Name");
            LocalDateTime now = LocalDateTime.now();
        
            try 
            {
            List<DoctorEntity> doctorList = doctorEntitySessionBeanRemote.retrieveAllDoctors();
            List<LeaveEntity> leaveToday = leaveEntitySessionBeanRemote.retrieveLeaveByDate(now.toLocalDate());
            List<DoctorEntity> present = new ArrayList<>();
            String availString ="";
            
            //print list of doctors not on leave
            for(DoctorEntity de : doctorList)
            {
     
                for (LeaveEntity le : leaveToday)
                {
                    if (le.getDoctorId().equals(de.getDoctorId()))
                    {
                        present = null;
                    }
                }
                
                if(present != null)
                {
                    System.out.println(de.getDoctorId()+" |"+de.getFullName());
                    present.add(de);
                    availString += de.getDoctorId()+" |";
                }   
              }
            
            System.out.println("Availability:");
            System.out.println("Time  |"+ availString);
            
            List<LocalTime> consultSessions = clinicEntitySessionBeanRemote.retrieveConsultationSlots(LocalDate.now());
            
            boolean isClinicOpen = true;
            for (LocalTime session : consultSessions)
            {
                isClinicOpen = false;
                if (session.isAfter(LocalTime.now())) 
                {
                    isClinicOpen = true;
                }
            }
            
            if (!isClinicOpen) {
                System.out.println("There are no remaining consultation slots today.");
                return;
            }
            for (LocalTime session : consultSessions)
            {
                int counter = 0;
                if (counter > 6) {
                    break;
                }
                if (session.isAfter(LocalTime.now())){
                    String output = session.toString();
                    for (DoctorEntity doctor : doctorList)
                    {
                       output = appointmentEntitySessionBeanRemote.hasAppointment(doctor, LocalDateTime.of(LocalDate.now(), session))+ " |";
                    }
                    System.out.println(output);
                }
                counter ++;
            }
            
            System.out.print("Enter Doctor Id> ");
            String in = scanner.nextLine().trim();
            Long doctorId = Long.valueOf(in);
            
            DoctorEntity chosenDoctor = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(doctorId);
           
            if (!doctorList.contains(chosenDoctor))
            {
                System.out.println("The selected doctor is not available for the session indicated.");
                return;
            }
            
            System.out.print("Enter Patient Identity Number> ");
            String identityNumber = scanner.nextLine().trim();
            PatientEntity currentPatientEntity = patientEntitySessionBeanRemote.retrievePatientByIdentityNumber(identityNumber);
 
            String time = "";
            LocalDateTime fin = null; //placeholder
           /* for (int i=0 ; i < 6 ; i++)
            { //select earliest appt
               LocalDateTime cur = LocalDateTime.of(LocalDate.now(), LocalTime.of(curHour, curMin));
               String earliestTime = appointmentEntitySessionBeanRemote.hasAppointment(chosenDoctor, cur);
              
               if(earliestTime.equals("O")){
                   time = String.format("%01d:%02d", curHour, curMin);
                   fin = LocalDateTime.of(LocalDate.now(), LocalTime.of(curHour, curMin));
                   break;
               }
            }*/
            
                for (LocalTime session : consultSessions) {
                    int counter = 0;
                    if (counter > 6) {
                        break;
                    }
                    if (session.isAfter(LocalTime.now())) {
                        String earliestSession = appointmentEntitySessionBeanRemote.hasAppointment(chosenDoctor, LocalDateTime.of(LocalDate.now(), session));
                        if (earliestSession.equals("O")) {
                            time = session.toString();
                            fin = LocalDateTime.of(LocalDate.now(), session);
                            break;
                        }
                    }
                    counter++;
                }
           
            AppointmentEntity ae = new AppointmentEntity(currentPatientEntity, chosenDoctor, fin);
            ae.setIsConfirmed(true);
            int queueNumber = queueGeneratorSessionBeanRemote.getNextQueueNumber();
            System.out.println(currentPatientEntity.getFirstName() + " " + currentPatientEntity.getLastName() + " with Dr." + chosenDoctor.getFullName() + " has been booked at " + time);
            System.out.printf("Queue Number is %d. \n", queueNumber);
            
            
            
        } catch (PatientNotFoundException | DoctorNotFoundException ex) 
        {
            System.out.println("Error in walk-in registration: "+ ex.getMessage()+"\n");
        }
       }    

//    private void registerWalkInConsult()
//    {
//        Scanner scanner = new Scanner(System.in);
//        
//        System.out.println("Doctor: ");
//        System.out.println("Id |Name ");
//        //Fetch list of doctors for the current day
//        List<DoctorEntity> doctorList = doctorEntitySessionBeanRemote.retrieveAllDoctors();
//        LocalDateTime now = LocalDateTime.now();
//        List<LeaveEntity> leaveList = leaveEntitySessionBeanRemote.retrieveLeaveByDate(now); //Problematic with using currentTime as variable, especially if we're using dummy data. 
//        
//        for(DoctorEntity de : doctorList){
//            for(LeaveEntity le : leaveList){
//                if(!de.getDoctorId().equals(le.getDoctorId())){ // doctor is not on leave today
//                    System.out.println(de.getDoctorId()+" |"+ de.getFullName());
//                }
//            }
//        }
//        
//        System.out.println("Availability: ");
//        //Fetch timesheet for doctors today
//        
//        System.out.println("Enter Doctor Id> ");
//        Integer doctorId1 = Integer.parseInt(scanner.nextLine().trim());
//        //Fetch doctor entity based on ID
//        System.out.println("Enter Patient Identity Number> ");
//        String patientIn = scanner.nextLine().trim();
//        //Fetch patient entity based on ID
//        //System.out.printf(%s%s appointment with Dr. %s%s has been booked at %tH:%tM, patientFirstName, patientLastName, doctorFirstName, doctorLastName, appointmentTIme, appointmentTIme);
//        try
//        {
//            Long doctorId = Long.valueOf(doctorId1);
//            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(doctorId);
//            String hasAppt = appointmentEntitySessionBeanRemote.hasAppointment(de, now);
//            PatientEntity pe = patientEntitySessionBeanRemote.retrievePatientByPatientIdentityNumber(patientIn);
//            Long patientId = pe.getPatientId();
//            if(hasAppt.equals("X")){
//                System.out.println("Invalid appointment. Please choose again.");
//                menuRegistrationOperation();
//            }else{ // can create new appt
//                AppointmentEntity ae = new AppointmentEntity(patientId, doctorId, /*next avail time*/);
//                appointmentEntitySessionBeanRemote.createNewAppointment(ae);
//                System.out.println(pe.getFirstName()+""+pe.getLastName()+" appointment with Dr."+de.getFullName()+" has been booked at "+/*next avail time*/);
//            }
//        }
//        catch(DoctorNotFoundException | PatientNotFoundException ex)
//        {
//            System.out.println("Error in booking appointment: "+ex.getMessage()+"\n");
//        }
//    }
    
    //
     private void registerAppointmentConsult() 
    {
         Scanner scanner = new Scanner(System.in);
         
         System.out.print("Enter Patient Identity Number> ");
         String patientIn = scanner.nextLine().trim();
         List<String> patientAppointments  = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(patientIn);
         
         if (patientAppointments.isEmpty()) {
             System.out.println("There are no appointments associated with this identity number.");
             return;
         }

         
         System.out.println("Appointments: ");
         System.out.printf("%s-1|%s-7|%s-3|%s \n", "Id", "Date", "Time", "Doctor");
         
         for (String appointmentDetails : patientAppointments) 
         {
             System.out.println(appointmentDetails);
         }
                 
         try {
            System.out.println("Enter Appointment Id> ");
            Integer appointmentId1 = Integer.parseInt(scanner.nextLine().trim());
            Long appointmentId = Long.valueOf(appointmentId1);
            PatientEntity pe = patientEntitySessionBeanRemote.retrievePatientByPatientIdentityNumber(patientIn);
            Long patientId = pe.getPatientId();

            AppointmentEntity confirmedAppointment = appointmentEntitySessionBeanRemote.confirmAppointment(patientId, appointmentId);
            DoctorEntity doctor = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(confirmedAppointment.getDoctorId());
            int queueNumber = queueGeneratorSessionBeanRemote.getNextQueueNumber();
            
            
            System.out.printf("%s%s appointment is confirmed with Dr. %s at %d:%d", pe.getFirstName(), pe.getLastName(), doctor.getFullName(), confirmedAppointment.getAppointmentTime().getHour(), confirmedAppointment.getAppointmentTime().getMinute());
            System.out.printf("Queue Number is %d. \n", queueNumber);
         } 
         catch (PatientNotFoundException | DoctorNotFoundException | AppointmentNotFoundException ex) 
         {
             System.out.println("An error has occured while registering the appointment: " + ex.getMessage());
         }
         
    }
}
