package GUI.Controller;

import BE.Geography;
import BE.ProjectTeam;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamDetailsController implements Initializable {


    @FXML
    private TextField txtTDGeography, txtSumAnnual, txtSumDaily, txtSumHourly, txtAvgAnnual, txtAvgDaily, txtAvgHourly;
    @FXML
    private Label lblTeamInTeamDetail;
    @FXML
    private TableColumn colFirstNameTD, colLastNameTD, colUtilizationTD, colProfileRoleTD;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void updateUI(ProjectTeam team, Geography geography) {
        lblTeamInTeamDetail.setText(team.getTeamName());
        if (geography != null){
            txtTDGeography.setText(geography.getGeographyName());
        } else {
            txtTDGeography.setText("No Geography");
        }
        txtSumAnnual.setText(String.format("%.2f", team.getSumOfAnnualSalary()));
        txtSumDaily.setText(String.format("%.2f", team.getSumOfDailyRate()));
        txtSumHourly.setText(String.format("%.2f", team.getSumOfHourlyRate()));
        txtAvgAnnual.setText(String.format("%.2f", team.getAvgAnnualSalary()));
        txtAvgDaily.setText(String.format("%.2f", team.getAvgDailyRate()));
        txtAvgHourly.setText(String.format("%.2f", team.getAvgHourlyRate()));
    }
}
