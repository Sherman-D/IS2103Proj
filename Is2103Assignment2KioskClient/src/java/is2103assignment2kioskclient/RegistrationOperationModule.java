package is2103assignment2kioskclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.LeaveEntity;
import entity.PatientEntity;
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
    
    //This will register a patient at the first available slot that the doctor has in the day. 
//    protected void registerWalkInConsult(PatientEntity currentPatientEntity)
//    {
//        System.out.println("*** Self-Service Kiosk :: Register Walk-In Consultation ***");
//        Scanner scanner = new Scanner(System.in);
//        
//        System.out.println("Doctor: ");
//        System.out.println("Id  |  Name ");
//        //Fetch list of doctors for the current day
//        
//        System.out.println("Availability: ");
//        //Fetch timesheet for doctors today
//        
//        System.out.println("Enter Doctor Id> ");
//        Integer doctorId = Integer.parseInt(scanner.nextLine().trim());
//        //Fetch doctor entity based on ID
//        //System.out.printf(%s%s appointment with Dr. %s%s has been booked at %tH:%tM, patientFirstName, patientLastName, doctorFirstName, doctorLastName, appointmentTIme, appointmentTIme);  
//       
//    }
    
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
