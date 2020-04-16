package ejb.session.singleton;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Remote;
import javax.ejb.Startup;


@Singleton
@Local(QueueGeneratorSessionBeanLocal.class)
@Remote(QueueGeneratorSessionBeanRemote.class)
@Startup
public class QueueGeneratorSessionBean implements QueueGeneratorSessionBeanLocal, QueueGeneratorSessionBeanRemote {
    private Integer queueNumber;
    
    public QueueGeneratorSessionBean()
    {
        
    }
    
    
    @PostConstruct
    public void postConstruct()
    {
        queueNumber = 1;
    }
    
    @Override
    public Integer getNextQueueNumber()
    {
        Integer newNumber = queueNumber.intValue();
        
        queueNumber += 1;
        
        return newNumber;
    }
}
