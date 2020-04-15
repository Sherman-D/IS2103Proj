package ejb.session.ws;

import ejb.session.stateless.AppointmentEntityControllerLocal;
import ejb.session.stateless.DoctorEntityControllerLocal;
import ejb.session.stateless.PatientEntityControllerLocal;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import util.exception.AppointmentAlreadyCancelledException;
import util.exception.AppointmentNotFoundException;
import util.exception.DoctorNotFoundException;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.InvalidLoginCredentialException;



@WebService(serviceName = "IS2103Assignment2WebService")
@Stateless

public class IS2103Assignment2WebService
{
    @EJB
    private AppointmentEntityControllerLocal appointmentEntityControllerLocal;
    @EJB
    private PatientEntityControllerLocal patientEntityControllerLocal;
    @EJB
    private DoctorEntityControllerLocal doctorEntityControllerLocal;
    
    
//    
//    @WebMethod(operationName = "retrieveAllProducts")
//    public List<ProductEntity> retrieveAllProducts(@WebParam(name = "username") String username,
//                                                    @WebParam(name = "password") String password) 
//                                throws InvalidLoginCredentialException
//    {
//       PatientEntity patientEntity = patientEntityControllerLocal.patientLogin(username, password);
//        System.out.println("********** PointOfSaleSystemWebService.retrieveAllProducts(): Staff " 
//                            + staffEntity.getUsername() 
//                            + " login remotely via web service");
//        
//        return productEntityControllerLocal.retrieveAllProducts();
//    }
//    
//    
//    
//    @WebMethod(operationName = "retrieveProductByProductId")
//    public ProductEntity retrieveProductByProductId(@WebParam(name = "username") String username,
//                                                    @WebParam(name = "password") String password,
//                                                    @WebParam(name = "productId") Long productId) 
//                                throws InvalidLoginCredentialException, ProductNotFoundException
//    {
//        StaffEntity staffEntity = staffEntityControllerLocal.staffLogin(username, password);
//        System.out.println("********** PointOfSaleSystemWebService.retrieveProductByProductId(): Staff " 
//                            + staffEntity.getUsername() 
//                            + " login remotely via web service");
//        
//        return productEntityControllerLocal.retrieveProductByProductId(productId);
//    }
//    
//    
    @WebMethod(operationName = "createNewPatient")
    public PatientEntity createNewPatient(@WebParam(name = "identityNumber") String identityNumber,
                                          @WebParam(name = "password") String password, 
                                          @WebParam(name = "firstName") String  firstName, 
                                          @WebParam(name = "lastName") String  lastName,
                                          @WebParam(name ="gender")  String gender,
                                          @WebParam(name ="age")  Integer age,
                                          @WebParam(name ="phone")  String phone,
                                          @WebParam(name ="address")  String address)
                                  throws EntityInstanceExistsInCollectionException
    {
        String passwordHash = patientEntityControllerLocal.hashPassword(password);
        PatientEntity newPatientEntity = new PatientEntity(identityNumber, passwordHash, firstName, lastName, gender, age, phone, address);
        
        return patientEntityControllerLocal.createNewPatient(newPatientEntity);
    }
    
    @WebMethod(operationName = "createNewAppointment")
    public AppointmentEntity createNewAppointment(@WebParam(name = "doctorId") String doctorId,
                                                  @WebParam(name = "date") String date,
                                                  @WebParam(name = "time") String time,
                                                  @WebParam(name = "patientId") Long patientId)
    {
        LocalTime t = LocalTime.parse(time);  
        LocalDate d = LocalDate.parse(date);
        LocalDateTime apptTime = t.atDate(d);
        AppointmentEntity ae = new AppointmentEntity(patientId, doctorId, apptTime);
        appointmentEntityControllerLocal.createNewAppointment(ae);
        return ae;
    }
    
    @WebMethod(operationName = "patientLogin")
    public void patientLogin(@WebParam(name = "identityNumber") String identityNumber,
                             @WebParam(name = "password") String password) throws InvalidLoginCredentialException
    {
         return patientEntityControllerLocal.patientLogin(identityNumber, password);

    }
    
    @WebMethod (operationName = "retrieveAppointmentByPatientIdentityNo")
    public List<String> retrieveAppointmentByPatientIdentityNo(@WebParam(name = "identityNumber")String identityNumber)
    {      
        return appointmentEntityControllerLocal.retrieveAppointmentByPatientIdentityNo(identityNumber);
    }
    
    @WebMethod (operationName = "retrieveDoctorsAvailableOnDate")
   public List<String> retrieveDoctorsAvailableOnDate(@WebParam(name ="date") String date) 
   {
       LocalDate d = LocalDate.parse(date);
        return doctorEntityControllerLocal.retrieveDoctorsAvailableOnDate(d);
   }
   
   @WebMethod(operationName = "retrieveDoctorByDoctorId")
   public DoctorEntity retrieveDoctorByDoctorId(@WebParam(name = "doctorId") Long doctorId)
                                                         throws DoctorNotFoundException
   {

        return doctorEntityControllerLocal.retrieveDoctorByDoctorId(doctorId);
   }
   
   @WebMethod(operationName = "retrieveDoctorAvailableSlotsOnDay")
   public List<String> retrieveDoctorAvailableSlotsOnDay(@WebParam(name = "doctorId") Long doctorId,
                                                         @WebParam(name = "date") String date)
   {
       LocalDate d = LocalDate.parse(date);
        
       return appointmentEntityControllerLocal.retrieveDoctorAvailableSlotsOnDay(doctorId, d);
   }
   
   @WebMethod (operationName = "cancelAppointment")
   public void cancelAppointment(@WebParam(name = "appointmentId")Long appointmentId) throws AppointmentNotFoundException, AppointmentAlreadyCancelledException, DoctorNotFoundException
   {
       AppointmentEntity ae = appointmentEntityControllerLocal.retrieveAppointmentByAppointmentId(appointmentId)''
       return appointmentEntityControllerLocal.cancelAppointment(ae);
   }
}
