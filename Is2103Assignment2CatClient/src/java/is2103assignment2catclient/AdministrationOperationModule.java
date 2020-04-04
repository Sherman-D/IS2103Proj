package is2103assignment2catclient;

import java.util.Scanner;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;

public class AdministrationOperationModule {
    
    public void menuAdministrationOperation() 
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CARS :: Appointment Operation ***\n");
            System.out.println("1: Patient Management");
            System.out.println("2: Doctor Management");
            System.out.println("3: Staff Management");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doManagePatients();
                }
                else if(response == 2)
                {
                    doManageDoctors();
                }
                else if(response == 3)
                {
                    doManageStaff();
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
    
    private void doManagePatients()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CARS :: Administration Operation :: Patient Management ***\n");
            System.out.println("1: Add Patient");
            System.out.println("2: View Patient Details");
            System.out.println("3: Update Patient");
            System.out.println("4: Delete Patient");
            System.out.println("5: View All Patients");
            System.out.println("6: Back\n");
            response = 0;
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doAddPatient();
                }
                else if(response == 2)
                {
                    doViewPatient();
                }
                else if(response == 3)
                {
                    doUpdatePatient();
                }
                else if (response == 4)
                {
                    doDeletePatient();
                }
                else if (response == 5)
                {
                    doViewAllPatients();
                }
                else if(response == 6)
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
    
    private void doAddPatient()
    {
        
    }
    
    private void doViewPatient()
    {
        
    }
    
    private void doUpdatePatient()
    {
        
    }
    
    private void doDeletePatient()
    {
        
    }
    
    private void doViewAllPatients()
    {
        
    }
    
    private void doManageDoctors() 
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CARS :: Administration Operation :: Doctor Management ***\n");
            System.out.println("1: Add Doctor");
            System.out.println("2: View Doctor Details");
            System.out.println("3: Update Doctor");
            System.out.println("4: Delete Doctor");
            System.out.println("5: View All Doctors");
            System.out.println("6: Leave Management");
            System.out.println("7: Back\n");
            response = 0;
            
            while(response < 1 || response > 7)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doAddDoctor();
                }
                else if(response == 2)
                {
                    doViewDoctor();
                }
                else if(response == 3)
                {
                    doUpdateDoctor();
                }
                else if (response == 4)
                {
                    doDeleteDoctor();
                }
                else if (response == 5)
                {
                    doViewAllDoctors();
                }
                else if(response == 6)
                {
                    doManageLeaves();
                }
                else if(response == 7)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 7)
            {
                break;
            }
        }
    }
    
    private void doAddDoctor()
      {
                
      }
    
    private void doViewDoctor()
    {
        
    }
    
    private void doUpdateDoctor()
    {
        
    }
    
    private void  doDeleteDoctor()
    {
        
    }
    
    private void doViewAllDoctors()
    {
        
    }
    
    
    private void doManageStaff()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CARS :: Administration Operation :: Staff Management ***\n");
            System.out.println("1: Add Staff");
            System.out.println("2: View Staff Details");
            System.out.println("3: Update Staff");
            System.out.println("4: Delete Staff");
            System.out.println("5: View All Staff");
            System.out.println("6: Back\n");
            response = 0;
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doAddStaff();
                }
                else if(response == 2)
                {
                    doViewStaff();
                }
                else if(response == 3)
                {
                    doUpdateStaff();
                }
                else if (response == 4)
                {
                    doDeleteStaff();
                }
                else if (response == 5)
                {
                    doViewAllStaff();
                }
                else if(response == 6)
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
    
    private void doAddStaff()
    {
        
    }
    
    private void doViewStaff()
    {
        
    }
    
    private void doUpdateStaff()
    {
        
    }
    
    private void doDeleteStaff()
    {
        
    }
    
    private void doViewAllStaff()
    {
        
    }
    
    private void doManageLeaves() 
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CARS :: Administration Operation :: Staff Management ***\n");
            System.out.println("1: Enter Leave Application");
            System.out.println("2: View Leave Details");
            System.out.println("3: Update Leave");
            System.out.println("4: Cancel Leave");
            System.out.println("5: View All Leaves");
            System.out.println("6: Back\n");
            response = 0;
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doAddLeave();
                }
                else if(response == 2)
                {
                    doViewLeave();
                }
                else if(response == 3)
                {
                    doUpdateLeave();
                }
                else if (response == 4)
                {
                    doDeleteLeave();
                }
                else if (response == 5)
                {
                    doViewAllLeaves();
                }
                else if(response == 6)
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
    
    private void doAddLeave()
    {
        
    }
    
    private void  doViewLeave()
    {
        
    }
    
    private void doUpdateLeave()
    {
        
    }
    
    private void  doDeleteLeave()
    {
        
    }
    
    private void doViewAllLeaves()
    {
        
    }
}
