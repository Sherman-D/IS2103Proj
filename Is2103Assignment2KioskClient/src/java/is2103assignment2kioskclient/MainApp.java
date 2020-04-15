package is2103assignment2kioskclient;

import ejb.session.singleton.QueueGeneratorSessionBeanRemote;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.exception.AppointmentNotFoundException;
import util.exception.DoctorNotFoundException;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityManagerException;
import util.exception.InvalidLoginCredentialException;

public class MainApp {
    
    
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private QueueGeneratorSessionBeanRemote queueGeneratorSessionBeanRemote;
    private LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote;
    
    private PatientEntity currentPatientEntity;

    public MainApp(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote, PatientEntitySessionBeanRemote patientEntitySessionBeanRemote, QueueGeneratorSessionBeanRemote queueGeneratorSessionBeanRemote, LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.doctorEntitySessionBeanRemote = doctorEntitySessionBeanRemote;
        this.patientEntitySessionBeanRemote = patientEntitySessionBeanRemote;
        this.queueGeneratorSessionBeanRemote = queueGeneratorSessionBeanRemote;
        this.leaveEntitySessionBeanRemote = leaveEntitySessionBeanRemote;
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
                        menuMain();
                }
                else if(response == 2)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                                               
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException | EntityManagerException | NoSuchAlgorithmException ex) 
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
   
    private void doRegister()
    {
        Scanner scanner = new Scanner(System.in);
        PatientEntity pe = new PatientEntity();

        System.out.println("*** Self-Service Kiosk :: Register ***\n");

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
        
        boolean isValid = true;
        Integer age = 0;
        while (isValid)
        {
            System.out.println("Enter Age> ");
            String ageInput = scanner.nextLine().trim();
            
            try {
               age = Integer.parseInt(ageInput);
               isValid = false;
            } 
            catch (NumberFormatException ex)
            {
                System.out.println("Please enter your age in digits (1-99)!");
                
            }
        }
        
        System.out.println("Enter Phone> ");
        String phone = scanner.nextLine().trim();
        System.out.println("Enter Address> ");
        String address = scanner.nextLine();

        try
        {
            String passwordHash = patientEntitySessionBeanRemote.hashPassword(password);
            patientEntitySessionBeanRemote.createNewPatient(new PatientEntity(identityNumber, passwordHash, firstName, lastName, gender, age, phone, address));
            System.out.println("Patient has been registered successfully!");
            
        }
        catch (EntityInstanceExistsInCollectionException ex)
        {
            System.out.println("An error has occurred while creating your account: " + ex.getMessage() + "\n");
        }
        catch(NoSuchAlgorithmException ex)
        {
            System.out.println("An error has occurred while creating your account: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doLogin() throws InvalidLoginCredentialException, EntityManagerException, NoSuchAlgorithmException
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
        
        private void doRegisterWalkIn()
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** Self-Service Kiosk :: Register Walk-In Consultation ***\n");

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
            int queueNumber = queueGeneratorSessionBeanRemote.getNextQueueNumber();
            System.out.println(currentPatientEntity.getFirstName() + " " + currentPatientEntity.getLastName() + " with Dr." + chosenDoctor.getFullName() + " has been booked at " + time);
            System.out.printf("Queue Number is %d. \n", queueNumber);
            
            
            
        } catch (DoctorNotFoundException ex) 
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
            int queueNumber = queueGeneratorSessionBeanRemote.getNextQueueNumber();
            
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(ae.getDoctorId());
            System.out.println(currentPatientEntity.getFirstName()+" "+currentPatientEntity.getLastName()+" appointment is confirmed with Dr. "+de.getFullName()+" at "+ae.getAppointmentTime());
            System.out.printf("Queue Number is %d. \n", queueNumber);
            
            }
            catch(AppointmentNotFoundException | DoctorNotFoundException ex)
            {
                System.out.println("Error registering appointment "+ex.getMessage());
            }
            
        }
        
        private void doViewAppointment(){
            System.out.println("*** Self-Service Kiosk :: View Appointments ***\n");
            System.out.println("Appointments:");
            System.out.printf("%s-1|%s-7|%s-3|%s", "Id", "Date", "Time", "Doctor");
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
            String d = scanner.nextLine().trim();
            LocalDate date = LocalDate.parse(d);
            
            try
            {
                LocalDate now = LocalDate.now();
                LocalDate twoDays = now.plusDays(2);
                if(date.isBefore(twoDays)){
                  System.out.println("Error: Appointments cannot be made less than two days to the consultation date.");
                  return;
                }
                DoctorEntity chosenDoctor = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(doctorId);
                System.out.println("Availability for "+ chosenDoctor.getFullName()+" on "+d);
                StringBuilder slots = new StringBuilder();
           
                List<LocalTime> availableSlots = appointmentEntitySessionBeanRemote.retrieveDoctorAvailableSlotsOnDay(chosenDoctor, date);

                if (availableSlots.isEmpty()) {
                    System.out.println("This doctor is has no open consultation sessions on the selected date.");
                    return;
                }

                for (LocalTime time : availableSlots) {
                    slots.append(time.toString());
                    slots.append(" ");
                }
                System.out.println(slots.toString());

                LocalTime time = LocalTime.MIDNIGHT;

                while (true) {
                    System.out.println("Enter Time> ");
                    String t = scanner.nextLine().trim();
                    time = LocalTime.parse(t);

                    if (!availableSlots.contains(time)) {
                        System.out.println("That timing is not available. Please choose another time slot");
                        continue;
                    }
                    break;
                }
                
                appointmentEntitySessionBeanRemote.createNewAppointment(new AppointmentEntity(currentPatientEntity.getPatientId(), doctorId, date.atTime(time) ));
               
                System.out.println(currentPatientEntity.getFirstName()+" "+currentPatientEntity.getLastName()+" appointment with "+chosenDoctor.getFullName()+" at "+ time +" on "+d+" has been added.");    
            }
                catch(DoctorNotFoundException ex)
            {
                System.out.println("Error adding appointment: "+ex.getMessage());
            }
        }
        
        private void doCancelAppointment(){
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** Self-Service Kiosk :: Cancel Appointment ***\n");
            
            System.out.println("Appointments:");
            System.out.printf("%s-1|%s-7|%s-3|%s", "Id", "Date", "Time", "Doctor");
            List<String> appts = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(currentPatientEntity.getIdentityNumber());
            
            for(String s : appts){
                System.out.println(s);
            }
            System.out.println();
            
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
            
            System.out.println(currentPatientEntity.getFirstName() + " "  + currentPatientEntity.getLastName() + " with " + de.getFullName()+" at"+time+" on "+date+" has been cancelled.");
            }
            catch (AppointmentNotFoundException | DoctorNotFoundException ex)
            {
                System.out.println("Error cancelling appointment: " +ex.getMessage());
            }
            
        }
    
}