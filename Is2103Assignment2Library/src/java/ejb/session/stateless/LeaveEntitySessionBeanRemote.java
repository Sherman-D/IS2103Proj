package ejb.session.stateless;

import entity.LeaveEntity;
import java.time.LocalDate;
import java.util.List;
import util.exception.EntityMismatchException;
import util.exception.LeaveNotFoundException;


public interface LeaveEntitySessionBeanRemote
{

     Long createNewLeave(LeaveEntity newLeaveEntity);
    
     List<LeaveEntity> retrieveAllLeaves();
    
    LeaveEntity retrieveLeaveByLeaveId(Long leaveId) throws LeaveNotFoundException;
    
    List<LeaveEntity> retrieveLeaveByDoctorId(Long doctorId) throws LeaveNotFoundException;

    List<LeaveEntity> retrieveLeaveByDate(LocalDate searchDate);
    
    void cancelLeave(LeaveEntity leaveEntity) throws EntityMismatchException;
}
