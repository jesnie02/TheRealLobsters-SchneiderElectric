package GUI.Controller.util;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TeamsBox {

    @FXML
    private Label lblGeography, lblNumberOfMembers, lblAnnualAVGTeamCost;

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
