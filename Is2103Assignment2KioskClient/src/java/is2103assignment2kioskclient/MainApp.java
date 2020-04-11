package is2103assignment2kioskclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.LeaveEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import ejb.session.stateless.StaffEntitySessionBeanRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.LeaveEntity;
import entity.PatientEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.AppointmentAlreadyCancelledException;
import util.exception.AppointmentNotFoundException;
import util.exception.DoctorNotFoundException;
import util.exception.EntityManagerException;
import util.exception.InvalidLoginCredentialException;
import util.exception.LeaveNotFoundException;

public class MainApp {
    
    
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private StaffEntitySessionBeanRemote staffEntitySessionBeanRemote;
    private LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote;
    
    private PatientEntity currentPatientEntity;
    
     public MainApp(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote,  PatientEntitySessionBeanRemote patientEntitySessionBeanRemote, StaffEntitySessionBeanRemote staffEntitySessionBeanRemote) 
    {        
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
        this.staffEntitySessionBeanRemote = staffEntitySessionBeanRemote;
    }
    
    public void runApp(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Self Service Kiosk ***\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                        doRegister();
                        System.out.println("Register successful!\n");
                }
                else if(response == 2)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                                               
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException | EntityManagerException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
    
    
        private void doLogin() throws InvalidLoginCredentialException,  EntityManagerException
    {
        Scanner scanner = new Scanner(System.in);
        String iN = "";
        String password = "";
        
        System.out.println("*** Self-Service Kiosk :: Login ***\n");
        System.out.print("Enter Identity Number> ");
        iN = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(iN.length() > 0 && password.length() > 0)
        {
            currentPatientEntity = patientEntitySessionBeanRemote.patientLogin(iN, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
        private void menuMain() 
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Self-Service Kiosk :: Main ***\n");
            
            System.out.printf("You are login as %s %s", currentPatientEntity.getFirstName(), currentPatientEntity.getLastName());
      
            System.out.println("1: Registration Walk-In Consultation");
            System.out.println("2: Register Consultation By Appointment");
            System.out.println("3: View Appointments");
            System.out.println("4: Add Appointment");
            System.out.println("5: Cancel Appointment");
            System.out.println("6: Logout \n");
            response = 0;
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doRegisterWalkIn();
                }
                else if(response == 2)
                {
                    doRegisterAppointment();                   
                }
                else if (response == 3)
                {
                    doViewAppointment();
                }
                else if (response == 4)
                {
                    doAddAppointment();
                }
                else if (response == 5)
                {
                    doCancelAppointment();
                }
                else if (response == 6)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 6)
            {
                break;
            }
        }
    }
        
        private void doRegisterWalkIn() throws AppointmentNotFoundException, DoctorNotFoundException{
            
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** Self-Service Kiosk :: Register Walk-In Consultation ***\n");

            System.out.println("Doctor:");
            System.out.println("Id |Name");
            LocalDateTime now = LocalDateTime.now();
        try {
            List<DoctorEntity> doctorList = doctorEntitySessionBeanRemote.retrieveAllDoctors();
            List<LeaveEntity> leaveToday = leaveEntitySessionBeanRemote.retrieveLeaveByDate(now);
            List<DoctorEntity> present = new ArrayList<DoctorEntity>();
            String availString ="";
            
            //print list of doctors not on leave
            for(DoctorEntity de : doctorList){
                DoctorEntity present = de;
                for(LeaveEntity le : leaveToday){
                    if(le.getDoctorId().equals(de.getDoctorId())){
                        present = null;
                    }
                }
                if(present != null){
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
            if(curMin>30){
                curHour++;
            }else if(curMin>0){
                curMin = 30; //earliest appt
            }else if (curMin==0){
                curMin = 0;
            }
            
            
            for(int i=0;i<6;i++){ //printing X and O
               String out = curHour+":"+curMin+" |";
               LocalDateTime cur = LocalDateTime.of(LocalDate.now(), LocalTime.of(curHour, curMin));
               for(DoctorEntity de : doctorList){
                   out += appointmentEntitySessionBeanRemote.hasAppointment(de, cur)+ " |";
               }
               System.out.println(out);
               if(curMin==30){
                   curHour +=1;
                   curMin = 0;
               }else{
                   curMin += 30;
               }
            }
            
            System.out.println("Enter Doctor Id> ");
            String in = scanner.next().trim();
            Long doctorId = Long.valueOf(in);
            DoctorEntity chosenDoctor = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(doctorId);
            if(!doctorList.contains(chosenDoctor)){
                throw new AppointmentNotFoundException();
            }
 
            String time = "";
            LocalDateTime fin = null; //placeholder
            for(int i=0;i<6;i++){ //select earliest appt
               LocalDateTime cur = LocalDateTime.of(LocalDate.now(), LocalTime.of(curHour, curMin));
               String earliestTime = appointmentEntitySessionBeanRemote.hasAppointment(chosenDoctor, cur);
               if(earliestTime.equals("O")){
                   time = curHour+":"+curMin;
                   fin = LocalDateTime.of(LocalDate.now(), LocalTime.of(curHour, curMin));
               }
            }
            AppointmentEntity ae = new AppointmentEntity(currentPatientEntity.getPatientId(), doctorId, fin);
            ae.setIsConfirmed(true);
            System.out.println(currentPatientEntity.getFirstName()+" "+currentPatientEntity.getLastName()+" with Dr."+chosenDoctor.getFullName()+" has been booked at "+ time);
            System.out.println("Queue Number is: "+/*qNum*/+".");
            
            
            
        } catch (LeaveNotFoundException ex) 
        {
            System.out.println("Error in walk-in registration"+ ex.getMessage()+"\n");
        }
        } 
        
        
        private void doRegisterAppointment(){
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** Self-Service Kiosk :: Register Consultation By Appointment ***\n");

            System.out.println("Appointments:");
            System.out.println("Id |Date       |Time  |Doctor");
            try
            {
            List<String> ls = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(currentPatientEntity.getIdentityNumber());
            for(String each : ls){
                System.out.println(each);
            }
            
            System.out.println("Enter Appointment Id> ");
            String appointmentId1 = scanner.nextLine().trim();
            Long appointmentId = Long.valueOf(appointmentId1);
            AppointmentEntity ae = appointmentEntitySessionBeanRemote.retrieveAppointmentByAppointmentId(appointmentId);
            ae.setIsConfirmed(true);
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(ae.getDoctorId());
            System.out.println(currentPatientEntity.getFirstName()+" "+currentPatientEntity.getLastName()+" appointment is confirmed with Dr. "+de.getFullName()+" at "+ae.getAppointmentTime());
            System.out.println("Queue Number is"+/*num*/+".");
            }
            catch(AppointmentNotFoundException ex)
            {
                System.out.println("Error registering appointment "+ex.getMessage());
            }
            
        }
        
        private void doViewAppointment(){
            System.out.println("*** Self-Service Kiosk :: View Appointments ***\n");
            System.out.println("Appointments:");
            List<String> ls = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(currentPatientEntity.getIdentityNumber());
            for(String each: ls){
                System.out.println(each);
            }
        }
        
        private void doAddAppointment(){
            System.out.println("*** Self-Service Kiosk :: Add Appointment ***\n");
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("Id |Name");
            List<DoctorEntity> ls = doctorEntitySessionBeanRemote.retrieveAllDoctors();
            for(DoctorEntity de: ls){ //print list of doctors
                System.out.println(de.getDoctorId()+" |"+de.getFullName());
            }
            
            System.out.println("");
            System.out.println("Enter Doctor Id> ");
            String dId = scanner.nextLine().trim();
            Long doctorId = Long.valueOf(dId);
            System.out.println("Enter Date> ");
            String date = scanner.nextLine().trim();
            
            try
            {
                DoctorEntity chosenDoctor = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(doctorId);
                System.out.println("Availability for "+ chosenDoctor.getFullName()+" on "+date);
                //help here
                
                System.out.println("");
                System.out.println("Enter Time>");
                String time = scanner.nextLine().trim();
//                ArrayList<Integer> arr = time.split(":"); // arr[0] is hr and arr[1] is min
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(date+" "+time, formatter);
                appointmentEntitySessionBeanRemote.createNewAppointment(new AppointmentEntity(currentPatientEntity.getPatientId(), doctorId, dateTime ));
                System.out.println(currentPatientEntity.getFirstName()+" "+currentPatientEntity.getLastName()+" appointment with "+chosenDoctor.getFullName()+" at "+time+" on "+date+" has been added.");    
            }
                catch(DoctorNotFoundException ex)
            {
                System.out.println("Error adding appointment "+ex.getMessage());
            }
        }
        
        private void doCancelAppointment(){
            System.out.println("*** Self-Service Kiosk :: Cancel Appointment ***\n");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Appointments:");
            System.out.println("Id |Date       |Time  |Doctor ");
            List<String> appts = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(currentPatientEntity.getIdentityNumber());
            for(String s : appts){
                System.out.println(s);
            }
            System.out.println("");
            System.out.println("Enter Appointment Id> ");
            String apptId = scanner.nextLine().trim();
            Long appointmentId = Long.valueOf(apptId);
            try
            {
            AppointmentEntity ae = appointmentEntitySessionBeanRemote.retrieveAppointmentByAppointmentId(appointmentId);
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(ae.getDoctorId());
            LocalDate d = ae.getAppointmentTime().toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-LL-dd");
            String date = d.format(formatter);
            LocalTime t = ae.getAppointmentTime().toLocalTime();
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
            String time = t.format(formatter1);
            ae.cancelAppointment();
            System.out.println(currentPatientEntity.getFirstName()+" "+currentPatientEntity.getLastName()+" with "+de.getFullName()+" at"+time+" on "+date+" has been cancelled.");
            }
            catch(AppointmentNotFoundException ex)
            {
                System.out.println("Error cancelling appointment: " +ex.getMessage());
            }
            catch(DoctorNotFoundException ex)
            {
                System.out.println("Error cancelling appointment: " +ex.getMessage());
            }
            
        }
    
}
