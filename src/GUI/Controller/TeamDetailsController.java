package GUI.Controller;

import BE.Geography;
import BE.Profile;
import BE.ProfileRole;
import BE.ProjectTeam;
import CustomExceptions.ApplicationWideException;
import GUI.Utility.ExceptionHandler;
import GUI.Utility.DataModelSingleton;
import GUI.Model.GeographyModel;
import GUI.Model.ProjectTeamsModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TeamDetailsController implements Initializable {

    public Button btnDeleteTeamTeamDetails;
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
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void setupTable() {
        colFirstNameTD.setCellValueFactory(new PropertyValueFactory<>("FName"));
        colLastNameTD.setCellValueFactory(new PropertyValueFactory<>("LName"));
        colUtilizationTD.setCellValueFactory(new PropertyValueFactory<>("Utilization"));
        colProfileRoleTD.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getProfileRoles().stream()
                        .map(ProfileRole::toString)
                        .collect(Collectors.joining(", "))
        ));
    }

    //Here we update the UI with the data from the team and geography
    public void updateUI(ProjectTeam team, Geography geography) {
        DataModelSingleton.getInstance().setCurrentTeam(team);
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
        System.out.println("Sum of annual salary: " + team.getSumOfAnnualSalary() + " from team: " + team.getTeamName());
        txtSumDaily.setText(String.format("%.2f", team.getSumOfDailyRate()));
        txtSumHourly.setText(String.format("%.2f", team.getSumOfHourlyRate()));
        txtAvgAnnual.setText(String.format("%.2f", team.getAvgAnnualSalary()));
        txtAvgDaily.setText(String.format("%.2f", team.getAvgDailyRate()));
        txtAvgHourly.setText(String.format("%.2f", team.getAvgHourlyRate()));
    }


    @FXML
    private void deleteProjectTeam(ActionEvent actionEvent) throws ApplicationWideException {
        ProjectTeam currentTeam = (ProjectTeam) DataModelSingleton.getInstance().getCurrentTeam();
        String teamName = currentTeam.getTeamName();

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the team: " + teamName + "?", ButtonType.YES, ButtonType.NO);
        confirmAlert.setHeaderText("Confirm Deletion");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                projectTeam.deleteTeam(currentTeam);
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Deletion Successful");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("The team " + teamName + " has been successfully deleted.");
                infoAlert.showAndWait();
                frameController.loadTeamsView(); //TODO initialize the view with the updated data, it does that but it duplicates the teams in the view, which it should not
                TeamsController.getInstance().loadTeamsInTilePane();
            } catch (Exception e) {
                throw new ApplicationWideException("Error during team deletion: " + e.getMessage(), e);
            }
        }
    }

    @FXML
    private void updateProjectTeam(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/updateProjectTeamView.fxml"));
            Node updateNode = loader.load();
            UpdateProjectTeamController controller = loader.getController();
            controller.initDataFromTeam();
            frameController.setMainView(updateNode);
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }
}
