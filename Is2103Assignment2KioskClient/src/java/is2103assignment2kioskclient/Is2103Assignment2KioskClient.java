package is2103assignment2kioskclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import javax.ejb.EJB;

public class Is2103Assignment2KioskClient {

    @EJB
    private static AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    @EJB
    private static DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    @EJB
    private static PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
        
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(appointmentEntitySessionBeanRemote, doctorEntitySessionBeanRemote, patientEntitySessionBeanRemote);
        mainApp.runApp();   
    }
    
}
