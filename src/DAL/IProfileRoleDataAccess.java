package DAL;

import BE.ProfileRole;

import java.util.List;

public interface IProfileRoleDataAccess {


    List<ProfileRole> getAllProfileRoles();
    void createProfileRole(ProfileRole profileRole);
}
