package BLL;

import BE.ProfileRole;
import DAL.ProfileRole_DAO;

import java.io.IOException;
import java.util.List;

public class ProfileRoleManager {

    private ProfileRole_DAO profileRoleDAO;

    public ProfileRoleManager() throws IOException {
        profileRoleDAO = new ProfileRole_DAO();
    }

    public List<ProfileRole> getAllProfileRoles() {
        return profileRoleDAO.getAllProfileRoles();
    }
}
