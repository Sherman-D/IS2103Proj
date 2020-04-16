
package ws.restful;

import ejb.session.stateless.DoctorEntityControllerLocal;
import entity.DoctorEntity;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.DoctorNotFoundException;
import ws.model.RetrieveDoctorsAvailableOnDateResponse;

@Path("DoctorEntity")

public class DoctorEntityResource {
    
    @Context
    private UriInfo context;
    
    private final DoctorEntityControllerLocal doctorEntityControllerLocal;
    
    
    
    public DoctorEntityResource()
    {
        doctorEntityControllerLocal = lookupDoctorEntityControllerLocal();
    }

   
   
    @GET
    @Path("retrieveDoctorByDoctorId")
    @Produces(MediaType.APPLICATION_XML)
    public Response retrieveDoctorByDoctorId(@QueryParam("doctorId") Long doctorId)
    {
        try
        {
            DoctorEntity doctorEntity = doctorEntityControllerLocal.retrieveDoctorByDoctorId(doctorId);

            return Response.status(Response.Status.OK).entity(doctorEntity).build();
        }
        catch(DoctorNotFoundException ex)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("retrieveDoctorsAvailableOnDate")
    @Produces(MediaType.APPLICATION_XML)
    public Response retrieveDoctorsAvailableOnDate(@QueryParam("date") String date)
    {
        try
        {
          
            RetrieveDoctorsAvailableOnDateResponse retrieveDoctorsAvailableOnDateResponse = new  RetrieveDoctorsAvailableOnDateResponse(doctorEntityControllerLocal.retrieveDoctorsAvailableOnDate(LocalDate.parse(date)));
            
            return Response.status(Response.Status.OK).entity(retrieveDoctorsAvailableOnDateResponse).build();
        }
        catch(DoctorNotFoundException ex)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
       

    private DoctorEntityControllerLocal lookupDoctorEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            //Change database name
            return (DoctorEntityControllerLocal) c.lookup("java:global/Is2103Assignment2/Is2103Assignment2-ejb/DoctorEntityController!ejb.session.stateless.DoctorEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

