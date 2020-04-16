package ejb.session.ws;

import ejb.session.stateless.AppointmentEntityControllerLocal;
import ejb.session.stateless.DoctorEntityControllerLocal;
import ejb.session.stateless.PatientEntityControllerLocal;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PatientNotFoundException;



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
                                  throws EntityInstanceExistsInCollectionException, NoSuchAlgorithmException
    {
        
        String passwordHash = patientEntityControllerLocal.hashPassword(password);
        PatientEntity newPatientEntity = new PatientEntity(identityNumber, passwordHash, firstName, lastName, gender, age, phone, address);
        
        return patientEntityControllerLocal.createNewPatient(newPatientEntity);
    }
    
    @WebMethod(operationName = "createNewAppointment")
    public AppointmentEntity createNewAppointment(@WebParam(name = "doctorId") Long doctorId,
                                                  @WebParam(name = "patientId") Long patientId,
                                                  @WebParam(name = "date") String dateTime) throws DoctorNotFoundException, PatientNotFoundException
    {
        LocalDateTime apptTime = LocalDateTime.parse(dateTime);
        DoctorEntity doctor = doctorEntityControllerLocal.retrieveDoctorByDoctorId(doctorId);
        PatientEntity patient = patientEntityControllerLocal.retrievePatientByPatientId(patientId);
        AppointmentEntity ae = new AppointmentEntity(patient, doctor, apptTime);
        appointmentEntityControllerLocal.createNewAppointment(ae);
        return ae;
    }
    
    @WebMethod(operationName = "patientLogin")
    public PatientEntity patientLogin(@WebParam(name = "identityNumber") String identityNumber,
                                                        @WebParam(name = "password") String password) throws InvalidLoginCredentialException, NoSuchAlgorithmException
    {   
         return patientEntityControllerLocal.patientLogin(identityNumber, password);

    }
    
    @WebMethod (operationName = "retrieveAppointmentByPatientIdentityNo")
    public List<String> retrieveAppointmentByPatientIdentityNo(@WebParam(name = "identityNumber")String identityNumber)
    {      
        return appointmentEntityControllerLocal.retrieveAppointmentByPatientIdentityNo(identityNumber);
    }
    
    @WebMethod (operationName = "retrieveDoctorsAvailableOnDate")
   public List<DoctorEntity> retrieveDoctorsAvailableOnDate(@WebParam(name ="date") String date) 
                                                        throws DoctorNotFoundException
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
       List<LocalTime> objTimes = appointmentEntityControllerLocal.retrieveDoctorAvailableSlotsOnDay(doctorId, d);
       List<String> strTime = new ArrayList<>();
       for (LocalTime time : objTimes) 
       {
           strTime.add(time.toString());
       }
       return strTime;
   }
   
   @WebMethod (operationName = "cancelAppointment")
   public AppointmentEntity cancelAppointment(@WebParam(name = "appointmentId")Long appointmentId) 
                throws AppointmentNotFoundException, AppointmentAlreadyCancelledException, DoctorNotFoundException, EntityMismatchException
   {
       AppointmentEntity ae = appointmentEntityControllerLocal.retrieveAppointmentByAppointmentId(appointmentId);
       appointmentEntityControllerLocal.cancelAppointment(ae);
       return ae;
   }
}
