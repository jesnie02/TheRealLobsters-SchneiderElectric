package DAL;

import BE.Profile;
import CustomExceptions.ApplicationWideException;

import java.util.List;

public interface IProfileDataAccess {

    List<Profile> getAllProfiles() throws ApplicationWideException;

    void createProfile(Profile newProfile) throws ApplicationWideException;


    boolean updateProfile(Profile profile) throws ApplicationWideException;
}
