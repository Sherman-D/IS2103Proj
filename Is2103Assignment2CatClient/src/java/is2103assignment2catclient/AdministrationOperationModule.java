package is2103assignment2catclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.DoctorEntitySessionBeanRemote;
import ejb.session.stateless.LeaveEntitySessionBeanRemote;
import ejb.session.stateless.PatientEntitySessionBeanRemote;
import ejb.session.stateless.StaffEntitySessionBeanRemote;
import java.util.Scanner;
import entity.DoctorEntity;
import entity.LeaveEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import util.exception.DoctorNotFoundException;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityManagerException;
import util.exception.EntityMismatchException;
import util.exception.LeaveNotFoundException;
import util.exception.PatientNotFoundException;
import util.exception.StaffNotFoundException;

public class AdministrationOperationModule {
    private PatientEntitySessionBeanRemote patientEntitySessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private DoctorEntitySessionBeanRemote doctorEntitySessionBeanRemote;
    private StaffEntitySessionBeanRemote staffEntitySessionBeanRemote;
    private LeaveEntitySessionBeanRemote leaveEntitySessionBeanRemote;

    public void menuAdministrationOperation()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while(true)
        {
            System.out.println("*** CARS :: Appointment Operation ***\n");
            System.out.println("1: Patient Management");
            System.out.println("2: Doctor Management");
            System.out.println("3: Staff Management");
            System.out.println("4: Back\n");
            response = 0;

            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doManagePatients();
                }
                else if(response == 2)
                {
                    doManageDoctors();
                }
                else if(response == 3)
                {
                    doManageStaff();
                }
                else if(response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if(response == 4)
            {
                break;
            }
        }
    }

    private void doManagePatients() throws EntityMismatchException
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while(true)
        {
            System.out.println("*** CARS :: Administration Operation :: Patient Management ***\n");
            System.out.println("1: Add Patient");
            System.out.println("2: View Patient Details");
            System.out.println("3: Update Patient");
            System.out.println("4: Delete Patient");
            System.out.println("5: View All Patients");
            System.out.println("6: Back\n");
            response = 0;

            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doAddPatient();
                }
                else if(response == 2)
                {
                    doViewPatient();
                }
                else if(response == 3)
                {
                    doUpdatePatient();
                }
                else if (response == 4)
                {
                    doDeletePatient();
                }
                else if (response == 5)
                {
                    doViewAllPatients();
                }
                else if(response == 6)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if(response == 6)
            {
                break;
            }
        }
    }

    private void doAddPatient()
    {
        Scanner scanner = new Scanner(System.in);
        PatientEntity pe = new PatientEntity();

        System.out.println("*** CARS :: Administartion Operation :: Patient Management :: Register New Patient ***\n");

        System.out.println("Enter Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.println("Enter Password> ");
        String password = scanner.nextLine().trim();
        System.out.println("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.println("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();
        System.out.println("Enter Gender> ");
        String gender = scanner.nextLine().trim();
        System.out.println("Enter Age> ");
        String age = scanner.nextLine().trim();
        System.out.println("Enter Phone> ");
        String phone = scanner.nextLine().trim();
        System.out.println("Enter Address> ");
        String address = scanner.nextLine();

        try
        {
            Integer age1 = Integer.parseInt(age);
            patientEntitySessionBeanRemote.createNewPatient(new PatientEntity(identityNumber, password, firstName, lastName, gender, age1, phone, address));
            System.out.println("Patient has been registered successfully!");
        }
        catch (EntityInstanceExistsInCollectionException ex)
        {
            System.out.println("An error has occurred while registering the new patient: " + ex.getMessage() + "\n");
        }

    }

    private void doViewPatient() //details
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Appointment Operation :: View Patient Details ***\n");

        System.out.println("Enter Patient Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        try
        {
            PatientEntity pe = patientEntitySessionBeanRemote.retrievePatientByIdentityNumber(identityNumber);
            String header = String.format("%s-10|%s-10|%s-1|%s-1|%s-4|%s", "First Name", "Last Name", "Gender", "Age", "Phone", "Address");
            System.out.println(header);
            String patientDetails = String.format("%s-10|%s-10|%s-1|%s-1|%s-4|%s", pe.getFirstName(), pe.getLastName(), pe.getGender(), pe.getAge(), pe.getPhone(), pe.getAddress());
            System.out.println(patientDetails);

        }
        catch(PatientNotFoundException ex)
        {
            System.out.println("An error has occurred while viewing the patient details: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdatePatient() 
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administration Operation :: Patient Management :: Update Patient ***\n");

        System.out.println("Enter Patient Identity Number> ");
        String identityNumber = scanner.nextLine().trim();

        try
        {
            PatientEntity pe = patientEntitySessionBeanRemote.retrievePatientByIdentityNumber(identityNumber);
            Long patientId = pe.getPatientId();
            String password = pe.getPassword();
//            System.out.println("Enter Password> ");
//            String password = scanner.nextLine().trim();
            System.out.println("Enter First Name> ");
            String firstName = scanner.nextLine().trim();
            System.out.println("Enter Last Name> ");
            String lastName = scanner.nextLine().trim();
            System.out.println("Enter Gender> ");
            String gender = scanner.nextLine().trim();
            System.out.println("Enter Age> ");
            String age = scanner.nextLine().trim();
            System.out.println("Enter Phone> ");
            String phone = scanner.nextLine().trim();
            System.out.println("Enter Address> ");
            String address = scanner.nextLine();

            Integer age1 = Integer.parseInt(age);
            PatientEntity pe1 = new PatientEntity(identityNumber, password, firstName, lastName, gender, age1, phone, address);
            pe1.setPatientId(patientId);
            patientEntitySessionBeanRemote.updatePatient(pe1);

        }
        catch(PatientNotFoundException ex)
        {
            System.out.println("An error has occurred while updating the patient details: " + ex.getMessage() + "\n");

        }
        catch(EntityMismatchException ex)
        {
            System.out.println("An error has occurred while updating the patient details: " + ex.getMessage() + "\n");
        }

    }

    private void doDeletePatient()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administration Operation :: Patient Management :: Delete Patient ***\n");

        System.out.println("Enter Patient Identity Number> ");
        String identityNumber = scanner.nextLine().trim();

        try
        {
            PatientEntity pe = patientEntitySessionBeanRemote.retrievePatientByIdentityNumber(identityNumber);
            Long patientId = pe.getPatientId();
            patientEntitySessionBeanRemote.deletePatient(patientId);
        }
        catch(PatientNotFoundException ex)
        {
            System.out.println("An error has occurred while deleting the patient: " + ex.getMessage() + "\n");
        }
    }

    private void doViewAllPatients()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administration Operation :: Patient Management :: View All Patients ***\n");

//        try
//        {
        List<PatientEntity> patientList = patientEntitySessionBeanRemote.retrieveAllPatients();
        String header = String.format("%s-1|%s-10|%s-10|%s-1|%s-1|%s-4|%s", "patientId", "First Name", "Last Name", "Gender", "Age", "Phone", "Address");
        System.out.println(header);
        for(PatientEntity pe : patientList){
            String patientDetails = String.format("%s-1|%s-10|%s-10|%s-1|%s-1|%s-4|%s", pe.getPatientId(), pe.getFirstName(), pe.getLastName(), pe.getGender(), pe.getAge(), pe.getPhone(), pe.getAddress());
            System.out.println(patientDetails);
        }
//        }
//        catch()
//        {
//
//        }
    }

    private void doManageDoctors()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while(true)
        {
            System.out.println("*** CARS :: Administration Operation :: Doctor Management ***\n");
            System.out.println("1: Add Doctor");
            System.out.println("2: View Doctor Details");
            System.out.println("3: Update Doctor");
            System.out.println("4: Delete Doctor");
            System.out.println("5: View All Doctors");
            System.out.println("6: Leave Management");
            System.out.println("7: Back\n");
            response = 0;

            while(response < 1 || response > 7)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doAddDoctor();
                }
                else if(response == 2)
                {
                    doViewDoctor();
                }
                else if(response == 3)
                {
                    doUpdateDoctor();
                }
                else if (response == 4)
                {
                    doDeleteDoctor();
                }
                else if (response == 5)
                {
                    doViewAllDoctors();
                }
                else if(response == 6)
                {
                    doManageLeaves();
                }
                else if(response == 7)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if(response == 7)
            {
                break;
            }
        }
    }

    private void doAddDoctor()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administartion Operation :: Doctor Management :: Add Doctor ***\n");

        System.out.println("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.println("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();
        System.out.println("Enter Registration> ");
        String registration = scanner.nextLine().trim();
        System.out.println("Enter Qualification> ");
        String qualification = scanner.nextLine().trim();

        try
        {
            DoctorEntity de = new DoctorEntity(firstName, lastName, registration, qualification);
            doctorEntitySessionBeanRemote.createNewDoctor(de);
            System.out.println("Doctor has been created successfully!");
        }
        catch(EntityInstanceExistsInCollectionException ex)
        {
            System.out.println("An error has occurred while adding the new doctor: " + ex.getMessage() + "\n");
        }

    }

    private void doViewDoctor()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administartion Operation :: Doctor Management :: View Doctor Details ***\n");

        System.out.println("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.println("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();

        try
        {
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorName(firstName, lastName);
            String header = String.format("%s-1|%s-10|%s-10|%s-1|%s", "doctorId", "First Name", "Last Name", "Registration", "Qualification");
            System.out.println(header);
            String doctorDetails = String.format("%s-1|%s-10|%s-10|%s-1|%s", de.getDoctorId(), de.getFirstName(), de.getLastName(), de.getRegistration(), de.getQualification());
            System.out.println(doctorDetails);
        }
        catch(DoctorNotFoundException ex)
        {
            System.out.println("An error has occurred while viewing the doctor: " + ex.getMessage() + "\n");
        }

    }

    private void doUpdateDoctor()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Update Doctor ***\n");

        System.out.println("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.println("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();


        try
        {
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorName(firstName, lastName);
            System.out.println("Enter First Name> ");
            String firstName1 = scanner.nextLine().trim();
            System.out.println("Enter Last Name> ");
            String lastName1 = scanner.nextLine().trim();
            System.out.println("Enter Registration> ");
            String registration1 = scanner.nextLine().trim();
            System.out.println("Enter Qualifications> ");
            String qualifications = scanner.nextLine().trim();

            Long doctorId = de.getDoctorId();
            DoctorEntity de1 = new DoctorEntity(firstName1, lastName1, registration1, qualifications);
            de1.setDoctorId(doctorId);
            doctorEntitySessionBeanRemote.updateDoctor(de1);

        }
        catch(DoctorNotFoundException ex)
        {
            System.out.println("An error has occurred while updating the doctor details: " + ex.getMessage() + "\n");

        }
        catch(EntityMismatchException ex)
        {
            System.out.println("An error has occurred while updating the doctor details: " + ex.getMessage() + "\n");
        }
    }

    private void  doDeleteDoctor()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Delete Doctor ***\n");

        System.out.println("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.println("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();

        try
        {
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorName(firstName, lastName);
            Long doctorId = de.getDoctorId();
            doctorEntitySessionBeanRemote.deleteDoctor(doctorId);
        }
        catch(DoctorNotFoundException ex)
        {
            System.out.println("An error has occurred while deleting the docotr: " + ex.getMessage() + "\n");
        }

    }

    private void doViewAllDoctors()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administration Operation :: Doctor Management :: View All Doctors ***\n");

        List<DoctorEntity> doctorList = doctorEntitySessionBeanRemote.retrieveAllDoctors();
        String header = String.format("%s-1|%s-10|%s-10|%s-1|%s", "doctorId", "First Name", "Last Name", "Registration", "Qualification");
        System.out.println(header);
        for(DoctorEntity de : doctorList){
            String doctorDetails = String.format("%s-1|%s-10|%s-10|%s-1|%s", de.getDoctorId(), de.getFirstName(), de.getLastName(), de.getRegistration(), de.getQualification());
            System.out.println(doctorDetails);
        }
    }


    private void doManageStaff()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while(true)
        {
            System.out.println("*** CARS :: Administration Operation :: Staff Management ***\n");
            System.out.println("1: Add Staff");
            System.out.println("2: View Staff Details");
            System.out.println("3: Update Staff");
            System.out.println("4: Delete Staff");
            System.out.println("5: View All Staff");
            System.out.println("6: Back\n");
            response = 0;

            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doAddStaff();
                }
                else if(response == 2)
                {
                    doViewStaff();
                }
                else if(response == 3)
                {
                    doUpdateStaff();
                }
                else if (response == 4)
                {
                    doDeleteStaff();
                }
                else if (response == 5)
                {
                    doViewAllStaff();
                }
                else if(response == 6)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if(response == 6)
            {
                break;
            }
        }
    }

    private void doAddStaff()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administartion Operation :: Staff Management :: Add Staff ***\n");

        System.out.println("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.println("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();
        System.out.println("Enter Username> ");
        String username = scanner.nextLine().trim();
        System.out.println("Enter Password> ");
        String password = scanner.nextLine().trim();

        try
        {
            StaffEntity se = new StaffEntity(firstName, lastName, username, password);
            staffEntitySessionBeanRemote.createNewStaff(se);
            System.out.println("Staff has been created successfully!");
        }
        catch(EntityInstanceExistsInCollectionException ex)
        {
            System.out.println("An error has occurred while adding the new staff: " + ex.getMessage() + "\n");
        }
    }

    private void doViewStaff()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administartion Operation :: Staff Management :: View Staff Details ***\n");

        System.out.println("Enter Username> ");
        String username = scanner.nextLine().trim();

        try
        {
            StaffEntity se = staffEntitySessionBeanRemote.retrieveStaffByUsername(username);
            String header = String.format("%s-1|%s-10|%s-10|%s-1|%s", "staffId", "First Name", "Last Name", "Username", "Password");
            System.out.println(header);
            String staffDetails = String.format("%s-1|%s-10|%s-10|%s-1|%s", se.getStaffId(), se.getFirstName(), se.getLastName(), se.getUserName(), se.getPassword());
            System.out.println(staffDetails);
        }
        catch(StaffNotFoundException ex)
        {
            System.out.println("An error has occurred while viewing the staff: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateStaff()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administration Operation :: Staff Management :: Update Staff ***\n");

        System.out.println("Enter Username> ");
        String username = scanner.nextLine().trim();

        try
        {
            StaffEntity se = staffEntitySessionBeanRemote.retrieveStaffByUsername(username);
            System.out.println("Enter First Name> ");
            String firstName1 = scanner.nextLine().trim();
            System.out.println("Enter Last Name> ");
            String lastName1 = scanner.nextLine().trim();
            System.out.println("Enter Username> ");
            String username1 = scanner.nextLine().trim();
            System.out.println("Enter Password> ");
            String password = scanner.nextLine().trim();

            Long staffId = se.getStaffId();
            StaffEntity se1 = new StaffEntity(firstName1, lastName1, username1, password);
            se1.setStaffId(staffId);
            staffEntitySessionBeanRemote.updateStaff(se1);

        }
        catch(StaffNotFoundException ex)
        {
            System.out.println("An error has occurred while updating the staff details: " + ex.getMessage() + "\n");

        }
        catch(EntityMismatchException ex)
        {
            System.out.println("An error has occurred while updating the staff details: " + ex.getMessage() + "\n");
        }
    }

    private void doDeleteStaff()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administration Operation :: Staff Management :: Delete Staff ***\n");

        System.out.println("Enter Username> ");
        String username = scanner.nextLine().trim();

        try
        {
            StaffEntity se = staffEntitySessionBeanRemote.retrieveStaffByUsername(username);
            Long staffId = se.getStaffId();
            staffEntitySessionBeanRemote.deleteStaff(staffId);
        }
        catch(StaffNotFoundException ex)
        {
            System.out.println("An error has occurred while deleting the staff: " + ex.getMessage() + "\n");
        }
    }

    private void doViewAllStaff()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administration Operation :: Staff Management :: View All Staff ***\n");

        List<StaffEntity> staffList = staffEntitySessionBeanRemote.retrieveAllStaffs();
        String header = String.format("%s-1|%s-10|%s-10|%s-1|%s", "staffId", "First Name", "Last Name", "Username", "Password");
        System.out.println(header);
        for(StaffEntity se : staffList){
            String staffDetails = String.format("%s-1|%s-10|%s-10|%s-1|%s", se.getStaffId(), se.getFirstName(), se.getLastName(), se.getUserName(), se.getPassword());
            System.out.println(staffDetails);
        }
    }

    private void doManageLeaves()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while(true)
        {
            System.out.println("*** CARS :: Administration Operation :: Doctor Management :: Leave Management ***\n");
            System.out.println("1: Enter Leave Application");
            System.out.println("2: View Leave Details");
            System.out.println("3: Cancel Leave");
            System.out.println("4: View All Leaves");
            System.out.println("5: Back\n");
            response = 0;

            while(response < 1 || response > 5)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doAddLeave();
                }
                else if(response == 2)
                {
                    doViewLeave();
                }
                else if (response == 3)
                {
                    doDeleteLeave();
                }
                else if (response == 4)
                {
                    doViewAllLeaves();
                }
                else if(response == 5)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if(response == 6)
            {
                break;
            }
        }
    }

    private void doAddLeave()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administartion Operation :: Doctor Management :: Leave Management :: Enter Leave Application ***\n");

        System.out.println("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.println("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();
        System.out.println("Enter Leave Date> ");
        String leaveDate = scanner.nextLine().trim();

        try
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime leaveDate1 = LocalDateTime.parse(leaveDate, formatter);
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorName(firstName, lastName);
            LeaveEntity le = new LeaveEntity(de.getDoctorId(), leaveDate1);
            leaveEntitySessionBeanRemote.createNewLeave(le);
            System.out.println("Leave has been created successfully!");
        }
        catch(EntityInstanceExistsInCollectionException ex)
        {
            System.out.println("An error has occurred while adding the new staff: " + ex.getMessage() + "\n");
        }
        catch(DoctorNotFoundException ex)
        {
            System.out.println("An error has occurred while adding the new staff: " + ex.getMessage() + "\n");
        }
    }

    private void  doViewLeave()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while(true)
        {
            System.out.println("1: View Leave By Doctor");
            System.out.println("2: View Leave By Date");
            response = 0;

            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doViewLeaveByDoctor();
                }
                else if(response == 2)
                {
                    doViewLeaveByDate();
                }
            }

        }
    }


    private void  doDeleteLeave()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CARS :: Administartion Operation :: Doctor Management :: Leave Management :: Delete Leaves ***\n");

        System.out.println("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.println("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();
        System.out.println("Enter Leave Date> ");
        String leaveDate = scanner.nextLine().trim();

        try
        {
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorName(firstName, lastName);
            Long doctorId = de.getDoctorId();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime leaveDate1 = LocalDateTime.parse(leaveDate, formatter);
            List<LeaveEntity> le1 = leaveEntitySessionBeanRemote.retrieveLeaveByDate(leaveDate1);
            List<LeaveEntity> le2 = leaveEntitySessionBeanRemote.retrieveLeaveByDoctorId(doctorId);
            LeaveEntity cur = null;
            for(LeaveEntity fromDate : le1){
                for(LeaveEntity fromDoc : le2){
                    if(fromDate.equals(fromDoc)){
                        cur = fromDate;
                    }
                }
            }
            leaveEntitySessionBeanRemote.cancelLeave(cur);
            System.out.println("Leave cancelled successfully!");
        }
        catch(EntityMismatchException ex)
        {
            System.out.println("An error has occurred while cancelling the leave: " + ex.getMessage() + "\n");
        }
        catch(DoctorNotFoundException ex)
        {
            System.out.println("An error has occurred while cancelling the leave: " + ex.getMessage() + "\n");
        }
        catch(LeaveNotFoundException ex)
        {
            System.out.println("An error has occurred while cancelling the leave: " + ex.getMessage() + "\n");
        }
    }

    private void doViewAllLeaves()
    {

    }

    private void doViewLeaveByDoctor()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CARS :: Administartion Operation :: Doctor Management :: Leave Management :: View Leave Details ***\n");

        System.out.println("Enter First Name> ");
        String firstName = scanner.nextLine().trim();
        System.out.println("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();

        try
        {
            DoctorEntity de = doctorEntitySessionBeanRemote.retrieveDoctorByDoctorName(firstName, lastName);
            Long doctorId = de.getDoctorId();
            System.out.println("Leaves:");
            List<LeaveEntity> leaveList = leaveEntitySessionBeanRemote.retrieveLeaveByDoctorId(doctorId);
            String header = String.format("%s-1|%s", "doctorId", "Leave Date");
            System.out.println(header);
            for(LeaveEntity each : leaveList){
                String leaveDetails = String.format("%s-1|%s", each.getDoctorId(), each.getLeaveDate());
            }
        }
        catch(LeaveNotFoundException ex)
        {
            System.out.println("An error has occurred while viewing the leave: " + ex.getMessage() + "\n");
        }
        catch(DoctorNotFoundException ex)
        {
            System.out.println("An error has occurred while viewing the leave: " + ex.getMessage() + "\n");
        }
    }
    private void doViewLeaveByDate()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CARS :: Administartion Operation :: Doctor Management :: Leave Management :: View Leave Details ***\n");

        System.out.println("Enter Leave Date> ");
        String leaveDate = scanner.nextLine().trim();

        try
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime leaveDate1 = LocalDateTime.parse(leaveDate, formatter);
            List<LeaveEntity> leaveList = leaveEntitySessionBeanRemote.retrieveLeaveByDate(leaveDate1);
            System.out.println("Leaves:");
            String header = String.format("%s-1|%s", "doctorId", "Leave Date");
            System.out.println(header);
            for(LeaveEntity each : leaveList){
                String leaveDetails = String.format("%s-1|%s", each.getDoctorId(), each.getLeaveDate());
            }
        }
        catch(LeaveNotFoundException ex)
        {
            System.out.println("An error has occurred while viewing the leave: " + ex.getMessage() + "\n");
        }
    }

}
