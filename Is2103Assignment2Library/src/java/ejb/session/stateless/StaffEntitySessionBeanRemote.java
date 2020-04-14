/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import util.exception.EntityMismatchException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;



public interface StaffEntitySessionBeanRemote
{

    Long createNewStaff(StaffEntity newStaffEntity);
    
    List<StaffEntity> retrieveAllStaffs();
    
    StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException;
    
    StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;

    StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;

    void updateStaff(StaffEntity staffEntity) throws EntityMismatchException;
    
    void deleteStaff(Long staffId) throws StaffNotFoundException;

    String hashPassword(String password) throws NoSuchAlgorithmException;
}
