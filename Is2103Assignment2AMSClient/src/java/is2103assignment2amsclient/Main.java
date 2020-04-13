/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is2103assignment2amsclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.StaffEntitySessionBeanRemote;


public class Main {
        
    @EJB
    private static AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    @EJB
    private static DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    @EJB
    private static PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    @EJB
    private static StaffEntitySessionBeanRemote staffEntitySessionBeanRemote; 
        

        public static void main(String[] args) 
        {

            MainApp mainApp = new MainApp(appointmentEntitySessionBeanRemote, doctorEntitySessionBeanRemote, patientEntitySessionBeanRemote, staffEntitySessionBeanRemote);
            mainApp.runApp();     
        }

}
