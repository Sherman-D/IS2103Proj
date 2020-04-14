package is2103assignment2catclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
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
import util.exception.AppointmentNotFoundException;
import util.exception.DoctorNotFoundException;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.LeaveNotFoundException;
import util.exception.PatientNotFoundException;

public class RegistrationOperationModule 
{
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote;
    private PatientEntity currentPatientEntity;

    public RegistrationOperationModule() 
    {
    }

    public RegistrationOperationModule(PatientEntity currentPatientEntity, AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote, PatientEntitySessionBeanRemote patientEntitySessionBeanRemote) 
    {
        this();
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
        this.currentPatientEntity = currentPatientEntity;
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
        
        try 
        {
            String passwordHash = patientEntitySessionBeanRemote.hashPassword(password);
            PatientEntity newPatientEntity = new PatientEntity(identityNumber, passwordHash, firstName, lastName, gender, age, phone, address);
            patientEntitySessionBeanRemote.createNewPatient(newPatientEntity);
            System.out.println("Patient has been registered successfully!");
       
        } catch (EntityInstanceExistsInCollectionException ex) 
        { 
            System.out.println("An error has occurred while creating the new patient: " + ex.getMessage() + "\n");
        }
        catch(NoSuchAlgorithmException ex){
            System.out.println("An error has occurred while creating the new patient: " + ex.getMessage() + "\n");
        }
    }
 
    
    
    //This will register a patient at the first available slot that the doctor has in the day. 
        private void registerWalkInConsult()
        {
            int queueNumber = 0;//TEMPORARY VARIABLE - Replace with queueNumber method from session bean
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** Self-Service Kiosk :: Register Walk-In Consultation ***\n");

            System.out.println("Doctor:");
            System.out.println("Id |Name");
            LocalDateTime now = LocalDateTime.now();
        
            try 
            {
            List<DoctorEntity> doctorList = doctorEntitySessionBeanRemote.retrieveAllDoctors();
            List<LeaveEntity> leaveToday = leaveEntitySessionBeanRemote.retrieveLeaveByDate(now);
            List<DoctorEntity> present = new ArrayList<DoctorEntity>();
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
            
            //initialising hour n min
            int curHour = now.getHour();
            int curMin = now.getMinute();
            if(curMin>30)
            {
                curHour++;
            }else if(curMin>0){
                curMin = 30; //earliest appt
            }else if (curMin==0){
                curMin = 0;
            }
            
            
            for(int i=0;i<6;i++)
            { //printing X and O
               String out = curHour+":"+curMin+" |";
               LocalDateTime cur = LocalDateTime.of(LocalDate.now(), LocalTime.of(curHour, curMin));
               
               for(DoctorEntity de : doctorList){
                   out += appointmentEntitySessionBeanRemote.hasAppointment(de, cur)+ " |";
               }
               System.out.println(out);
               
               if(curMin==30)
               {
                   curHour +=1;
                   curMin = 0;
               } 
               else
               {
                   curMin += 30;
               }
            }
            
            System.out.println("Enter Doctor Id> ");
            String in = scanner.next().trim();
            Long doctorId = Long.valueOf(in);
            
            DoctorEntity chosenDoctor = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(doctorId);
           
            if (!doctorList.contains(chosenDoctor))
            {
                System.out.println("The selected doctor is not available for the session indicated.");
                return;
            }
 
            String time = "";
            LocalDateTime fin = null; //placeholder
            for (int i=0 ;i<6 ;i++)
            { //select earliest appt
               LocalDateTime cur = LocalDateTime.of(LocalDate.now(), LocalTime.of(curHour, curMin));
               String earliestTime = appointmentEntitySessionBeanRemote.hasAppointment(chosenDoctor, cur);
              
               if(earliestTime.equals("O")){
                   time = curHour+":"+curMin;
                   fin = LocalDateTime.of(LocalDate.now(), LocalTime.of(curHour, curMin));
                   break;
               }
            }
           
            AppointmentEntity ae = new AppointmentEntity(currentPatientEntity.getPatientId(), doctorId, fin);
            ae.setIsConfirmed(true);
            System.out.println(currentPatientEntity.getFirstName() + " " + currentPatientEntity.getLastName() + " with Dr." + chosenDoctor.getFullName() + " has been booked at " + time);
            System.out.printf("Queue Number is %d. \n", queueNumber);
            
            
            
        } catch (LeaveNotFoundException | DoctorNotFoundException ex) 
        {
            System.out.println("Error in walk-in registration"+ ex.getMessage()+"\n");
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
         
         System.out.println("Enter Patient Identity Number> ");
         String patientIn = scanner.nextLine().trim();
         List<String> patientAppointments  = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(patientIn);
         
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
                 
         try {
            System.out.println("Enter Appointment Id> ");
            Integer appointmentId1 = Integer.parseInt(scanner.nextLine().trim());
            Long appointmentId = Long.valueOf(appointmentId1);
            PatientEntity pe = patientEntitySessionBeanRemote.retrievePatientByPatientIdentityNumber(patientIn);
            Long patientId = pe.getPatientId();

            appointmentEntitySessionBeanRemote.confirmAppointment(patientId, appointmentId);
            //Throw error if the appointment is not today?
            //Parse doctorId,
            //System.out.printf(%s%s appointment is confirmed with Dr. %s%s at %tH:%tM, patientFirstName, patientLastName, doctorFirstName, doctorLastName, appointmentTIme, appointmentTIme);
         } 
         catch (PatientNotFoundException | AppointmentNotFoundException ex) 
         {
             System.out.println("An error has occured while registering the appointment: " + ex.getMessage());
         }
         
    }
}
