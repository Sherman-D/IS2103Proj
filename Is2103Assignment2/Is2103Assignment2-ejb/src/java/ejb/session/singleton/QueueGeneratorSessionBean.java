package ejb.session.singleton;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Startup;


@Singleton
@LocalBean
@Remote
@Startup
public class QueueGeneratorSessionBean {
    private Integer queueNumber;
    
    public QueueGeneratorSessionBean()
    {
        
    }
    
    
    @PostConstruct
    public void postConstruct()
    {
        queueNumber = 0;
    }
    
    public Integer getNextQueueNumber()
    {
        Integer newNumber = queueNumber.intValue();
        
        queueNumber += 1;
        
        return newNumber;
    }
}
