/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DoctorEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.DoctorNotFoundException;
import util.exception.EntityMismatchException;

/**
 *
 * @author User
 */
@Local
public interface DoctorEntityControllerLocal {

    Long createNewDoctor(DoctorEntity newDoctorEntity);
    
    List<DoctorEntity> retrieveAllDoctors();
    
    DoctorEntity retrieveDoctorByDoctorId(Long doctorId) throws DoctorNotFoundException;

    DoctorEntity retrieveDoctorByDoctorName(String firstName, String lastName) throws DoctorNotFoundException;
    
    List<DoctorEntity> retrieveDoctorsAvailableOnDate(LocalDate searchDate) throws DoctorNotFoundException;

    void updateDoctor(DoctorEntity doctorEntity) throws EntityMismatchException;
    
    void deleteDoctor(Long doctorId) throws DoctorNotFoundException;
}
