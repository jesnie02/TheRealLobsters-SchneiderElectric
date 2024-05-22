package GUI.Controller;

import BE.Country;
import BE.Geography;
import BE.ProjectTeam;
import CustomExceptions.ApplicationWideException;
import GUI.Model.CountryModel;
import GUI.Model.GeographyModel;
import GUI.Model.ProjectTeamsModel;
import GUI.Utility.ExceptionHandler;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    public Label lblDailyRateSumCountry, lblHourlyRateSumCountry, lblAvgDailyRateCountry, lblAvgHourlyRateCountry;

    @FXML
    public TabPane tabPaneDashboard;

    @FXML
    private Label lblAvgHourlyRateTeam, lblAvgDailyRateTeam, lblHourlyRateSumTeam, lblDailyRateSumTeam;


    @FXML
    private ComboBox<Geography> cBoxGeographyDash;
    @FXML
    private ComboBox<ProjectTeam> cBoxTeamDash;

    private CountryModel countryModel;
    private ProjectTeamsModel projectTeamsModel;
    private GeographyModel geographyModel;

    @FXML
    private Label lblSumDailyRateGeo, lblSumHourlyRateGeo, lblAvgDailyRateGeo, lblAvgHourlyRateGeo;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private MFXLegacyTableView<ProjectTeam> tblTeams;
    @FXML
    private TableColumn<ProjectTeam, String> colTeams;
    @FXML
    private MFXLegacyTableView<Geography> tblGeography;
    @FXML
    private TableColumn<Geography, String> colGeographies;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            countryModel = new CountryModel();
            projectTeamsModel = new ProjectTeamsModel();
            geographyModel = new GeographyModel();

            // Populate the AreaChart with team expenses
            populateBarChartWithTeamExpenses();

        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
        setUpComboBoxListeners();
    }

    private void setUpComboBoxListeners() {

            // Geography
            colGeographies.setCellValueFactory(new PropertyValueFactory<>("geographyName"));
            tblGeography.setItems(geographyModel.getSumsAndAveragesForGeographies());
            tblGeography.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                updateLabelsGeographyTab(newValue);
            });


            // Team
            colTeams.setCellValueFactory(new PropertyValueFactory<>("TeamName"));
            tblTeams.setItems(projectTeamsModel.getAllProjectTeamsData());
            tblTeams.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                updateLabelsTeamTab(newValue);
            });
    }

    private void populateBarChartWithTeamExpenses() {

            // Fetch top 10 project teams by annual salary from the model
            List<ProjectTeam> top10ProjectTeams = projectTeamsModel.getTop10ProjectTeamsByAnnualSalary();

            // Clear any existing data
            barChart.getData().clear();
            xAxis.getCategories().clear();

            // Add team names to the xAxis categories
            for (ProjectTeam team : top10ProjectTeams) {
                xAxis.getCategories().add(team.getTeamName());
            }

            // Create a data series for SumOfAnnualSalary
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Sum of Annual Salary");

            for (ProjectTeam team : top10ProjectTeams) {
                series.getData().add(new XYChart.Data<>(team.getTeamName(), team.getSumOfAnnualSalary()));
            }

            // Add the series to the chart
            barChart.getData().add(series);

    }

    private void updateLabelsGeographyTab(Geography selectedGeography) {
        if (selectedGeography != null) {
            lblSumDailyRateGeo.setText(String.format("%.2f", selectedGeography.getSumOfDailyRate()));
            lblSumHourlyRateGeo.setText(String.format("%.2f", selectedGeography.getSumOfHourlyRate()));
            lblAvgDailyRateGeo.setText(String.format("%.2f", selectedGeography.getAvgDailyRate()));
            lblAvgHourlyRateGeo.setText(String.format("%.2f", selectedGeography.getAvgHourlyRate()));
        } else {
            // Clear labels if no geography is selected
            lblSumDailyRateGeo.setText("");
            lblSumHourlyRateGeo.setText("");
            lblAvgDailyRateGeo.setText("");
            lblAvgHourlyRateGeo.setText("");
        }
    }

    private void updateLabelsTeamTab(ProjectTeam selectedTeam) {
        if (selectedTeam != null) {
            lblHourlyRateSumTeam.setText(String.format("%.2f", selectedTeam.getSumOfHourlyRate()));
            lblDailyRateSumTeam.setText(String.format("%.2f", selectedTeam.getSumOfDailyRate()));
            lblAvgDailyRateTeam.setText(String.format("%.2f", selectedTeam.getAvgDailyRate()));
            lblAvgHourlyRateTeam.setText(String.format("%.2f", selectedTeam.getAvgHourlyRate()));
        } else {
            // Clear labels if no team is selected
            lblHourlyRateSumTeam.setText("");
            lblDailyRateSumTeam.setText("");
            lblAvgDailyRateTeam.setText("");
            lblAvgHourlyRateTeam.setText("");
        }
    }
}
