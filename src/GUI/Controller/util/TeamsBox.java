package GUI.Controller.util;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamsBox {

    @FXML
    private Label lblGeography;
    @FXML
    private Label lblNumberOfMembers;
    @FXML
    private Label lblAnnualAVGTeamCost;







    private void setLblGeography(Label lblGeography) {
        this.lblGeography = lblGeography;
    }


    private void setLblNumberOfMembers(Label lblNumberOfMembers) {
        this.lblNumberOfMembers = lblNumberOfMembers;
    }



    private void setLblAnnualAVGTeamCost(Label lblAnnualAVGTeamCost) {
        this.lblAnnualAVGTeamCost = lblAnnualAVGTeamCost;
    }

    @FXML
    public void initialize() {
        setLblGeography(lblGeography);
        setLblNumberOfMembers(lblNumberOfMembers);
        setLblAnnualAVGTeamCost(lblAnnualAVGTeamCost);
    }
}
