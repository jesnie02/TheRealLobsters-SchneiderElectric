package BLL;

import BE.ProfileRole;
import CustomExceptions.ApplicationWideException;
import DAL.ProfileRole_DAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProfileRoleManager {

    private ProfileRole_DAO profileRoleDAO;

    public ProfileRoleManager() throws ApplicationWideException {
        profileRoleDAO = new ProfileRole_DAO();
    }

    public List<ProfileRole> getAllProfileRoles() throws ApplicationWideException {
        return profileRoleDAO.getAllProfileRoles();
    }

    public void createRole(ProfileRole profileRole) throws ApplicationWideException {
        profileRoleDAO.createProfileRole(profileRole);
    }


}
