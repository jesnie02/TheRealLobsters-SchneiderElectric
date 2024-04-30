package GUI.Controller;

import javafx.scene.control.ComboBox;
import GUI.Model.CountryModel;

import java.io.IOException;

public class CreateProfileController {
    public ComboBox cBoxCountry_CreateProfile;
    public ComboBox cBoxTeam_CreateProfile;

    private CountryModel countryModel;

    public CreateProfileController() {
        try {
            countryModel = new CountryModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        try {
            cBoxCountry_CreateProfile.setItems(countryModel.getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
