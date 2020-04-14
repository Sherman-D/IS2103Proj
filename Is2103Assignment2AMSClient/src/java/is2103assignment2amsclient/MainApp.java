/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is2103assignment2amsclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.LeaveEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import ejb.session.stateless.StaffEntitySessionBeanRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.time.LocalDateTime;
import java.time.*;
import java.util.List;
import java.util.Scanner;
import util.exception.AppointmentNotFoundException;
import util.exception.DoctorNotFoundException;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityManagerException;
import util.exception.InvalidLoginCredentialException;

public class MainApp {
    
    private AppointmentOperationModule appointmentOperationModule;
    
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
            System.out.println("*** Welcome to AMS Client ***\n");
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
//                        menuMain();
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
    
    private void doRegister()
    {
        Scanner scanner = new Scanner(System.in);
        PatientEntity pe = new PatientEntity();

        System.out.println("*** Self-Service Kiosk :: Register ***\n");

        System.out.println("Enter Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.println("Enter Password> ");
        String password = scanner.nextLine().trim();
        String passwordHash = patientEntitySessionBeanRemote.hashPassword(password);
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
            patientEntitySessionBeanRemote.createNewPatient(new PatientEntity(identityNumber, passwordHash, firstName, lastName, gender, age, phone, address));
//            System.out.println("Patient has been registered successfully!");
            
        }
        catch (EntityInstanceExistsInCollectionException ex)
        {
            System.out.println("An error has occurred while creating your account: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doLogin() throws InvalidLoginCredentialException,  EntityManagerException
    {
        Scanner scanner = new Scanner(System.in);
        String iN = "";
        String password = "";
        
        System.out.println("*** AMS Client :: Login ***\n");
        
        System.out.print("Enter Identity Number> ");
        iN = scanner.nextLine().trim();
        System.out.print("Enter Password> ");
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
    
        private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** AMS Client :: Main ***\n");
            
            System.out.printf("You are login as %s %s", currentPatientEntity.getFirstName(), currentPatientEntity.getLastName());
      
            System.out.println("1: View Appointments");
            System.out.println("2: Add Appointment");
            System.out.println("3: Cancel Appointment");
            System.out.println("4: Logout \n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doViewAppointments();
//                    appointmentOperationModule.menuRegistrationOperation();
                }
                else if(response == 2)
                {
                    
                    doAddAppointment();
//                    appointmentOperationModule.menuAppointmentOperation();
                    
                }
                else if (response == 3)
                {
                    doCancelAppointment();
//                    appointmentOperationModule.menuRegistrationOperation();
                }
                else if (response == 4)
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

  
        
        private void doViewAppointments(){

//            Scanner scanner = new Scanner(System.in);

            System.out.println("*** AMS Client :: View Appointments ***\n");
           
            System.out.println("Appointments:");
            
            List<String> appts = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(currentPatientEntity.getIdentityNumber());
            for (String appointment : appts)
            {
                System.out.println(appointment);
            }
        }
        
        private void doAddAppointment(){
            Scanner scanner = new Scanner(System.in);

            System.out.println("*** AMS Client :: Add Appointment ***\n");
            
            System.out.println("Doctor:");
            System.out.println("Id |Name");
            LocalDateTime now = LocalDateTime.now();
            List<DoctorEntity> deList = doctorEntitySessionBeanRemote.retrieveDoctorsAvailableOnDate(now);
            
            for(DoctorEntity de : deList){ //print list of doctors
                System.out.println(de.getDoctorId()+" |"+de.getFullName());
            }
            System.out.println();
            
            System.out.println("Enter Doctor Id> ");
            Long doctorId = Long.valueOf(scanner.nextLine().trim());
            
            System.out.println("Enter Date> ");
            String d = scanner.nextLine().trim();
//            DateFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(d);
            
            try
            {
                DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(doctorId);
                System.out.println("Availability for " + de.getFullName() + " on " + d + ":");
                //not sure how to print
                System.out.println("EDIT AMS CLIENT MAINAPP DOADDAPPT");
                System.out.println("");
               
                System.out.println("Enter Time> ");
                String t = scanner.nextLine().trim();
//                TimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime time = LocalTime.parse(t);
                //check if this time is avail
                System.out.println(currentPatientEntity.getFirstName() + " " + currentPatientEntity.getLastName());
            }
            catch(DoctorNotFoundException ex)
            {
                System.out.println("Error in adding appointment: "+ex.getMessage());
            }
        }
        
        private void doCancelAppointment(){
            Scanner scanner = new Scanner(System.in);

            System.out.println("*** AMS Client :: Cancel Appointment ***\n");
            System.out.println("Appointments:");
            System.out.println("Id |Date       |Time  |Doctor");
            List<String> appts = appointmentEntitySessionBeanRemote.retrieveAppointmentByPatientIdentityNo(currentPatientEntity.getIdentityNumber());
            for(String s : appts)
            {
                System.out.println(s);
            }
            System.out.println();
            
            System.out.println("Enter Appointment Id> ");
            String in = scanner.nextLine().trim();
            Long appointmentId = Long.valueOf(in);
         
            try
            {
             AppointmentEntity ae = appointmentEntitySessionBeanRemote.retrieveAppointmentByAppointmentId(appointmentId);
             DoctorEntity d = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorId(ae.getDoctorId());
             System.out.println(currentPatientEntity.getFirstName()+" "+currentPatientEntity.getLastName()+" appointment with " + d.getFullName() + " at "+ t +" on "+ d +" has been cancelled");
             appointmentEntitySessionBeanRemote.cancelAppointment(ae);
            }
            catch(AppointmentNotFoundException | DoctorNotFoundException  ex)
            {
                System.out.println("Error cancelling appointment: "+ ex.getMessage());
            }
        }
}