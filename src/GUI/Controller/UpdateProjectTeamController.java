package GUI.Controller;

import BE.Geography;
import BE.Profile;
import BE.ProjectTeam;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateProjectTeamController implements Initializable {
    @FXML
    private TableColumn colTeamAnnualSalary;
    @FXML
    private TableColumn colTeamCountryId;
    @FXML
    private TableColumn colTeamHourlyRate;
    @FXML
    private TableColumn colTeamDailyRate;
    @FXML
    private TableColumn colTeamGeography;
    @FXML
    private TableColumn colTeamName;
    @FXML
    private TableColumn colTeamProfileId;
    @FXML
    private TableView tblProfileToTeam;
    @FXML
    private TextField txtUtilization;
    @FXML
    private MFXSlider sliderUtilization;
    @FXML
    private CheckComboBox cBoxProfiles;
    @FXML
    private Label lblHourlyRateSum;
    @FXML
    private Label lblDailyRateSum;
    @FXML
    private Label lblAnnualSalarySum;
    @FXML
    private CheckComboBox cBoxGeographies;
    @FXML
    private TextField txtProjectTeamName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initDataFromTeam(ProjectTeam team, Geography geography) {
        txtProjectTeamName.setText(team.getTeamName());
        lblAnnualSalarySum.setText(String.format("%.2f", team.getSumOfAnnualSalary()));
        lblDailyRateSum.setText(String.format("%.2f", team.getSumOfDailyRate()));
        lblHourlyRateSum.setText(String.format("%.2f", team.getSumOfHourlyRate()));
    }

    @FXML
    private void updateProjectTeam(ActionEvent actionEvent) {
    }

    @FXML
    private void removeProfileFromTbl(ActionEvent actionEvent) {
    }

    @FXML
    private void selectProfileToTable(ActionEvent actionEvent) {
    }


}
