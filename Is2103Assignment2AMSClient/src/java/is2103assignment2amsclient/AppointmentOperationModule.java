/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is2103assignment2amsclient;

import java.util.Scanner;

/**
 *
 * @author User
 */
public class AppointmentOperationModule {
    
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private StaffEntitySessionBeanRemote staffEntitySessionBeanRemote;
    private LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote;
    
    
        public void menuAdministrationOperation()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while(true)
        {
            System.out.println("*** AMS Client :: Main ***\n");
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

    
}
