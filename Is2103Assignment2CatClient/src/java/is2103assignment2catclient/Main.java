package is2103assignment2catclient;

import ejb.session.singleton.QueueGeneratorSessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.LeaveEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.StaffEntitySessionBeanRemote;


public class Main {
        
    @EJB
    private static AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    @EJB
    private static DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    @EJB
    private static LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote;
    @EJB
    private static PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    @EJB
    private static QueueGeneratorSessionBeanRemote queueGeneratorSessionBeanRemote; 
    @EJB
    private static StaffEntitySessionBeanRemote staffEntitySessionBeanRemote; 
     
        

        public static void main(String[] args) 
        {

            MainApp mainApp = new MainApp(appointmentEntitySessionBeanRemote,  doctorEntitySessionBeanRemote, leaveEntitySessionBeanRemote, patientEntitySessionBeanRemote, queueGeneratorSessionBeanRemote, staffEntitySessionBeanRemote);
            mainApp.runApp();     
        }

}
