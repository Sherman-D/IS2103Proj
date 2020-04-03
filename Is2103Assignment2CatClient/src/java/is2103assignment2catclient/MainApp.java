package is2103assignment2catclient;

import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import util.exception.EntityManagerException;

public class MainApp {
    
    private RegistrationOperationModule registrationOperationModule;
    private AppointmentOperationModule appointmentOperationModule;
    private AdministrationOperationModule administrationOperationModule;
    
    public MainApp() 
    {        
    }
    
    public void runApp(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Clinic Appointment Registration System (CARS) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        //Insert registration/administration/appointment initialisation here
                        
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException | EntityManagerException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
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
        String username = "";
        String password = "";
        
        System.out.println("*** CARS :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            //currentStaffEntity = staffEntitySessionBeanRemote.staffLogin(username, password);      
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
            System.out.println("*** CARS :: Main ***\n");
      
            System.out.println("1: Registration Operation");
            System.out.println("2: Appointment Operation");
            System.out.println("3: Adminstration Operation");
            System.out.println("4: Logout \n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    registrationOperationModule.menuRegistrationOperation();
                }
                else if(response == 2)
                {
                    try
                    {
                        appointmentOperationModule.menuAppointmentOperation();
                    }
                    catch (InvalidAccessRightException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 3)
                {
                    administrationOperationModule.menuAdministrationOperation();
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
    
}
