package BE;

import java.util.List;

public class ProfileRole {

    private int profileRoleId;
    private String profileRoleType;
    private List<ProfileRole> roles;

    public ProfileRole(int profileRoleId, String profileRoleType) {
        this.profileRoleId = profileRoleId;
        this.profileRoleType = profileRoleType;
    }

    public ProfileRole(String profileRoleType) {
        this.profileRoleType = profileRoleType;
    }

    public int getProfileRoleId() {
        return profileRoleId;
    }

    public void setProfileRoleId(int profileRoleId) {
        this.profileRoleId = profileRoleId;
    }

    public String getProfileRoleType() {
        return profileRoleType;
    }

    public void setProfileRoleType(String profileRoleType) {
        this.profileRoleType = profileRoleType;
    }

    @Override
    public String toString() {
        return profileRoleType;
    }

    public List<ProfileRole> getRoles() {
        return roles;
    }
}
