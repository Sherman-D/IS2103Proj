package ws.restful;

import ejb.session.stateless.AppointmentEntityControllerLocal;
import entity.AppointmentEntity;
import java.time.LocalDateTime;
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
//import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityMismatchException;
import ws.model.RetrieveAppointmentByPatientIdResponse;

@Path("AppointmentEntity")

public class AppointmentEntityResource {

    @Context
    private UriInfo context;
    
    private final AppointmentEntityControllerLocal appointmentEntityControllerLocal;
    
    
    
    public AppointmentEntityResource()
    {
        appointmentEntityControllerLocal = lookupAppointmentEntityControllerLocal();
    }

    //How to validate that input data doesn't point to an invalid slot?
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_XML)
    public Response createNewAppointment(@FormParam("doctorId") Long doctorId,
                                        @FormParam("date") String date,                          
                                        @FormParam("time") String time, 
                                        @FormParam("patientId") Long patientId
                                        )
    {
        
            String[] strArrDate = date.split("-");
            Integer[] intArrDate = new Integer[3];
            for (int i = 0; i < strArrDate.length; i++) {
                intArrDate[i] = Integer.parseInt(strArrDate[i]);
            }
            
            String[] strArrTime = time.split(":");
            Integer[] intArrTime = new Integer[2];
            for (int i = 0; i < strArrDate.length; i++) {
                 intArrTime[i] = Integer.parseInt(strArrTime[i]);
            }
            
            LocalDateTime appointmentTime = LocalDateTime.of(intArrDate[0], intArrDate[1], intArrDate[2], intArrTime[0], intArrTime[1]);
            AppointmentEntity appointmentEntity = appointmentEntityControllerLocal.createNewAppointment(new AppointmentEntity(patientId, doctorId, appointmentTime));
             
             return Response.status(Response.Status.OK).entity(appointmentEntity).build();
        /*} catch (EntityInstanceExistsInCollectionException ex) {
            
            return Response.status(Response.Status.CONFLICT).build();
        }*/
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
    @Path(value = "retrieveAppointmentByPatientId")
    @Produces(MediaType.APPLICATION_XML)
    public Response retrieveAppointmentByPatientId(@QueryParam("patientId") Long patientId) 
    {
            RetrieveAppointmentByPatientIdResponse  retrieveAppointmentByPatientIdResponse = new  RetrieveAppointmentByPatientIdResponse(appointmentEntityControllerLocal.retrieveAppointmentByPatientId(patientId));

            return Response.status(Status.OK).entity(retrieveAppointmentByPatientIdResponse).build();

    }

    @PUT
    @Path("{staffId}")
    public Response confirmAppointment(@PathParam("appointmentId") Long appointmentId,
                                                                                 @PathParam("patientId") Long patientId)
    {
        try
        {
            appointmentEntityControllerLocal.confirmAppointment(patientId, appointmentId);

            return Response.status(Response.Status.OK).build();
        }
        catch(AppointmentNotFoundException ex)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @PUT
    @Path("{staffId}")
    public Response cancelAppointment(@PathParam("appointmentId") Long appointmentId,
                                                                                 @PathParam("patientId") Long patientId)
    {
        try
        {
            appointmentEntityControllerLocal.cancelAppointment(appointmentEntityControllerLocal.retrieveAppointmentByAppointmentId(appointmentId));

            return Response.status(Response.Status.OK).build();
        }
        catch(AppointmentNotFoundException | EntityMismatchException | AppointmentAlreadyCancelledException ex)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    
    private AppointmentEntityControllerLocal lookupAppointmentEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            //TEMPORARY DATABASE NAME
            return (AppointmentEntityControllerLocal) c.lookup("java:global/Is2103Lecture9/Is2103Lecture9-ejb/AppointmentEntityController!ejb.session.stateless.AppointmentEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
