
package ws.restful;

import ejb.session.stateless.PatientEntityControllerLocal;
import entity.PatientEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.PatientNotFoundException;

@Path("PatientEntity")

public class PatientEntityResource {
    
    @Context
    private UriInfo context;
    
    private final PatientEntityControllerLocal patientEntityControllerLocal;
    
    
    
    public PatientEntityResource()
    {
        patientEntityControllerLocal = lookupPatientEntityControllerLocal();
    }

    
    
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_XML)
    public Response createNewPatient(@FormParam("identityNumber") String identityNumber,
                                        @FormParam("password") String password,                          
                                        @FormParam("firstName") String firstName, 
                                        @FormParam("lastName") String lastName, 
                                        @FormParam("gender") String gender, 
                                        @FormParam("age") Integer age,
                                        @FormParam("phone") String phone,
                                        @FormParam("address") String address
                                        )
    {
        try {
             PatientEntity patientEntity = patientEntityControllerLocal.createNewPatient(new PatientEntity(identityNumber, password, firstName, lastName, gender, age, phone, address));
             
             return Response.status(Response.Status.OK).entity(patientEntity).build();
        } catch (EntityInstanceExistsInCollectionException ex) {
            
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
   
    @GET
    @Path("retrievePatientByPatientId")
    @Produces(MediaType.APPLICATION_XML)
    public Response retrievePatientByPatientId(@QueryParam("patientId") Long patientId)
    {
        try
        {
            PatientEntity patientEntity = patientEntityControllerLocal.retrievePatientByPatientId(patientId);
            patientEntity.setIdentityNumber(null);
            patientEntity.setPassword(null);

            return Response.status(Response.Status.OK).entity(patientEntity).build();
        }
        catch(PatientNotFoundException ex)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("retrievePatientByPatientIdentityNumber")
    @Produces(MediaType.APPLICATION_XML)
    public Response retrievePatientByPatientIdentityNumber(@QueryParam("identityNumber") String identityNumber)
    {
        try
        {
            PatientEntity patientEntity = patientEntityControllerLocal.retrievePatientByPatientIdentityNumber(identityNumber);
            patientEntity.setIdentityNumber(null);
            patientEntity.setPassword(null);

            return Response.status(Response.Status.OK).entity(patientEntity).build();
        }
        catch(PatientNotFoundException ex)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    

    private PatientEntityControllerLocal lookupPatientEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            //Change database name
            return (PatientEntityControllerLocal) c.lookup("java:global/Is2103Lecture9/Is2103Lecture9-ejb/PatientEntityController!ejb.session.stateless.PatientEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

