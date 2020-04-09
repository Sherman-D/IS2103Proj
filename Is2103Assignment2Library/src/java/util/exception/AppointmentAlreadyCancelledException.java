package util.exception;



public class AppointmentAlreadyCancelledException extends Exception
{
    public AppointmentAlreadyCancelledException()
    {
    }
    
    
    
    public AppointmentAlreadyCancelledException(String msg)
    {
        super(msg);
    }
}