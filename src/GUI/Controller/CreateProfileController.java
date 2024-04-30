package GUI.Controller;

import GUI.Model.TeamsModel;
import javafx.scene.control.ComboBox;
import GUI.Model.CountryModel;

import java.io.IOException;

public class CreateProfileController {
    public ComboBox cBoxCountry_CreateProfile;
    public ComboBox cBoxTeam_CreateProfile;

    private CountryModel countryModel;
    private TeamsModel teamsModel;

    public CreateProfileController() {
        try {
            countryModel = new CountryModel();
            teamsModel = new TeamsModel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        try {
            cBoxCountry_CreateProfile.setItems(countryModel.getAllCountries());
            cBoxTeam_CreateProfile.setItems(teamsModel.getAllProjectTeams());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
