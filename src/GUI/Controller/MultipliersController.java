package GUI.Controller;
import GUI.Model.CountryModel;
import GUI.Model.MultiplierModel;
import GUI.Model.ProfileModel;
import GUI.Model.TeamsModel;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MultipliersController {

    @FXML
    private MFXSlider sliderGM, sliderMU;
    @FXML
    private TextField txtGM, txtMU;
    @FXML
    private ComboBox cBoxProfile_Multiplier, cBoxTeam_Multiplier;
    @FXML
    private Label lblGMResult, lblMUResult;
    private CountryModel countryModel;
    private TeamsModel teamsModel;
    private MultiplierModel multiplierModel;
    private ProfileModel profileModel;
    private double grossMargin, markUp;
    @FXML
    private Button btnShowCalculation;


    @FXML
    private void initialize() {
        try {
            countryModel = new CountryModel();
            teamsModel = new TeamsModel();
            profileModel = new ProfileModel();
            multiplierModel = new MultiplierModel(this);

            setUpSliders();
            setValueInField();
            setUpComboBoxes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void setUpSliders() {
        sliderGM.valueProperty().addListener(((observable, oldValue, newValue) -> {
            grossMargin = newValue.doubleValue();
            setValueInField();
        }));
        sliderMU.valueProperty().addListener(((observable, oldValue, newValue) -> {
            markUp = newValue.doubleValue();
            setValueInField();
        }));
    }

    private void setUpComboBoxes() throws Exception {
        cBoxTeam_Multiplier.setItems(teamsModel.getAllProjectTeams());
        //cBoxProfile_Multiplier.setItems(profileModel.getAllProfiles());
    }

    private void setValueInField() {
        txtGM.setText(String.valueOf(grossMargin));
        txtMU.setText(String.valueOf(markUp));
    }

    public double getGMValue() {
        return grossMargin;
    }

    public double getMUValue() {
        return markUp;
    }


    @FXML
    private void calculateResult(ActionEvent actionEvent) {
        double dayRate = 100;
        double percentage = Double.parseDouble(txtMU.getText());
        double result = multiplierModel.getResultOfDayRWithMultiplier(dayRate, percentage);
        lblMUResult.setText(String.format("%.2f", result));
    }
}
