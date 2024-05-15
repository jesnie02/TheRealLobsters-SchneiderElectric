package GUI.Controller;

import BE.Geography;
import BE.Profile;
import BE.ProfileRole;
import BE.ProjectTeam;
import GUI.Model.GeographyModel;
import GUI.Model.ProjectTeamsModel;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TeamDetailsController implements Initializable {

    private ProjectTeamsModel projectTeam;
    private FrameController frameController;
    private GeographyModel geographyModel;

    @FXML
    private TextField txtTDGeography, txtSumAnnual, txtSumDaily, txtSumHourly, txtAvgAnnual, txtAvgDaily, txtAvgHourly;
    @FXML
    private Label lblTeamInTeamDetail;
    @FXML
    TableView<Profile> tableViewProfile;
    @FXML
    private TableColumn <Profile, String> colFirstNameTD, colLastNameTD, colProfileRoleTD;
    @FXML
    private TableColumn<Profile,  Double> colUtilizationTD;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            projectTeam = new ProjectTeamsModel();
            geographyModel = new GeographyModel();
            frameController = FrameController.getInstance();
            setupTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupTable() {
        colFirstNameTD.setCellValueFactory(new PropertyValueFactory<>("FName"));
        colLastNameTD.setCellValueFactory(new PropertyValueFactory<>("LName"));
        colUtilizationTD.setCellValueFactory(new PropertyValueFactory<>("totalUtilization"));
        colProfileRoleTD.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getProfileRoles().stream()
                        .map(ProfileRole::toString)
                        .collect(Collectors.joining(", "))
        ));
    }

    //Here we update the UI with the data from the team and geography
    public void updateUI(ProjectTeam team, Geography geography) {
        lblTeamInTeamDetail.setText(team.getTeamName());
        if (geography != null){
            txtTDGeography.setText(geography.getGeographyName());
        } else {
            txtTDGeography.setText("No Geography");
        }
        updateSalaryInfo(team);
        ObservableList<Profile> profiles = projectTeam.getProfileForTeam(team.getTeamId());
        tableViewProfile.setItems(profiles);
    }

    private void updateSalaryInfo(ProjectTeam team){
        txtSumAnnual.setText(String.format("%.2f", team.getSumOfAnnualSalary()));
        txtSumDaily.setText(String.format("%.2f", team.getSumOfDailyRate()));
        txtSumHourly.setText(String.format("%.2f", team.getSumOfHourlyRate()));
        txtAvgAnnual.setText(String.format("%.2f", team.getAvgAnnualSalary()));
        txtAvgDaily.setText(String.format("%.2f", team.getAvgDailyRate()));
        txtAvgHourly.setText(String.format("%.2f", team.getAvgHourlyRate()));
    }


    @FXML
    private void deleteProjectTeam(ActionEvent actionEvent) {
    }

    @FXML
    private void updateProjectTeam(ActionEvent actionEvent) {
        frameController.loadUpdateProjectTeamView();
    }
}
