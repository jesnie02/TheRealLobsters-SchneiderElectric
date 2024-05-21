package DAL;

import BE.ProfileRole;
import CustomExceptions.ApplicationWideException;

import java.util.List;

public interface IProfileRoleDataAccess {


    List<ProfileRole> getAllProfileRoles() throws ApplicationWideException;

    void createProfileRole(ProfileRole profileRole) throws ApplicationWideException;
}
