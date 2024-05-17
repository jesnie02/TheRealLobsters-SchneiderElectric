package DAL;

import BE.Profile;
import CustomExceptions.ApplicationWideException;

import java.util.List;

public interface IProfileDataAccess {

    List<Profile> getAllProfiles() throws ApplicationWideException;
}
