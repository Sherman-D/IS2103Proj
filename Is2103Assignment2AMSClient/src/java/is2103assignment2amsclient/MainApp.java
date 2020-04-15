/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is2103assignment2amsclient;

import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.*;
import java.util.List;
import java.util.Scanner;
import util.exception.AppointmentAlreadyCancelledException;
import util.exception.AppointmentNotFoundException;
import util.exception.DoctorNotFoundException;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;

public class MainApp {

    private PatientEntity currentPatient;

    public MainApp() {

    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to AMS Client ***\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doRegister();
                } else if (response == 2) {
                    doLogin();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 2) {
                break;
            }
        }
    }

    private void doRegister() {
        Scanner scanner = new Scanner(System.in);

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
        while (isValid) {
            System.out.println("Enter Age> ");
            String ageInput = scanner.nextLine().trim();

            try {
                age = Integer.parseInt(ageInput);
                isValid = false;
            } catch (NumberFormatException ex) {
                System.out.println("Please enter your age in digits (1-99)!");

            }
        }

        System.out.println("Enter Phone> ");
        String phone = scanner.nextLine().trim();
        System.out.println("Enter Address> ");
        String address = scanner.nextLine();

        try {
            //String passwordHash = patientEntitySessionBeanRemote.hashPassword(password);
            PatientEntity patient  = createNewPatient(identityNumber, password, firstName, lastName, gender, age, phone, address);
            currentPatient = patient;
            menuMain();
        } catch (EntityInstanceExistsInCollectionException /*| NoSuchAlgorithmException*/ ex) {
            System.out.println("An error has occurred while creating your account: " + ex.getMessage() + "\n");
        }
    }

    private void doLogin() {
        Scanner scanner = new Scanner(System.in);
        String iN = "";
        String password = "";

        System.out.println("*** AMS Client :: Login ***\n");

        System.out.print("Enter Identity Number> ");
        iN = scanner.nextLine().trim();
        System.out.print("Enter Password> ");
        password = scanner.nextLine().trim();

        if (iN.length() > 0 && password.length() > 0) {
            PatientEntity patient = patientLogin(iN, password);
            currentPatient = patient;
            menuMain();
        } else {
            System.out.println("Missing login credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;


        while (true) {
            System.out.println("*** AMS Client :: Main ***\n");

            System.out.printf("You are login as %s %s", currentPatient.getFirstName(), currentPatient.getLastName());

            System.out.println("1: View Appointments");
            System.out.println("2: Add Appointment");
            System.out.println("3: Cancel Appointment");
            System.out.println("4: Logout \n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doViewAppointments();

                } else if (response == 2) {

                    doAddAppointment();

                } else if (response == 3) {
                    doCancelAppointment();

                } else if (response == 4) {
                    currentPatient = new PatientEntity("", "", "", "", "", 0, "", "");
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                currentPatient = new PatientEntity("", "", "", "", "", 0, "", "");
                break;
            }
        }
    }

    private void doViewAppointments() {

//            Scanner scanner = new Scanner(System.in);
        System.out.println("*** AMS Client :: View Appointments ***\n");

        System.out.println("Appointments:");

        List<String> appts = retrieveAppointmentByPatientIdentityNo(currentPatient.getIdentityNumber());
        for (String appointment : appts) {
            System.out.println(appointment);
        }
    }

    private void doAddAppointment() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** AMS Client :: Add Appointment ***\n");

        System.out.println("Doctor:");
        System.out.println("Id |Name");
        
        LocalDate now = LocalDate.now();
        try {
            List<String> deList = retrieveDoctorsAvailableOnDate(now.toString());
            for (String doctor : deList) 
            { //print list of doctors
                System.out.println(doctor);
            }
            System.out.println();

            System.out.println("Enter Doctor Id> ");
            Long doctorId = Long.valueOf(scanner.nextLine().trim());

            System.out.println("Enter Date> ");
            String d = scanner.nextLine().trim();
            LocalDate date = LocalDate.parse(d);
            LocalDate twoDays = LocalDate.now().plusDays(2);

           if (date.isBefore(twoDays))
           {
               System.out.println("Error: Appointments cannot be made less than two days to the consultation date.");
               return;
            }
        
            
            DoctorEntity de = retrieveDoctorByDoctorId(doctorId);
            System.out.println("Availability for " + de.getFullName() + " on " + d + ":");
            List<String> availableSlots = retrieveDoctorAvailableSlotsOnDay(de.getDoctorId(), date.toString());
            
            if (availableSlots.isEmpty())
            {
                System.out.println("This doctor is has no open consultation sessions on the selected date.");
                return;
            }
            String slots = "";
            for (String time : availableSlots) 
            {
                slots = slots.concat(time + " ");
            }
            System.out.println(slots);
           

            LocalTime time = LocalTime.MIDNIGHT;
            
           while (true) 
            {
                System.out.println("Enter Time> ");
                String t = scanner.nextLine().trim();
                time = LocalTime.parse(t);
                
                if (!availableSlots.contains(time)) {
                    System.out.println("That timing is not available. Please choose another time slot");
                    continue;
            }
        
            createNewAppointment(currentPatient.getPatientId(), doctorId, date.atTime(time).toString());
        
            System.out.printf("%s%s appointment with Dr. %s at %d:%d on %d-%d-%d has been added.", currentPatient.getFirstName(), currentPatient.getLastName(), de.getFullName(), time.getHour(), time.getMinute(), date.getYear(), date.getMonth(), date.getDayOfMonth());
            }
        
        } catch (DoctorNotFoundException_Exception ex) {
            System.out.println("No Doctors Found: " + ex.getMessage());
        }
    }

    private void doCancelAppointment() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** AMS Client :: Cancel Appointment ***\n");
        System.out.println("Appointments:");
        System.out.printf("%s-1|%s-7|%s-3|%s", "Id", "Date", "Time", "Doctor");
        List<String> appts = retrieveAppointmentByPatientIdentityNo(currentPatient.getIdentityNumber());
       
        for (String s : appts) {
            System.out.println(s);
        }
        System.out.println();

        System.out.println("Enter Appointment Id> ");
        String in = scanner.nextLine().trim();
        Long appointmentId = Long.valueOf(in);

        try {
            
            cancelAppointment(appointmentId);
            //Send cancel status to database entry. 
           System.out.printf("%s%s appointment with Dr. %s at %d:%d on %d-%d-%d has been added", currentPatient.getFirstName(), currentPatient.getLastName(), de.getFullName(), appointmentTime.getHour(), appointmentTime.getMinute(), appointmentTime.getYear(), appointmentTime.getMonth(), appointmentTime.getDayOfMonth());
        } catch (AppointmentNotFoundException | AppointmentAlreadyCancelledException | DoctorNotFoundException | DoctorNotFoundException ex) {
            System.out.println("Error cancelling appointment: " + ex.getMessage());
        }
    }

    private static ws.client.ams.PatientEntity createNewPatient(java.lang.String identityNumber, java.lang.String password, java.lang.String firstName, java.lang.String lastName, java.lang.String gender, java.lang.Integer age, java.lang.String phone, java.lang.String address) throws EntityInstanceExistsInCollectionException_Exception
    {

        ws.client.ams.XXXXXX_Service service = new ws.client.ams.XXXXX_Service();
        ws.client.ams.XXXXXX port = service.getXXXXXPort();

        return port.createNewPatient(identityNumber, password, firstName, lastName, gender, age, phone, address);
    }
    
    private static ws.client.ams.PatientEntity patientLogin(java.lang.String identityNumber, java.lang.String password) throws InvalidLoginCredentialException_Exception
    {
        ws.client.ams.XXXXXX_Service service = new ws.client.ams.XXXXX_Service();
        ws.client.ams.XXXXXX port = service.getXXXXXPort();
        
        return port.createNewPatient(identityNumber, password);
    }
    
    
    private static java.util.List<java.lang.String> retrieveAppointmentByPatientIdentityNo(java.lang.String identityNumber)
    {
        ws.client.ams.XXXXXX_Service service = new ws.client.ams.XXXXX_Service();
        ws.client.ams.XXXXXX port = service.getXXXXXPort();
        
        return port.retrieveAppointmentByPatientIdentityNo(identityNumber);
    }
    
   private static  java.util.List<java.lang.String> retrieveDoctorsAvailableOnDate(java.lang.String now) 
   {
        ws.client.ams.XXXXXX_Service service = new ws.client.ams.XXXXX_Service();
        ws.client.ams.XXXXXX port = service.getXXXXXPort();
        
        return port.retrieveDoctorsAvailableOnDate(now);
   }
   
   private static ws.client.ams.DoctorEntity retrieveDoctorByDoctorId(java.lang.Long doctorId) throws DoctorNotFoundException_Exception
   {
       ws.client.ams.XXXXXX_Service service = new ws.client.ams.XXXXX_Service();
       ws.client.ams.XXXXXX port = service.getXXXXXPort();
        
        return port.retrieveDoctorByDoctorId(doctorId);
   }
   
   
   private static java.util.List<java.lang.String> retrieveDoctorAvailableSlotsOnDay(java.lang.Long doctorId, java.lang.String date)
   {
       ws.client.ams.XXXXXX_Service service = new ws.client.ams.XXXXX_Service();
       ws.client.ams.XXXXXX port = service.getXXXXXPort();
        
        return port.retrieveDoctorByDoctorId(doctorId, date);
   }
   
   
   private static void createNewAppointment(java.lang.Long patientId, java.lang.Long doctorId, java.lang.String dateTime) 
   {
       ws.client.ams.XXXXXX_Service service = new ws.client.ams.XXXXX_Service();
       ws.client.ams.XXXXXX port = service.getXXXXXPort();
        
       port.createNewAppointment(patientId, doctorId, dateTime);
   }
   
   private static void cancelAppointment(java.lang.Long appointmentId) throws AppointmentNotFoundException_Exception, AppointmentAlreadyCancelledException_Exception, DoctorNotFoundException_Exception
   {
       ws.client.ams.XXXXXX_Service service = new ws.client.ams.XXXXX_Service();
       ws.client.ams.XXXXXX port = service.getXXXXXPort();
        
       port.createNewAppointment(appointmentId);
   }
}
