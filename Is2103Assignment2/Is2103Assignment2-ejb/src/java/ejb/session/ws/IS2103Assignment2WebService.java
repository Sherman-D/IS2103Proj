package ejb.session.ws;

import ejb.session.stateless.AppointmentEntityControllerLocal;
import ejb.session.stateless.PatientEntityControllerLocal;
import entity.AppointmentEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import javax.jws.WebParam;
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
    
    
    
    @WebMethod(operationName = "retrieveAllProducts")
    public List<ProductEntity> retrieveAllProducts(@WebParam(name = "username") String username,
                                                    @WebParam(name = "password") String password) 
                                throws InvalidLoginCredentialException
    {
       PatientEntity patientEntity = patientEntityControllerLocal.patientLogin(username, password);
        System.out.println("********** PointOfSaleSystemWebService.retrieveAllProducts(): Staff " 
                            + staffEntity.getUsername() 
                            + " login remotely via web service");
        
        return productEntityControllerLocal.retrieveAllProducts();
    }
    
    
    
    @WebMethod(operationName = "retrieveProductByProductId")
    public ProductEntity retrieveProductByProductId(@WebParam(name = "username") String username,
                                                    @WebParam(name = "password") String password,
                                                    @WebParam(name = "productId") Long productId) 
                                throws InvalidLoginCredentialException, ProductNotFoundException
    {
        StaffEntity staffEntity = staffEntityControllerLocal.staffLogin(username, password);
        System.out.println("********** PointOfSaleSystemWebService.retrieveProductByProductId(): Staff " 
                            + staffEntity.getUsername() 
                            + " login remotely via web service");
        
        return productEntityControllerLocal.retrieveProductByProductId(productId);
    }
    
    
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
        PatientEntity newPatientEntity = new PatientEntity(identityNumber, password, firstName, lastName, gender, age, phone, address);
        
        return patientEntityControllerLocal.createNewPatient(newPatientEntity);
    }
    
    @WebMethod(operationName = "createNewAppointment")
    public AppointmentEntity createNewAppointment(@WebParam(name = "doctorId") String doctorId,
                                                                                                            @WebParam(name = "date") String date,
                                                                                                            @WebParam(name = "time") String time,
                                                                                                            @WebParam(name = "patientId") Long patientId)
    {
        
     return new AppointmentEntity();
    }
}
