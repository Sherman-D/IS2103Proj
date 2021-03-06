/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is2103assignment2amsclient;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import ws.client.ams.AppointmentAlreadyCancelledException_Exception;
import ws.client.ams.AppointmentEntity;
import ws.client.ams.AppointmentNotFoundException_Exception;
import ws.client.ams.DoctorEntity;
import ws.client.ams.DoctorNotFoundException_Exception;
import ws.client.ams.EntityInstanceExistsInCollectionException_Exception;
import ws.client.ams.EntityMismatchException_Exception;
import ws.client.ams.InvalidLoginCredentialException_Exception;
import ws.client.ams.NoSuchAlgorithmException_Exception;
import ws.client.ams.PatientEntity;
import ws.client.ams.PatientNotFoundException_Exception;

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

        boolean isValid = true;
        Integer age = 0;
        while (isValid) {
            System.out.print("Enter Age> ");
            String ageInput = scanner.nextLine().trim();

            try {
                age = Integer.parseInt(ageInput);
                isValid = false;
            } catch (NumberFormatException ex) {
                System.out.println("Please enter your age in digits (1-99)!");

            }
        }

        System.out.print("Enter Phone> ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter Address> ");
        String address = scanner.nextLine();

        try {
            //String passwordHash = patientEntitySessionBeanRemote.hashPassword(password);
            PatientEntity patient  = createNewPatient(identityNumber, password, firstName, lastName, gender, age, phone, address);
            currentPatient = patient;
            menuMain();
        } catch (EntityInstanceExistsInCollectionException_Exception | NoSuchAlgorithmException_Exception ex) {
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

        try {
        if (iN.length() > 0 && password.length() > 0) {
            PatientEntity patient = patientLogin(iN, password);
            currentPatient = patient;
            menuMain();
        } else {
            System.out.println("Missing login credential!");
        }
        } catch (InvalidLoginCredentialException_Exception | NoSuchAlgorithmException_Exception ex) {
            System.out.println("Incorrect login credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;


        while (true) {
            System.out.println("*** AMS Client :: Main ***\n");

            System.out.printf("You are login as %s %s \n", currentPatient.getFirstName(), currentPatient.getLastName());

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
                    currentPatient = new PatientEntity();
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                currentPatient = new PatientEntity();
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
        System.out.println();
    }

    private void doAddAppointment() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** AMS Client :: Add Appointment ***\n");

        System.out.println("Doctor:");
        System.out.println("Id |Name");
        
        LocalDate now = LocalDate.now();
        try {
            List<DoctorEntity> deList = (List<DoctorEntity>) retrieveDoctorsAvailableOnDate(now.toString());
            for (DoctorEntity doctor : deList) 
            { //print list of doctors
                System.out.println(doctor.getDoctorId() + " | " + doctor.getFirstName() + " " + doctor.getLastName());
            }
            System.out.println();

            System.out.print("Enter Doctor Id> ");
            Long doctorId = Long.valueOf(scanner.nextLine().trim());

            System.out.print("Enter Date> ");
            String d = scanner.nextLine().trim();
            LocalDate date = LocalDate.parse(d);
            LocalDate twoDays = LocalDate.now().plusDays(2);

           if (date.isBefore(twoDays))
           {
               System.out.println("Error: Appointments cannot be made less than two days to the consultation date.");
               return;
            }
        
            
            DoctorEntity de = retrieveDoctorByDoctorId(doctorId);
            System.out.println("Availability for " + de.getFirstName() + " " +  de.getLastName() + " on " + d + ":");
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
                System.out.print("Enter Time> ");
                String selectedTime = scanner.nextLine().trim();
                
                if (selectedTime.equals("cancel")) {
                    return;
                }
                
                if (!availableSlots.contains(selectedTime)) {
                    System.out.println("That timing is not available. Please choose another time slot. Otherwise, enter \" cancel \" to cancel.");
                    continue;
                }
                time = LocalTime.parse(selectedTime);
                break;
            }
        
            createNewAppointment(currentPatient.getPatientId(), doctorId, date.atTime(time).toString());
        
            System.out.printf("%s %s appointment with Dr. %s %s at %01d:%01d on %d-%01d-%01d has been added. \n", currentPatient.getFirstName(), currentPatient.getLastName(), de.getFirstName(), de.getLastName(), time.getHour(), time.getMinute(), date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            
        
        } catch (DoctorNotFoundException_Exception | PatientNotFoundException_Exception ex) {
            System.out.println("Error in creating appointment: " + ex.getMessage());
        }
    }

    private void doCancelAppointment() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** AMS Client :: Cancel Appointment ***\n");
        System.out.println("Appointments:");
        System.out.printf("%-1s|%-7s|%-3s|%s\n", "Id", "Date", "Time", "Doctor");
        List<String> appts = retrieveAppointmentByPatientIdentityNo(currentPatient.getIdentityNumber());
       
        if (appts.isEmpty())
            {
                System.out.println("No appointments with the associated patient were found.");
                return;
            }
        
        for (String s : appts) {
            System.out.println(s);
        }
        System.out.println();

        System.out.print("Enter Appointment Id> ");
        String in = scanner.nextLine().trim();
        Long appointmentId = Long.valueOf(in);

        try {
            AppointmentEntity appointment = cancelAppointment(appointmentId);
            DoctorEntity de = appointment.getDoctor();
            LocalDateTime appointmentTime = LocalDateTime.parse(appointment.getDateTime());
           System.out.printf("%s %s appointment with Dr. %s %s at %01d:%01d on %d-%01d-%01d has been cancelled. \n", currentPatient.getFirstName(), currentPatient.getLastName(), de.getFirstName(), de.getLastName(), appointmentTime.getHour(), appointmentTime.getMinute(), appointmentTime.getYear(), appointmentTime.getMonthValue(), appointmentTime.getDayOfMonth());
        } catch (AppointmentNotFoundException_Exception | AppointmentAlreadyCancelledException_Exception |  DoctorNotFoundException_Exception | EntityMismatchException_Exception ex) {
            System.out.println("Error cancelling appointment: " + ex.getMessage());
        }
    }

    private static ws.client.ams.PatientEntity createNewPatient(java.lang.String identityNumber, java.lang.String password, java.lang.String firstName, java.lang.String lastName, java.lang.String gender, java.lang.Integer age, java.lang.String phone, java.lang.String address) 
                throws EntityInstanceExistsInCollectionException_Exception, NoSuchAlgorithmException_Exception
    {

        ws.client.ams.IS2103Assignment2WebService_Service service = new ws.client.ams.IS2103Assignment2WebService_Service();
        ws.client.ams.IS2103Assignment2WebService port = service.getIS2103Assignment2WebServicePort();

        return port.createNewPatient(identityNumber, password, firstName, lastName, gender, age, phone, address);
    }
    
    private static ws.client.ams.PatientEntity patientLogin(java.lang.String identityNumber, java.lang.String password) 
                throws InvalidLoginCredentialException_Exception, NoSuchAlgorithmException_Exception
    {
        ws.client.ams.IS2103Assignment2WebService_Service service = new ws.client.ams.IS2103Assignment2WebService_Service();
        ws.client.ams.IS2103Assignment2WebService port = service.getIS2103Assignment2WebServicePort();
        
        return port.patientLogin(identityNumber, password);
    }
    
    
    private static java.util.List<java.lang.String> retrieveAppointmentByPatientIdentityNo(java.lang.String identityNumber)
    {
        ws.client.ams.IS2103Assignment2WebService_Service service = new ws.client.ams.IS2103Assignment2WebService_Service();
        ws.client.ams.IS2103Assignment2WebService port = service.getIS2103Assignment2WebServicePort();
        
        return port.retrieveAppointmentByPatientIdentityNo(identityNumber);
    }
    
   private static  java.util.List<ws.client.ams.DoctorEntity> retrieveDoctorsAvailableOnDate(java.lang.String now) 
           throws DoctorNotFoundException_Exception
   {
        ws.client.ams.IS2103Assignment2WebService_Service service = new ws.client.ams.IS2103Assignment2WebService_Service();
        ws.client.ams.IS2103Assignment2WebService port = service.getIS2103Assignment2WebServicePort();
        
        return port.retrieveDoctorsAvailableOnDate(now);
   }
   
   private static ws.client.ams.DoctorEntity retrieveDoctorByDoctorId(java.lang.Long doctorId) throws DoctorNotFoundException_Exception
   {
       ws.client.ams.IS2103Assignment2WebService_Service service = new ws.client.ams.IS2103Assignment2WebService_Service();
        ws.client.ams.IS2103Assignment2WebService port = service.getIS2103Assignment2WebServicePort();
        
        return port.retrieveDoctorByDoctorId(doctorId);
   }
   
   
   private static java.util.List<java.lang.String> retrieveDoctorAvailableSlotsOnDay(java.lang.Long doctorId, java.lang.String date)
   {
       ws.client.ams.IS2103Assignment2WebService_Service service = new ws.client.ams.IS2103Assignment2WebService_Service();
        ws.client.ams.IS2103Assignment2WebService port = service.getIS2103Assignment2WebServicePort();
        
        return port.retrieveDoctorAvailableSlotsOnDay(doctorId, date);
   }
   
   
   private static void createNewAppointment(java.lang.Long patientId, java.lang.Long doctorId, java.lang.String dateTime)
           throws DoctorNotFoundException_Exception, PatientNotFoundException_Exception
   {
       ws.client.ams.IS2103Assignment2WebService_Service service = new ws.client.ams.IS2103Assignment2WebService_Service();
        ws.client.ams.IS2103Assignment2WebService port = service.getIS2103Assignment2WebServicePort();
        
       port.createNewAppointment(patientId, doctorId, dateTime);
   }
   
   private static ws.client.ams.AppointmentEntity cancelAppointment(java.lang.Long appointmentId) 
           throws AppointmentNotFoundException_Exception, AppointmentAlreadyCancelledException_Exception, DoctorNotFoundException_Exception, EntityMismatchException_Exception
   {
       ws.client.ams.IS2103Assignment2WebService_Service service = new ws.client.ams.IS2103Assignment2WebService_Service();
        ws.client.ams.IS2103Assignment2WebService port = service.getIS2103Assignment2WebServicePort();
        
       return port.cancelAppointment(appointmentId);
   }
}
