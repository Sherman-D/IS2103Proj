package ws.restful;

import ejb.session.stateless.AppointmentEntityControllerLocal;
import ejb.session.stateless.DoctorEntityControllerLocal;
import ejb.session.stateless.PatientEntityControllerLocal;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.AppointmentAlreadyCancelledException;
import util.exception.AppointmentNotFoundException;
import util.exception.DoctorNotFoundException;
//import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityMismatchException;
import util.exception.PatientNotFoundException;
import ws.model.RetrieveAppointmentByPatientIdResponse;
import ws.model.RetrieveDoctorAvailableSlotsOnDayResponse;

@Path("AppointmentEntity")

public class AppointmentEntityResource {

    @Context
    private UriInfo context;
    
    private final AppointmentEntityControllerLocal appointmentEntityControllerLocal;
    private final PatientEntityControllerLocal patientEntityControllerLocal;
    private final DoctorEntityControllerLocal doctorEntityControllerLocal;
    
    
    
    public AppointmentEntityResource()
    {
        appointmentEntityControllerLocal = lookupAppointmentEntityControllerLocal();
        patientEntityControllerLocal = lookupPatientEntityControllerLocal();
        doctorEntityControllerLocal = lookupDoctorEntityControllerLocal();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_XML)
    public Response createNewAppointment(@FormParam("doctorId") Long doctorId,
                                        @FormParam("date") String date,                          
                                        @FormParam("time") String time, 
                                        @FormParam("patientId") Long patientId
                                        )
    {
            LocalDate appDate = LocalDate.parse(date);
            LocalTime appTime = LocalTime.parse(time);
            LocalDateTime appointmentTime = appDate.atTime(appTime);
      
            try {
            PatientEntity patient = patientEntityControllerLocal.retrievePatientByPatientId(patientId);
            DoctorEntity doctor = doctorEntityControllerLocal.retrieveDoctorByDoctorId(doctorId);
            AppointmentEntity appointmentEntity = appointmentEntityControllerLocal.createNewAppointment(new AppointmentEntity(patient, doctor, appointmentTime));
             
             return Response.status(Response.Status.OK).entity(appointmentEntity).build();
            }
            catch (PatientNotFoundException | DoctorNotFoundException ex) {
            
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    
    @GET
    @Path(value = "retrieveAppointmentByAppointmentId")
    @Produces(MediaType.APPLICATION_XML)
    public Response retrieveAppointmentByAppointmentId(@QueryParam("appointmentId") Long appointmentId) 
    {
        try
        {
            AppointmentEntity appointmentEntity = appointmentEntityControllerLocal.retrieveAppointmentByAppointmentId(appointmentId);

            return Response.status(Status.OK).entity(appointmentEntity).build();
        }
        catch(AppointmentNotFoundException ex)
        {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    
    @GET
    @Path(value = "retrieveAppointmentByPatientIdentityNo")
    @Produces(MediaType.APPLICATION_XML)
    public Response retrieveAppointmentByPatientId(@QueryParam("identityNumber") String identityNumber) 
    {
            RetrieveAppointmentByPatientIdResponse  retrieveAppointmentByPatientIdResponse = new  RetrieveAppointmentByPatientIdResponse(appointmentEntityControllerLocal.retrieveAppointmentByPatientIdentityNo(identityNumber));

            return Response.status(Status.OK).entity(retrieveAppointmentByPatientIdResponse).build();

    }
    
    @GET
    @Path("retrieveDoctorAvailableSlotsOnDay")
    @Produces(MediaType.APPLICATION_XML)
    public Response retrieveDoctorAvailableSlotsOnDay(@QueryParam("doctorId") Long doctorId,
                                                                                                           @QueryParam("date") String date )
    {
  
            RetrieveDoctorAvailableSlotsOnDayResponse retrieveDoctorAvailableSlotsOnDayResponse = new  RetrieveDoctorAvailableSlotsOnDayResponse(appointmentEntityControllerLocal.retrieveDoctorAvailableSlotsOnDay(doctorId, LocalDate.parse(date)));
            
            return Response.status(Response.Status.OK).entity(retrieveDoctorAvailableSlotsOnDayResponse).build();
        
    }

    
    @PUT
    @Path("cancelAppointment")
    public Response cancelAppointment(@PathParam("appointmentId") Long appointmentId,
                                                                                 @PathParam("patientId") Long patientId)
    {
        try
        {
            AppointmentEntity appointment = appointmentEntityControllerLocal.cancelAppointment(appointmentEntityControllerLocal.retrieveAppointmentByAppointmentId(appointmentId));

            return Response.status(Response.Status.OK).entity(appointment).build();
        }
        catch(AppointmentNotFoundException | EntityMismatchException | AppointmentAlreadyCancelledException ex)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    
    private AppointmentEntityControllerLocal lookupAppointmentEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (AppointmentEntityControllerLocal) c.lookup("java:global/Is2103Assignment2/Is2103Assignment2-ejb/AppointmentEntityController!ejb.session.stateless.AppointmentEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private PatientEntityControllerLocal lookupPatientEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            //TEMPORARY DATABASE NAME
            return (PatientEntityControllerLocal) c.lookup("java:global/Is2103Assignment2/Is2103Assignment2-ejb/PatientEntityController!ejb.session.stateless.PatientEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private DoctorEntityControllerLocal lookupDoctorEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            //TEMPORARY DATABASE NAME
            return (DoctorEntityControllerLocal) c.lookup("java:global/Is2103Assignment2/Is2103Assignment2-ejb/DoctorEntityController!ejb.session.stateless.DoctorEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
