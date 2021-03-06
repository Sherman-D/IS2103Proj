package ws.restful;

import java.util.Set;
import javax.ws.rs.core.Application;


@javax.ws.rs.ApplicationPath("Is2103Assignment2Resources")

public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    
    
    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) 
    {
        resources.add(ws.restful.AppointmentEntityResource.class);
        resources.add(ws.restful.CorsFilter.class);
        resources.add(ws.restful.DoctorEntityResource.class);
        resources.add(ws.restful.PatientEntityResource.class);
    }    
}