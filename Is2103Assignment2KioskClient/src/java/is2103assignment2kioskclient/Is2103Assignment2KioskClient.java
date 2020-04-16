package is2103assignment2kioskclient;

import ejb.session.singleton.QueueGeneratorSessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.ClinicEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.LeaveEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import javax.ejb.EJB;


public class Is2103Assignment2KioskClient {


    @EJB
    private static AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    @EJB
    private static ClinicEntitySessionBeanRemote clinicEntitySessionBeanRemote;
    @EJB
    private static DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    @EJB
    private static PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    @EJB
    private static QueueGeneratorSessionBeanRemote queueGeneratorSessionBeanRemote;
    @EJB
    private static LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote; 


    public static void main(String[] args) {
        MainApp mainApp = new MainApp(appointmentEntitySessionBeanRemote, clinicEntitySessionBeanRemote, doctorEntitySessionBeanRemote, patientEntitySessionBeanRemote, queueGeneratorSessionBeanRemote, leaveEntitySessionBeanRemote);
        mainApp.runApp();   
    }
    
}
    